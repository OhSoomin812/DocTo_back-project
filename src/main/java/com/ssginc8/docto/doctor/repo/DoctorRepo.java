package com.ssginc8.docto.doctor.repo;

import java.util.List;
import java.util.Optional;

import javax.print.Doc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.user.entity.User;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface DoctorRepo extends JpaRepository<Doctor, Long> {

	List<Doctor> findByHospitalHospitalId(Long hospitalId);

	List<Doctor> findByHospital(Hospital hospital);
	void deleteAll(Iterable<? extends Doctor> doctors);
	Page<Doctor> findByDeletedAtIsNull(Pageable pageable);
	boolean existsByUser(User user);

	Optional<Doctor> findByUserUserId(Long userId);

	// 같은 의사의 예약 확정을 직렬화해 슬롯 정원 검사가 동시에 통과하지 못하도록 행 잠금
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT d FROM Doctor d WHERE d.doctorId = :doctorId")
	Optional<Doctor> findByIdForUpdate(@Param("doctorId") Long doctorId);
}
