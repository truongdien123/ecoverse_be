package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.*;
import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PartnerRegisterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PartnerUpdateRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.*;
import com.fpt.ecoverse_backend.entities.Parent;
import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.entities.Student;
import com.fpt.ecoverse_backend.exceptions.BadRequestException;
import com.fpt.ecoverse_backend.exceptions.FileHandlingException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.ParentMapper;
import com.fpt.ecoverse_backend.mappers.PartnerMapper;
import com.fpt.ecoverse_backend.mappers.StudentMapper;
import com.fpt.ecoverse_backend.repositories.*;
import com.fpt.ecoverse_backend.services.PartnerService;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class PartnerServiceImp implements PartnerService {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_REGEX =
            Pattern.compile("^\\+?[0-9]{9,11}$");
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;
    private final PasswordEncoder passwordEncoder;
    private final UploadFile uploadFile;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final CheckingUserRepository checkingUserRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ParentMapper parentMapper;
    private final StudentMapper studentMapper;

    public PartnerServiceImp(PartnerRepository partnerRepository, PartnerMapper partnerMapper, PasswordEncoder passwordEncoder, UploadFile uploadFile, ParentRepository parentRepository, StudentRepository studentRepository, AdminRepository adminRepository, CheckingUserRepository checkingUserRepository, ApplicationEventPublisher eventPublisher, ParentMapper parentMapper, StudentMapper studentMapper) {
        this.partnerRepository = partnerRepository;
        this.partnerMapper = partnerMapper;
        this.passwordEncoder = passwordEncoder;
        this.uploadFile = uploadFile;
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
        this.checkingUserRepository = checkingUserRepository;
        this.eventPublisher = eventPublisher;
        this.parentMapper = parentMapper;
        this.studentMapper = studentMapper;
    }

    @Override
    @Transactional
    public PartnerResponseDto createPartner(PartnerRegisterRequestDto request) {
        boolean existingEmail = partnerRepository.existsByEmail(request.getEmail())
                || parentRepository.existsByEmail(request.getEmail())
                || adminRepository.existsByEmail(request.getEmail());
        if (existingEmail) {
            throw new BadRequestException("Email already exist");
        }
        boolean existingPhoneNumber = partnerRepository.existsByPhoneNumber(request.getPhoneNumber())
                || parentRepository.existsByPhoneNumber(request.getPhoneNumber())
                || adminRepository.existsByPhoneNumber(request.getPhoneNumber());
        if (existingPhoneNumber) {
            throw new BadRequestException("Phone number already exist");
        }
        Partner partner = partnerMapper.toPartner(request, uploadFile);
        partner.setPassword(passwordEncoder.encode(request.getPassword()));
        partnerRepository.save(partner);
        return partnerMapper.toPartnerResponse(partner);
    }

    @Override
    public PartnerResponseDto getDetailPartner(String partnerId) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        PartnerResponseDto partnerResponseDto = partnerMapper.toPartnerResponse(partner.get());
        StatisticPartner statisticPartner = new StatisticPartner();
        statisticPartner.setTotalParents(parentRepository.countParentsByPartnerId(partnerId));
        statisticPartner.setTotalStudents(studentRepository.countStudentByPartnerId(partnerId));
        statisticPartner.setTotalActiveGames(0);
        statisticPartner.setTotalActiveQuizzes(0);
        statisticPartner.setTotalPointDistributed(0);
        statisticPartner.setTotalRedemptions(0);
        partnerResponseDto.setStatistics(statisticPartner);
        return partnerResponseDto;
    }

    @Override
    @Transactional
    public PartnerResponseDto updatePartner(String partnerId, PartnerUpdateRequestDto request) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        partnerMapper.toPartner(partner.get(), request, uploadFile);
        if (request.getAvatar() != null) {
            partner.get().setAvatarUrl(uploadFile.imageToUrl(request.getAvatar()));
        }
        partnerRepository.save(partner.get());
        PartnerResponseDto partnerResponseDto = partnerMapper.toPartnerResponse(partner.get());
        StatisticPartner statisticPartner = new StatisticPartner();
        statisticPartner.setTotalParents(parentRepository.countParentsByPartnerId(partnerId));
        statisticPartner.setTotalStudents(studentRepository.countStudentByPartnerId(partnerId));
        statisticPartner.setTotalActiveGames(0);
        statisticPartner.setTotalActiveQuizzes(0);
        statisticPartner.setTotalPointDistributed(0);
        statisticPartner.setTotalRedemptions(0);
        partnerResponseDto.setStatistics(statisticPartner);
        return partnerResponseDto;
    }

    @Override
    @Transactional
    public BulkCreateReportResponseDto bulkCreate(MultipartFile file, String partnerId) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        ParsedTwoSheets parsed = readTwoSheets(file);
        List<RowResult<ParentExcelRowDto>> parentResults = new ArrayList<>();
        List<RowResult<StudentExcelRowDto>> studentResults = new ArrayList<>();
        List<ParentCredentialMail> createdParentMails = new ArrayList<>();
        Set<String> parentEmailInFile = new HashSet<>();
        for (RowInput<ParentExcelRowDto> input : parsed.parentRows) {
            String email = input.getDto().getEmail().toLowerCase();
            parentEmailInFile.add(email);
        }
        Set<String> existingEmails = checkingUserRepository.findExistingEmail(parentEmailInFile);
        Set<String> seenParentEmailsInFile = new HashSet<>();
        for (RowInput<ParentExcelRowDto> input : parsed.parentRows) {
            int rowNo = input.getExcelRowNumber();
            ParentExcelRowDto dto = input.getDto();
            String email = dto.getEmail().trim().toLowerCase();
            String error = validateParent(dto, email);
            if (error != null) {
                parentResults.add(RowResult.failed(rowNo, dto, error));
                continue;
            }
            if (!seenParentEmailsInFile.add(email)) {
                parentResults.add(RowResult.failed(rowNo, dto, "Duplicate email in parent sheet"));
                continue;
            }
            if (existingEmails.contains(email)) {
                parentResults.add(RowResult.failed(rowNo, dto, "Email already exist"));
                continue;
            }
            String password = generatePassword();
            String rawPassword = passwordEncoder.encode(password);

            Parent parent = new Parent();
            parent.setFullName(dto.getFullName().trim());
            parent.setEmail(email);
            parent.setPhoneNumber(dto.getPhoneNumber().replaceAll("\\s+", ""));
            parent.setAddress(dto.getAddress().trim());
            parent.setPassword(rawPassword);
            parent.setPartner(partner.get());

            parentRepository.save(parent);

            createdParentMails.add(new ParentCredentialMail(parent.getEmail(), parent.getFullName(), password));
            parentResults.add(RowResult.success(rowNo, dto, "Add successfully"));

        }
        eventPublisher.publishEvent(new ParentsCreatedEvent(createdParentMails));
        for (RowInput<StudentExcelRowDto> input : parsed.studentRows) {
            int rowNo = input.getExcelRowNumber();
            StudentExcelRowDto dto = input.getDto();
            if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
                studentResults.add(RowResult.failed(rowNo, dto, "Student fullname required"));
                continue;
            }
            if (dto.getGrade() == null || dto.getGrade().trim().isEmpty()) {
                studentResults.add(RowResult.failed(rowNo, dto, "Student grade required"));
            }
            String studentCode = generateStudentCode(dto.getFullName(), dto.getGrade(), dto.getClassNumber());
            Student student = new Student();
            student.setFullName(dto.getFullName().trim());
            student.setGrade(dto.getGrade());
            student.setStudentCode(studentCode);
            student.setPartner(partner.get());

            studentRepository.save(student);
            studentResults.add(RowResult.success(rowNo, dto, "Add successfully"));
        }
        int totalRows = parentResults.size() + studentResults.size();

        long parentSuccess = parentResults.stream().filter(RowResult::isSuccess).count();
        long studentSuccess = studentResults.stream().filter(RowResult::isSuccess).count();

        int successful = (int) (parentSuccess + studentSuccess);
        int failed = totalRows - successful;

