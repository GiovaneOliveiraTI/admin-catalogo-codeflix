package com.admin.catalogo.application.category.update;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_WhenCallsCreateCategory_shouldReturnCategoryId() {

        final var aCategory = Category.newCategory("film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

         when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

         final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(categoryGateway, times(1)).update(argThat(
                aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                            && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                            && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                            && Objects.equals(expectedId,aUpdatedCategory.getId())
                            && Objects.equals(aCategory.getCreatedAt(),aUpdatedCategory.getCreatedAt())
                            && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                            && Objects.isNull(aUpdatedCategory.getDeletedAt())
                ));
    }

    @Test
    public void givenAInvalidName_WhenCallsUpdateCategory_ThenShouldReturnDomainException() {

        final var aCategory = Category.newCategory("film", null, true);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;


        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        final var notification  = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(categoryGateway, times(0))
                .update(any());

    }

    @Test
    void givenAValidInactivateCommand_WhenCallsUpdateCategory_shouldReturnInactivatedCategoryId() {

        final var aCategory = Category.newCategory("film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());


        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(categoryGateway, times(1)).update(argThat(
                aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                && Objects.equals(expectedId,aUpdatedCategory.getId())
                                && Objects.equals(aCategory.getCreatedAt(),aUpdatedCategory.getCreatedAt())
                                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                && Objects.nonNull(aUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommand_WhenGatewayThrowsRandomException_ShouldReturnAException() {

        final var aCategory = Category.newCategory("film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));


        when(categoryGateway.update(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));


        final var notification  = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());


        Mockito.verify(categoryGateway, times(1)).update(argThat(
                aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                && Objects.equals(expectedId,aUpdatedCategory.getId())
                                && Objects.equals(aCategory.getCreatedAt(),aUpdatedCategory.getCreatedAt())
                                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                && Objects.isNull(aUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    void givenACommandWithInvalidID_WhenCallsUpdateCategory_shouldReturnNotFoundException() {

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = "123";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID 123 was not found";


        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var actualOutput = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));


        Assertions.assertEquals(expectedErrorCount, actualOutput.getErros().size());
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getErros().get(0).message());

        Mockito.verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId)));

        Mockito.verify(categoryGateway, times(0)).update(any());
    }

}
