package com.fpt.ecoverse_backend.repositories;

import com.fpt.ecoverse_backend.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    @Query("select count(st.id) from Student st where st.partner.id = :partnerId")
    long countStudentByPartnerId(@Param("partnerId") String partnerId);

    @Query(
            "select s from Student s " +
                    "where s.partner.id = :partnerId " +
                    "and (:searching is null or lower(s.fullName) like lower(concat('%',:searching,'%'))) " +
                    "and (:grade is null or s.grade = :grade)"
    )
    Page<Student> searchStudents(@Param("partnerId") String partnerId, @Param("searching") String searching, @Param("grade") String grade, Pageable pageable);
}
