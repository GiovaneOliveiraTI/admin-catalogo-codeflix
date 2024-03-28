package com.admin.catalogo.application.category.retrieve.list;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;

    void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCategories_ThenSholdReturnCategories() {
        final var expetedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerm = "";
        final var expectedSort= "createdAt";
        final var expetedDirection = "asc";


        final var aQuery =
                new CategorySearchQuery(expetedPage, expectedPerPage, expectedTerm, expectedSort, expetedDirection);

      final var categories =  List.of(
                Category.newCategory("Filmes", null, true),
                Category.newCategory("Series", null, true)
        );

      final var expectedPagination =
              new Pagination<>(expetedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput:: from);


        when(categoryGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult,  actualResult);
        Assertions.assertEquals(expetedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());

    }

    @Test
    public void givenAValidQuery_whenHasNoResults_ThenSholdReturnEmptyCategories() {

        final var categories =  List.<Category>of();


        final var expetedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerm = "";
        final var expectedSort= "createdAt";
        final var expetedDirection = "asc";


        final var aQuery =
                new CategorySearchQuery(expetedPage, expectedPerPage, expectedTerm, expectedSort, expetedDirection);


        final var expectedPagination =
                new Pagination<>(expetedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemCount = 0;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);


        when(categoryGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult,  actualResult);
        Assertions.assertEquals(expetedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());

    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_SholdReturnExeption() {
        final var expetedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerm = "";
        final var expectedSort= "createdAt";
        final var expetedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";


        final var aQuery =
                new CategorySearchQuery(expetedPage, expectedPerPage, expectedTerm, expectedSort, expetedDirection);



        when(categoryGateway.findAll(eq(aQuery))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    }






}
