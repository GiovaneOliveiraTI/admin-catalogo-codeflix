package com.admin.catalogo.domain.exceptions;

import com.admin.catalogo.domain.validation.Error;
import lombok.Getter;

import java.util.List;
@Getter
public class DomainException extends NoStackTraceException {

    private final List<Error> erros;
    private DomainException(final String aMessage, final List<Error> anErrors) {
        super(aMessage);
        this.erros = anErrors;
    }
    public static DomainException with(final Error anErrors) {
        return new DomainException(anErrors.message(), List.of(anErrors));
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("", anErrors);
    }



}
