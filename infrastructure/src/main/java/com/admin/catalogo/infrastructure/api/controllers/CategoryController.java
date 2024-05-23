package com.admin.catalogo.infrastructure.api.controllers;

import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.api.CategoryAPI;
import org.springframework.http.ResponseEntity;

public class CategoryController implements CategoryAPI {
    @Override
    public ResponseEntity<?> createCategory() {
        return null;
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String dir) {
        return null;
    }
}
