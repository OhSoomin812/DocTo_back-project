package com.ssginc8.docto.patient.repo;

import com.ssginc8.docto.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.Optional;

public interface PatientRepo extends JpaRepository<Patient, Long> {
	Optional<Patient> findByPatientIdAndDeletedAtIsNull(Long patientId);

	Page<Patient> findByDeletedAtIsNull(Pageable pageable);

	// 같은 환자의 예약 요청을 직렬화하기 위한 행 잠금 (SELECT ... FOR UPDATE)
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Patient p WHERE p.patientId = :patientId AND p.deletedAt IS NULL")
	Optional<Patient> findActiveByIdForUpdate(@Param("patientId") Long patientId);

	Optional<Patient> findByUser_UserIdAndUser_DeletedAtIsNullAndDeletedAtIsNull(Long userUserId);

}
