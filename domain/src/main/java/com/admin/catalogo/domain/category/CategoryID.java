package com.admin.catalogo.domain.category;

import com.admin.catalogo.domain.Identifier;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = false)
public class CategoryID extends Identifier {

    private final String value;

    private CategoryID( final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static CategoryID unique() {
        return CategoryID.from(UUID.randomUUID());
    }
    public static CategoryID from(final String anID) {
        return new CategoryID(anID);
    }
    public static CategoryID from(final UUID anId) {
        return new CategoryID(anId.toString().toLowerCase());
    }



}
