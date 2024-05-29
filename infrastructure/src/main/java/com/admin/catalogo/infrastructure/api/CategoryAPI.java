package com.admin.catalogo.infrastructure.api;


import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.category.models.CreatedCategoryApiInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "categories")
@Tag(name = "Categories")
public interface CategoryAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Created a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created sucessfully"),
            @ApiResponse(responseCode = "422", description = "unprocessable error"),
            @ApiResponse(responseCode = "500", description = "An internal server error"),

    })
    ResponseEntity<?> createCategory(@RequestBody CreatedCategoryApiInput input);

    @GetMapping
    @Operation(summary = "List all categories paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed sucessfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was receive"),
            @ApiResponse(responseCode = "500", description = "An internal server error"),
    })
    Pagination<?> listCategories(
            @RequestParam(name = "search", required = false,defaultValue = "") final String search,
            @RequestParam(name = "page", required = false,defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false,defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false,defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false,defaultValue = "asc") final String dir
    );
}
