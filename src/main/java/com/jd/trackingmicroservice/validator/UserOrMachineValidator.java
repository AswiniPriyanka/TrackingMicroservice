package com.jd.trackingmicroservice.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.ObjectUtils;

import com.jd.trackingmicroservice.dto.SessionDTO;

public class UserOrMachineValidator implements ConstraintValidator<RequestAnnotation, SessionDTO> {

	@Override
	public void initialize(RequestAnnotation constraintAnnotation) {

	}

	@Override
	public boolean isValid(SessionDTO value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		if (ObjectUtils.isEmpty(value.getUserId()) && ObjectUtils.isEmpty(value.getMachineId()))
			return false;
		if (!ObjectUtils.isEmpty(value.getUserId()) && !ObjectUtils.isEmpty(value.getMachineId()))
			return false;

		return true;
	}
}
