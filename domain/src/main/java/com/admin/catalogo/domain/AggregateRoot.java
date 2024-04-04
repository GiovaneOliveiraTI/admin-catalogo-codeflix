package com.admin.catalogo.domain;


import com.admin.catalogo.domain.category.CategoryID;

import java.util.List;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(final ID id) {
        super(id);
    }
}
