package com.admin.catalogo.domain.category;

import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.ValidationHandler;
import com.admin.catalogo.domain.validation.Validator;

public class CateroryValidator extends Validator {

    private final Category category;
    protected CateroryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }



    @Override
    public void validate() {
        this.checkNameContraints();
    }

    private void checkNameContraints() {
        final var name = this.category.getName();
        if(name == null) {
            this.validationHandler().append(new Error("'Name' should not be null"));
            return;
        }

        if(name.isBlank()) {
            this.validationHandler().append(new Error("'Name' should not be empty"));
            return;
        }
        final int length = name.trim().length();
        if(length > 255 || length <3) {
            this.validationHandler().append(new Error("'Name' must be between 3 and 255 characters"));
        }
    }
}
