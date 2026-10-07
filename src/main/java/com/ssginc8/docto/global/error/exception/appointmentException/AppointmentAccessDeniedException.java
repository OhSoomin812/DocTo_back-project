package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class AppointmentAccessDeniedException extends BusinessBaseException {
	public AppointmentAccessDeniedException(ErrorCode errorCode) { super(errorCode); }

	public AppointmentAccessDeniedException() { super(ErrorCode.FORBIDDEN_REQUEST); }
}
