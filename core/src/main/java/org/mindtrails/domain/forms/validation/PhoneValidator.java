package org.mindtrails.domain.forms.validation;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, HasPhone> {

    private Phone phone;
    private static PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    @Override
    public void initialize(Phone phone) {
        this.phone = phone;
    }

    @Override
    public boolean isValid(HasPhone hasPhone, ConstraintValidatorContext ctx) {

        // Don't validate null phones.
        if (hasPhone.getPhone() == null || hasPhone.getPhone() == "") { return true; }

        try {
            Phonenumber.PhoneNumber phone = phoneUtil.parse(hasPhone.getPhone(), hasPhone.getPhoneLocale());
            return phoneUtil.isValidNumber(phone);
        } catch (NumberParseException e) {
            return false;
        }
    }

}