// generate report file
        String reportFileName = "bulk_import_report_" + System.currentTimeMillis() + ".xlsx";
        String reportFileUrl = generateReportFile(parentResults, studentResults, reportFileName);

        BulkCreateReportResponseDto response = new BulkCreateReportResponseDto();
        response.setType("PARENT_STUDENT");
        response.setTotalRows(totalRows);
        response.setSuccessful(successful);
        response.setFailed(failed);
        response.setProcessedAt(LocalDateTime.now());
        response.setReportFileUrl(reportFileUrl);
        response.setReportFileName(reportFileName);
        response.setExpiresIn(86400); // 24h

        return response;
    }

    private String getInitialInFullName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return "";
        }
        String[] words = fullName.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (String word : words) {
            initials.append(Character.toUpperCase(word.charAt(0)));
        }
        return initials.toString();
    }

    private String generateStudentCode(String fullName, String grade, String order) {
        String initials = getInitialInFullName(fullName);
        return initials+grade+order;
    }

    @Override
    public StudentResponseDto getStudentDetail(String partnerId, String studentId) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("Not found student");
        }
        StatisticStudent statistic = new StatisticStudent();
        StudentResponseDto studentResponseDto = studentMapper.toStudentResponse(student.get());
        if (student.get().getParent() != null) {
            Optional<Parent> parent = parentRepository.findById(student.get().getParent().getId());
            studentResponseDto.setParent(parentMapper.toParentResponse(parent.get()));
        }
        studentResponseDto.setStatistics(statistic);
        return studentResponseDto;
    }

    @Override
    public StudentResponseDto changeStatusStudent(String partnerId, String studentId, String status) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("Not found student");
        }
        if (status.equalsIgnoreCase("active")) {
            student.get().setActive(true);
            studentRepository.save(student.get());
        } else {
            student.get().setActive(false);
            studentRepository.save(student.get());
        }
        return studentMapper.toStudentResponse(student.get());
    }

    @Override
    public UserListResponseDto<?> getListUser(String partnerId, PageFilterRequestDto pageFilterRequestDto) {
        Pageable pageable = PageRequest.of(
                pageFilterRequestDto.getPageNo()-1,
                pageFilterRequestDto.getPageSize(),
                Sort.by(pageFilterRequestDto.getSorting()).descending());
        if (pageFilterRequestDto.getType().equalsIgnoreCase("student")) {
            Page<Student> students = studentRepository.searchStudents(
                    partnerId, pageFilterRequestDto.getSearching(), pageFilterRequestDto.getGrade(), pageable);
            List<StudentResponseDto> list = students.getContent().stream().map(studentMapper::toStudentResponse).toList();
            return new UserListResponseDto<>(
                    list,
                    pageFilterRequestDto.getPageNo(),
                    pageFilterRequestDto.getPageSize(),
                    students.getTotalElements(),
                    students.getTotalPages());
        } else if (pageFilterRequestDto.getType().equalsIgnoreCase("parent")) {
            Page<Parent> parents = parentRepository.searchParents(
                    partnerId, pageFilterRequestDto.getSearching(), pageFilterRequestDto.isHasChildren(), pageable);
            List<ParentResponseDto> list = parents.getContent().stream().map(parentMapper::toParentResponse).toList();
            return new UserListResponseDto<>(
                    list,
                    pageFilterRequestDto.getPageNo(),
                    pageFilterRequestDto.getPageSize(),
                    parents.getTotalElements(),
                    parents.getTotalPages());
        }
        throw new BadRequestException("Invalid type");
    }

    @Override
    public List<StudentResponseDto> createStudents(String partnerId, List<StudentRequestDto> studentRequestDtos) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        List<Student> students = new ArrayList<>();
        for (StudentRequestDto dto : studentRequestDtos) {
            Student student = studentMapper.toStudent(dto);
            student.setPartner(partner.get());
            String studentCode = generateStudentCode(dto.getFullName(), dto.getGrade(), dto.getClassNumber());
            student.setStudentCode(studentCode);
            students.add(student);
        }
        studentRepository.saveAll(students);
        return students.stream().map(studentMapper::toStudentResponse).toList();
    }

    private String generateReportFile(
            List<RowResult<ParentExcelRowDto>> parentResults,
            List<RowResult<StudentExcelRowDto>> studentResults,
            String fileName) {

        try (Workbook workbook = new XSSFWorkbook()) {

            // ===== Parent sheet =====
            Sheet parentSheet = workbook.createSheet("Parent Result");

            int rowIdx = 0;
            for (RowResult<ParentExcelRowDto> result : parentResults) {

                Row row = parentSheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(result.getData().getFullName());
                row.createCell(1).setCellValue(result.getData().getEmail());
                row.createCell(2).setCellValue(result.getData().getPhoneNumber());
                row.createCell(3).setCellValue(result.getData().getAddress());
                row.createCell(4).setCellValue(result.getMessage());
            }

            // ===== Student sheet =====
            Sheet studentSheet = workbook.createSheet("Student Result");

            rowIdx = 0;
            for (RowResult<StudentExcelRowDto> result : studentResults) {

                Row row = studentSheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(result.getData().getClassNumber());
                row.createCell(1).setCellValue(result.getData().getFullName());
                row.createCell(2).setCellValue(result.getData().getGrade());
                row.createCell(3).setCellValue(result.getMessage());
            }

            // Upload file
            return uploadFile.uploadExcel(workbook, fileName);

        } catch (Exception e) {
            throw new FileHandlingException("Cannot generate report file", e);
        }
    }

    private String generatePassword() {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }

    private String validateParent(ParentExcelRowDto dto, String email) {
        if (dto.getFullName() == null) {
            return "Fullname is required";
        }
        if (dto.getEmail() == null) {
            return "Email is required";
        }
        if (dto.getAddress() == null) {
            return "Address is required";
        }
        if (!EMAIL_REGEX.matcher(email).matches()) {
            return "Email invalid format";
        }
        if (dto.getPhoneNumber() == null) {
            return "Phone number is required";
        }
        String phoneNumber = dto.getPhoneNumber().replaceAll("\\s+","");
        if (!PHONE_REGEX.matcher(phoneNumber).matches()) {
            return "Phone number invalid";
        }
        return null;
    }

    private ParsedTwoSheets readTwoSheets(MultipartFile file) {
        try(Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            DataFormatter fmt = new DataFormatter();
            Sheet parentSheet = workbook.getSheetAt(0);
            Sheet studentSheet = workbook.getSheetAt(1);
            List<RowInput<ParentExcelRowDto>> parents = readParentSheet(parentSheet, fmt);
            List<RowInput<StudentExcelRowDto>> students = readStudentSheet(studentSheet, fmt);
            return new ParsedTwoSheets(parents, students);
        } catch (IOException e) {
            throw new FileHandlingException("Error handling file", e);
        }
    }

    private List<RowInput<StudentExcelRowDto>> readStudentSheet(Sheet studentSheet, DataFormatter fmt) {
        List<RowInput<StudentExcelRowDto>> rows = new ArrayList<>();
        for (int i = 1; i <= studentSheet.getLastRowNum(); i++) {
            Row row = studentSheet.getRow(i);
            if (isRowEmpty(row)) {
                continue;
            }
            StudentExcelRowDto dto = new StudentExcelRowDto();
            dto.setClassNumber(fmt.formatCellValue(row.getCell(0)));
            dto.setFullName(fmt.formatCellValue(row.getCell(1)));
            dto.setGrade(fmt.formatCellValue(row.getCell(2)));

            rows.add(new RowInput<>(i+1, dto));
        }
        return rows;
    }

    private List<RowInput<ParentExcelRowDto>> readParentSheet(Sheet parentSheet, DataFormatter fmt) {
        List<RowInput<ParentExcelRowDto>> rows = new ArrayList<>();
        for (int i = 1; i <= parentSheet.getLastRowNum(); i++) {
            Row row = parentSheet.getRow(i);
            if (isRowEmpty(row)) {
                continue;
            }
            ParentExcelRowDto dto = new ParentExcelRowDto();
            dto.setFullName(fmt.formatCellValue(row.getCell(0)));
            dto.setEmail(fmt.formatCellValue(row.getCell(1)));
            dto.setPhoneNumber(fmt.formatCellValue(row.getCell(2)));
            dto.setAddress(fmt.formatCellValue(row.getCell(3)));

            rows.add(new RowInput<>(i+1, dto));
        }
        return rows;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell == null) {
                continue;
            }
            if (cell.getCellType() == CellType.BLANK) {
                continue;
            }
            String value = new DataFormatter().formatCellValue(cell);
            if (value != null && !value.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static class ParsedTwoSheets {
        private final List<RowInput<ParentExcelRowDto>> parentRows;
        private final List<RowInput<StudentExcelRowDto>> studentRows;

        private ParsedTwoSheets(List<RowInput<ParentExcelRowDto>> parentRows, List<RowInput<StudentExcelRowDto>> studentRows) {
            this.parentRows = parentRows;
            this.studentRows = studentRows;
        }
    }
}

