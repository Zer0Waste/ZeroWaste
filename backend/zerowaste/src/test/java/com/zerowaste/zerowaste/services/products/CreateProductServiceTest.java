package com.zerowaste.zerowaste.services.products;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.zerowaste.dtos.products.CreateProductDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.CreateProductService;

@ExtendWith(MockitoExtension.class)
public class CreateProductServiceTest {
    @Autowired
    @InjectMocks
    private CreateProductService sut;

    @Mock
    private ProductsRepository productsRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to create a product")
    public void itShouldCreateProduct() {
        // Arrange
        var dto = new CreateProductDTO(
            "Product Name",
            "Product Description",
            "Product Brand",
            ProductCategory.BAKERY.getCategory(),
            10.0,
            10,
            LocalDate.now().plusDays(1), 
            null
        );

        var product = new Product();

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setBrand(dto.brand());
        product.setCategory(ProductCategory.valueOf(dto.category()));
        product.setUnitPrice(dto.unitPrice());
        product.setStock(dto.stock());
        product.setExpiresAt(dto.expiresAt());
        
        // Act & Assert
        assertDoesNotThrow(() -> sut.execute(dto));
        verify(this.productsRepository, times(1)).save(product);
    }
}
