package com.admin.catalogo.infrastructure.api.controllers;

import com.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.validation.handler.Notification;
import com.admin.catalogo.infrastructure.api.CategoryAPI;
import com.admin.catalogo.infrastructure.category.models.CreatedCategoryApiInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;


@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;


    public CategoryController(final CreateCategoryUseCase createCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
    }
    @Override
    public ResponseEntity<?> createCategory(final CreatedCategoryApiInput input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() :true
        );
        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);


        return this.createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String dir) {
        return null;
    }
}
