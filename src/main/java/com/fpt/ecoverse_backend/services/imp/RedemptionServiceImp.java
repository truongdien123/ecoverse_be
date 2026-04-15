package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.responses.RedemptionResponseDto;
import com.fpt.ecoverse_backend.entities.*;
import com.fpt.ecoverse_backend.enums.ApprovalStatus;
import com.fpt.ecoverse_backend.enums.UserType;
import com.fpt.ecoverse_backend.exceptions.BadRequestException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.RedemptionRequestMapper;
import com.fpt.ecoverse_backend.repositories.*;
import com.fpt.ecoverse_backend.services.RedemptionService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RedemptionServiceImp implements RedemptionService {

    private final RedemptionRequestRepository redemptionRequestRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final PartnerRepository partnerRepository;
    private final RewardItemRepository rewardItemRepository;
    private final RedemptionRequestMapper redemptionRequestMapper;
    private final UserRepository userRepository;

    public RedemptionServiceImp(RedemptionRequestRepository redemptionRequestRepository, StudentRepository studentRepository, ParentRepository parentRepository, PartnerRepository partnerRepository, RewardItemRepository rewardItemRepository, RedemptionRequestMapper redemptionRequestMapper, UserRepository userRepository) {
        this.redemptionRequestRepository = redemptionRequestRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.partnerRepository = partnerRepository;
        this.rewardItemRepository = rewardItemRepository;
        this.redemptionRequestMapper = redemptionRequestMapper;
        this.userRepository = userRepository;
    }

    @Override
    public RedemptionResponseDto createRedemption(String studentId, String rewardItemId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("Student not found");
        }
        Optional<RewardItem> rewardItem = rewardItemRepository.findById(rewardItemId);
        if (rewardItem.isEmpty()) {
            throw new NotFoundException("Reward item not found");
        }
        if (student.get().getPoints() < rewardItem.get().getPointsRequired()) {
            throw new BadRequestException("Not enough points to redeem this reward");
        }
        RedemptionRequest redemptionRequest = new RedemptionRequest();
        redemptionRequest.setStudent(student.get());
        redemptionRequest.setRewardItem(rewardItem.get());
        redemptionRequest.setParent(student.get().getParent());
        redemptionRequest.setPartner(rewardItem.get().getPartner());
        redemptionRequest.setParentApproval(ApprovalStatus.PENDING);
        redemptionRequest.setPartnerApproval(ApprovalStatus.PENDING);
        redemptionRequest.setFulfilled(false);
        redemptionRequestRepository.save(redemptionRequest);
        student.get().setPoints(student.get().getPoints() - rewardItem.get().getPointsRequired());
        studentRepository.save(student.get());
        RedemptionResponseDto response = redemptionRequestMapper.toRedemptionResponse(redemptionRequest, redemptionRequest.getId());
        response.setStudentName(student.get().getUser().getFullName());
        response.setRewardItemName(rewardItem.get().getName());
        response.setImageRewardItem(rewardItem.get().getImageUrl());
        response.setAvatarUrl(student.get().getUser().getAvatarUrl());
        return response;
    }

    @Override
    public RedemptionResponseDto approveRedemptionByParent(String parentId, String redemptionId, boolean approved, String parentReason) {
        Optional<Parent> parent = parentRepository.findById(parentId);
        if (parent.isEmpty()) {
            throw new NotFoundException("Parent not found");
        }
        Optional<RedemptionRequest> redemptionRequest = redemptionRequestRepository.findById(redemptionId);
        if (redemptionRequest.isEmpty()) {
            throw new NotFoundException("Redemption request not found");
        }
        if (!redemptionRequest.get().getParent().getId().equals(parentId)) {
            throw new BadRequestException("Redemption request not for this parent");
        }
        if (redemptionRequest.get().getParentApproval() != ApprovalStatus.PENDING) {
            throw new BadRequestException("Already processed by parent");
        }
        ApprovalStatus oldParent = redemptionRequest.get().getParentApproval();
        ApprovalStatus oldPartner = redemptionRequest.get().getPartnerApproval();
        redemptionRequest.get().setParentApproval(approved ? ApprovalStatus.APPROVED : ApprovalStatus.REJECTED);
        if (redemptionRequest.get().getParentApproval() == ApprovalStatus.REJECTED) {
            redemptionRequest.get().setReasonParent(parentReason);
            refundPointsIfFirstRejection(redemptionRequest.get(), oldParent, oldPartner);
        }
        redemptionRequestRepository.save(redemptionRequest.get());
        RedemptionResponseDto response = redemptionRequestMapper.toRedemptionResponse(redemptionRequest.get(), redemptionRequest.get().getId());
        response.setStudentName(redemptionRequest.get().getStudent().getUser().getFullName());
        response.setRewardItemName(redemptionRequest.get().getRewardItem().getName());
        response.setImageRewardItem(redemptionRequest.get().getRewardItem().getImageUrl());
        response.setAvatarUrl(parent.get().getUser().getAvatarUrl());
        return response;
    }

    @Override
    public RedemptionResponseDto approveRedemptionByPartner(String partnerId, String redemptionId, boolean approved, String partnerReason) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Partner not found");
        }
        Optional<RedemptionRequest> redemptionRequest = redemptionRequestRepository.findById(redemptionId);
        if (redemptionRequest.isEmpty()) {
            throw new NotFoundException("Redemption request not found");
        }
        if (!redemptionRequest.get().getPartner().getId().equals(partnerId)) {
            throw new BadRequestException("Redemption request not for this partner");
        }
        if (redemptionRequest.get().getParentApproval() != ApprovalStatus.APPROVED) {
            throw new BadRequestException("Parent must approve first");
        }
        if (redemptionRequest.get().getPartnerApproval() != ApprovalStatus.PENDING) {
            throw new BadRequestException("Already processed by partner");
        }
        ApprovalStatus oldParent = redemptionRequest.get().getParentApproval();
        ApprovalStatus oldPartner = redemptionRequest.get().getPartnerApproval();
        redemptionRequest.get().setPartnerApproval(approved ? ApprovalStatus.APPROVED : ApprovalStatus.REJECTED);
        if (redemptionRequest.get().getPartnerApproval() == ApprovalStatus.REJECTED) {
            redemptionRequest.get().setReasonPartner(partnerReason);
            refundPointsIfFirstRejection(redemptionRequest.get(), oldParent, oldPartner);
        }
        redemptionRequestRepository.save(redemptionRequest.get());
        RedemptionResponseDto response = redemptionRequestMapper.toRedemptionResponse(redemptionRequest.get(), redemptionRequest.get().getId());
        response.setStudentName(redemptionRequest.get().getStudent().getUser().getFullName());
        response.setRewardItemName(redemptionRequest.get().getRewardItem().getName());
        response.setImageRewardItem(redemptionRequest.get().getRewardItem().getImageUrl());
        response.setAvatarUrl(partner.get().getUser().getAvatarUrl());
        return response;
    }

    @Override
    public RedemptionResponseDto fulfillRedemption(String redemptionId, String partnerId) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Partner not found");
        }
        Optional<RedemptionRequest> redemptionRequest = redemptionRequestRepository.findById(redemptionId);
        if (redemptionRequest.isEmpty()) {
            throw new NotFoundException("Redemption request not found");
        }
        if (redemptionRequest.get().getFulfilled()) {
            throw new BadRequestException("Already fulfilled");
        }
        if (redemptionRequest.get().getParentApproval() != ApprovalStatus.APPROVED || redemptionRequest.get().getPartnerApproval() != ApprovalStatus.APPROVED) {
            throw new BadRequestException("Redemption request not approved by parent or partner");
        }
        if (!redemptionRequest.get().getPartner().getId().equals(partnerId)) {
            throw new BadRequestException("Redemption request not for this partner");
        }
        redemptionRequest.get().setFulfilled(true);
        redemptionRequestRepository.save(redemptionRequest.get());
        RedemptionResponseDto response = redemptionRequestMapper.toRedemptionResponse(redemptionRequest.get(), redemptionRequest.get().getId());
        response.setStudentName(redemptionRequest.get().getStudent().getUser().getFullName());
        response.setRewardItemName(redemptionRequest.get().getRewardItem().getName());
        response.setImageRewardItem(redemptionRequest.get().getRewardItem().getImageUrl());
        response.setAvatarUrl(partner.get().getUser().getAvatarUrl());
        return response;
    }

    private void refundPointsIfFirstRejection(RedemptionRequest request,
                                              ApprovalStatus oldParentStatus,
                                              ApprovalStatus oldPartnerStatus) {

        boolean wasRejectedBefore =
                oldParentStatus == ApprovalStatus.REJECTED ||
                        oldPartnerStatus == ApprovalStatus.REJECTED;

        boolean isRejectedNow =
                request.getParentApproval() == ApprovalStatus.REJECTED ||
                        request.getPartnerApproval() == ApprovalStatus.REJECTED;

        if (!wasRejectedBefore && isRejectedNow) {
            Student student = request.getStudent();
            int points = request.getRewardItem().getPointsRequired();

            student.setPoints(student.getPoints() + points);
            studentRepository.save(student);
        }
    }

    @Override
    public List<RedemptionResponseDto> getRedemptionRequests(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<RedemptionRequest> redemptionRequests;
        switch (user.getRole()) {
            case STUDENT:
                redemptionRequests = redemptionRequestRepository.findByStudentId(userId);
                break;
            case PARENT:
                redemptionRequests = redemptionRequestRepository.findByParentId(userId);
                break;
            case PARTNERSHIP:
                redemptionRequests = redemptionRequestRepository.findByPartnerId(userId);
                break;
            default:
                throw new BadRequestException("Invalid user role");
        }
        return redemptionRequests.stream().map(redemptionRequest -> {
            RedemptionResponseDto response =
                    redemptionRequestMapper.toRedemptionResponse(redemptionRequest, redemptionRequest.getId());
            response.setStudentName(
                    redemptionRequest.getStudent().getUser().getFullName()
            );
            response.setRewardItemName(
                    redemptionRequest.getRewardItem().getName()
            );
            response.setImageRewardItem(
                    redemptionRequest.getRewardItem().getImageUrl()
            );
            if (user.getRole() == UserType.PARENT) {
                response.setAvatarUrl(
                        redemptionRequest.getStudent().getUser().getAvatarUrl()
                );
            } else if (user.getRole() == UserType.PARTNERSHIP) {
                response.setAvatarUrl(
                        redemptionRequest.getPartner().getUser().getAvatarUrl()
                );
            } else if (user.getRole() == UserType.STUDENT) {
                response.setAvatarUrl(
                        redemptionRequest.getParent().getUser().getAvatarUrl()
                );
            }
            return response;
        }).toList();
    }

    @Override
    public RedemptionResponseDto createRedemptionByParent(String parentId, String studentId, String rewardItemId) {
        Optional<Parent> parent = parentRepository.findById(parentId);
        if (parent.isEmpty()) {
            throw new NotFoundException("Not found parent");
        }
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("Not found student");
        }
        Optional<RewardItem> rewardItem = rewardItemRepository.findById(rewardItemId);
        if (rewardItem.isEmpty()) {
            throw new NotFoundException("Not found reward item");
        }
        if (!student.get().getParent().equals(parent.get())) {
            throw new BadRequestException("Student is not children of parent");
        }
        RedemptionRequest redemptionRequest = new RedemptionRequest();
        redemptionRequest.setStudent(student.get());
        redemptionRequest.setRewardItem(rewardItem.get());
        redemptionRequest.setParent(student.get().getParent());
        redemptionRequest.setPartner(rewardItem.get().getPartner());
        redemptionRequest.setParentApproval(ApprovalStatus.APPROVED);
        redemptionRequest.setPartnerApproval(ApprovalStatus.PENDING);
        redemptionRequest.setFulfilled(false);
        redemptionRequestRepository.save(redemptionRequest);
        student.get().setPoints(student.get().getPoints() - rewardItem.get().getPointsRequired());
        studentRepository.save(student.get());
        RedemptionResponseDto response = redemptionRequestMapper.toRedemptionResponse(redemptionRequest, redemptionRequest.getId());
        response.setStudentName(student.get().getUser().getFullName());
        response.setRewardItemName(rewardItem.get().getName());
        response.setImageRewardItem(rewardItem.get().getImageUrl());
        response.setAvatarUrl(student.get().getUser().getAvatarUrl());
        return response;
    }
}
