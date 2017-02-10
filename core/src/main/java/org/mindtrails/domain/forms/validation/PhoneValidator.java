package org.mindtrails.domain.forms.validation;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, HasPhone> {

    private Phone phone;

    @Override
    public void initialize(Phone phone) {
        this.phone = phone;
    }

    @Override
    public boolean isValid(HasPhone hasPhone, ConstraintValidatorContext ctx) {

        // Don't validate null phones.
        if (hasPhone.getPhone() == null) { return true; }

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber usPhone = phoneUtil.parse(hasPhone.getPhone(), hasPhone.getPhoneLocale());
            return phoneUtil.isValidNumber(usPhone);
        } catch (NumberParseException e) {
            return false;
        }
    }
}