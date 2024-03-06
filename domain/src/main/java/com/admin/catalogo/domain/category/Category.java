package com.admin.catalogo.domain.category;

import com.admin.catalogo.domain.AggregateRoot;
import com.admin.catalogo.domain.validation.ValidationHandler;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant aUpdatedDate,
            final Instant aDeleteDate
    ) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdatedDate;
        this.deletedAt = aDeleteDate;
    }

    public static Category newCategory(final String aName, final String aDescription, final boolean isActive) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Category(id, aName, aDescription, isActive, now, now, deletedAt);

    }

    @Override
    public void validate(ValidationHandler handler) {
        new CateroryValidator(this,handler).validate();
    }
    public Category activate(){
        this.updatedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }
    public Category deactivate(){
        if(getDeletedAt() == null) {
            this.deletedAt= Instant.now();
        }
        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category update(
            final String aName,
            final String aDescription,
            final boolean isActive
    ){
        if(isActive) {
            activate();
        } else {
            deactivate();
        }
        this.name = aName;
        this.description = aDescription;
        this.updatedAt = Instant.now();
        return this;

    }


    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
