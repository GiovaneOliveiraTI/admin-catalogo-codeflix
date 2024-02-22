package com.admin.catalogo.domain;

import com.admin.catalogo.domain.validation.ValidationHandler;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;
@Getter
@EqualsAndHashCode(callSuper=false)
public abstract class Entity<ID extends Identifier> {
    protected final ID id;

    protected Entity(final ID id) {
        Objects.requireNonNull(id, "'id' should not be null");
        this.id = id;
    }

    public abstract void validate(ValidationHandler handler);


}
