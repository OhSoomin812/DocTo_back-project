package com.ssginc8.docto.patient.provider;

import com.ssginc8.docto.global.error.exception.patientException.PatientNotFoundException;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.patient.repo.PatientRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PatientProvider {

	private final PatientRepo patientRepo;

	/**
	 * 예약 생성·변경 시 사용. 같은 환자에 대한 동시 요청이 중복 검사를 동시에 통과하지 못하도록 행 잠금으로 조회
	 */
	public Patient getActivePatientForUpdate(Long id) {
		return patientRepo.findActiveByIdForUpdate(id)
			.orElseThrow(PatientNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public Patient getActivePatient(Long id) {
		return patientRepo.findByPatientIdAndDeletedAtIsNull(id)
			.orElseThrow(PatientNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public Page<Patient> getAllActivePatients(Pageable pageable) {
		return patientRepo.findByDeletedAtIsNull(pageable);
	}

	@Transactional
	public Patient savePatient(Patient patient) {
		return patientRepo.save(patient);
	}

	@Transactional(readOnly = true)
	public Patient getPatientByUserId(Long userId) {
		return patientRepo.findByUser_UserIdAndUser_DeletedAtIsNullAndDeletedAtIsNull(userId)
			.orElseThrow(PatientNotFoundException::new);
	}
}
