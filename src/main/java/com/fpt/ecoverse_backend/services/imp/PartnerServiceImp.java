package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.*;
import com.fpt.ecoverse_backend.dtos.requests.*;
import com.fpt.ecoverse_backend.dtos.responses.*;
import com.fpt.ecoverse_backend.entities.Parent;
import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.entities.Student;
import com.fpt.ecoverse_backend.entities.User;
import com.fpt.ecoverse_backend.enums.UserType;
import com.fpt.ecoverse_backend.exceptions.BadRequestException;
import com.fpt.ecoverse_backend.exceptions.FileHandlingException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.ParentMapper;
import com.fpt.ecoverse_backend.mappers.PartnerMapper;
import com.fpt.ecoverse_backend.mappers.StudentMapper;
import com.fpt.ecoverse_backend.mappers.UserMapper;
import com.fpt.ecoverse_backend.repositories.*;
import com.fpt.ecoverse_backend.services.PartnerService;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class PartnerServiceImp implements PartnerService {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9]+([._+-]?[A-Za-z0-9]+)*@[A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)+$");
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
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ParentMapper parentMapper;
    private final StudentMapper studentMapper;
    private final UserMapper userMapper;

    public PartnerServiceImp(PartnerRepository partnerRepository, PartnerMapper partnerMapper, PasswordEncoder passwordEncoder, UploadFile uploadFile, ParentRepository parentRepository, StudentRepository studentRepository, UserRepository userRepository, ApplicationEventPublisher eventPublisher, ParentMapper parentMapper, StudentMapper studentMapper, UserMapper userMapper) {
        this.partnerRepository = partnerRepository;
        this.partnerMapper = partnerMapper;
        this.passwordEncoder = passwordEncoder;
        this.uploadFile = uploadFile;
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.parentMapper = parentMapper;
        this.studentMapper = studentMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public PartnerResponseDto createPartner(PartnerRegisterRequestDto request) {
        Optional<User> existingUser = userRepository.findByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber());
        if (existingUser.isPresent()) {
            if (existingUser.get().getEmail().equalsIgnoreCase(request.getEmail())) {
                throw new BadRequestException("Email already exist");
            } else {
                throw new BadRequestException("Phone number already exist");
            }
        }
        User user = userMapper.toUser(request, null, uploadFile);
        Partner partner = partnerMapper.toPartner(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserType.PARTNERSHIP);
        userRepository.save(user);
        partner.setUser(user);
        partnerRepository.save(partner);
        return partnerMapper.toPartnerResponse(partner, user);
    }

    @Override
    public PartnerResponseDto getDetailPartner(String partnerId) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        Optional<User> user = userRepository.findById(partner.get().getUser().getId());
        if (user.isEmpty()) {
            throw new NotFoundException("Not found user for partner");
        }
        PartnerResponseDto partnerResponseDto = partnerMapper.toPartnerResponse(partner.get(), user.get());
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
        Optional<User> user = userRepository.findById(partner.get().getUser().getId());
        if (user.isEmpty()) {
            throw new NotFoundException("Not found user for partner");
        }
        partnerMapper.toPartner(partner.get(), request);
        if (request.getAvatar() != null) {
            user.get().setAvatarUrl(uploadFile.imageToUrl(request.getAvatar()));
        }
        partnerRepository.save(partner.get());
        PartnerResponseDto partnerResponseDto = partnerMapper.toPartnerResponse(partner.get(), user.get());
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
    public BulkCreateReportResponseDto bulkCreate(byte[] bytes, String partnerId) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        ParsedTwoSheets parsed = readTwoSheets(bytes);
        List<RowResult<ParentExcelRowDto>> parentResults = new ArrayList<>();
        List<RowResult<StudentExcelRowDto>> studentResults = new ArrayList<>();
        List<ParentCredentialMail> createdParentMails = new ArrayList<>();
        Set<String> parentEmailInFile = new HashSet<>();
        for (RowInput<ParentExcelRowDto> input : parsed.parentRows) {
            String email = input.getDto().getEmail().toLowerCase();
            parentEmailInFile.add(email);
        }
        Set<String> existingEmails = userRepository.findExistingEmail(parentEmailInFile);
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
            User user = new User();
            Parent parent = new Parent();
            user.setFullName(dto.getFullName().trim());
            user.setEmail(email);
            user.setPhoneNumber(dto.getPhoneNumber().replaceAll("\\s+", ""));
            user.setAddress(dto.getAddress().trim());
            user.setPassword(rawPassword);
            parent.setPartner(partner.get());
            user.setRole(UserType.PARENT);
            userRepository.save(user);
            parent.setUser(user);
            parentRepository.save(parent);

            createdParentMails.add(new ParentCredentialMail(user.getEmail(), user.getFullName(), password));
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
            User user = new User();
            Student student = new Student();
            user.setFullName(dto.getFullName().trim());
            student.setGrade(dto.getGrade());
            student.setStudentCode(studentCode);
            student.setPartner(partner.get());
            user.setRole(UserType.STUDENT);
            userRepository.save(user);
            student.setUser(user);
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
        Optional<User> user = userRepository.findById(student.get().getUser().getId());
        if (user.isEmpty()) {
            throw new NotFoundException("Not found user for student");
        }
        StatisticStudent statistic = new StatisticStudent();
        StudentResponseDto studentResponseDto = studentMapper.toStudentResponse(student.get());
        if (student.get().getParent() != null) {
            Optional<Parent> parent = parentRepository.findById(student.get().getParent().getId());
            Optional<User> parentUser = userRepository.findById(parent.get().getUser().getId());
            studentResponseDto.setParentId(parent.get().getId());
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
        Optional<User> user = userRepository.findById(student.get().getUser().getId());
        if (status.equalsIgnoreCase("active")) {
            user.get().setActive(true);
            userRepository.save(user.get());
        } else {
            user.get().setActive(false);
            userRepository.save(user.get());
        }
        return studentMapper.toStudentResponse(student.get());
    }

    @Override
    public UserListResponseDto<?> getListUser(String partnerId, PageFilterRequestDto pageFilterRequestDto) {
        Pageable pageable = PageRequest.of(
                pageFilterRequestDto.getPageNo()-1,
                pageFilterRequestDto.getPageSize());
        if (pageFilterRequestDto.getType().equalsIgnoreCase("student")) {
            Page<Object[]> students = studentRepository.searchStudents(
                    partnerId, pageFilterRequestDto.getSearching(), pageFilterRequestDto.getGrade(), pageable);
            List<StudentResponseDto> list = students.getContent().stream().map(row -> {
                Student student = (Student) row[0];
                User user = (User) row[1];
                StudentResponseDto response = studentMapper.toStudentResponse(student);
                 if (student.getParent() != null) {
                    Optional<User> parentUser = userRepository.findById(student.getParent().getUser().getId());
                    response.setParentId(parentUser.get().getId());
                    response.setParentName(parentUser.get().getFullName());
                    response.setPartnerId(student.getPartner().getId());
                }
                return response;
            }).toList();
            return new UserListResponseDto<>(
                    list,
                    pageFilterRequestDto.getPageNo(),
                    pageFilterRequestDto.getPageSize(),
                    students.getTotalElements(),
                    students.getTotalPages());
        } else if (pageFilterRequestDto.getType().equalsIgnoreCase("parent")) {
            Page<Object[]> parents = parentRepository.searchParents(
                    partnerId, pageFilterRequestDto.getSearching(), pageFilterRequestDto.getHasChildren(), pageable);
            List<ParentResponseDto> list = parents.getContent().stream().map(row -> {
                Parent parent = (Parent) row[0];
                List<Student> studentOpt = studentRepository.findByParentId(parent.getId());
                User user = (User) row[1];
                ParentResponseDto parentResponse = parentMapper.toParentResponse(parent);
                parentResponse.setNumberChildren(studentOpt.size());
                return parentResponse;
            }).toList();
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
    @Transactional
    public List<StudentResponseDto> createStudents(String partnerId, List<StudentRequestDto> studentRequestDtos) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        List<Student> students = studentRequestDtos.stream().map(dto -> {
            User user = userMapper.toUser(dto, null);
            user.setRole(UserType.STUDENT);
            User savedUser = userRepository.save(user);
            Student student = studentMapper.toStudent(dto, null);
            student.setUser(savedUser);
            student.setPartner(partner.get());
            String studentCode = generateStudentCode(
                    dto.getFullName(),
                    dto.getGrade(),
                    dto.getClassNumber()
            );
            student.setStudentCode(studentCode);
            return studentRepository.save(student);

        }).toList();
        List<StudentResponseDto> response = new ArrayList<>();
        for (Student student : students) {
            response.add(studentMapper.toStudentResponse(student));
        }
        return response;
    }

    @Override
    public PartnerResponseDto deletePartner(String partnerId) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        partnerRepository.delete(partner.get());
        userRepository.delete(partner.get().getUser());
        return partnerMapper.toPartnerResponse(partner.get(), partner.get().getUser());
    }

    @Override
    @Transactional
    public List<ParentResponseDto> createParents(String partnerId, List<ParentRequestDto> request) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        List<ParentCredentialMail> createdParentMails = new ArrayList<>();
        List<Parent> parents = request.stream().map(dto -> {
            Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());
            if (userOptional.isPresent()) {
                throw new BadRequestException("Email already exist: " + dto.getEmail());
            }
            Optional<User> phoneOptional = userRepository.findByPhoneNumber(dto.getPhoneNumber());
            if (phoneOptional.isPresent()) {
                throw new BadRequestException("Phone number already exist: " + dto.getPhoneNumber());
            }
            User user = userMapper.toUser(dto, null);
            user.setRole(UserType.PARENT);
            String password = generatePassword();
            user.setPassword(passwordEncoder.encode(password));
            User savedUser = userRepository.save(user);
            Parent parent = new Parent();
            parent.setUser(savedUser);
            parent.setPartner(partner.get());
            ParentCredentialMail mail = new ParentCredentialMail(savedUser.getEmail(), savedUser.getFullName(), password);
            createdParentMails.add(mail);
            return parentRepository.save(parent);
        }).toList();
        eventPublisher.publishEvent(new ParentsCreatedEvent(createdParentMails));
        List<ParentResponseDto> response = new ArrayList<>();
        for (Parent parent : parents) {
            response.add(parentMapper.toParentResponse(parent));
        }
        return response;
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

    private ParsedTwoSheets readTwoSheets(byte[] bytes) {
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            Workbook workbook = WorkbookFactory.create(inputStream);
            if (workbook.getNumberOfSheets() < 2) {
                throw new FileHandlingException("Excel file must contain at least 2 sheets");
            }
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

