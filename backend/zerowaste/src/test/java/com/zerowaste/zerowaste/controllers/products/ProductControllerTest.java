package com.zerowaste.zerowaste.controllers.products;


import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerowaste.controllers.products.ProductController;
import com.zerowaste.dtos.products.CreateProductDTO;
import com.zerowaste.dtos.products.EditProductDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.services.products.CreateProductService;
import com.zerowaste.services.products.DeleteProductService;
import com.zerowaste.services.products.EditProductService;
import com.zerowaste.services.products.GetProductIdService;
import com.zerowaste.services.products.GetProductService;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateProductService createProductService;

    @Mock
    private DeleteProductService deleteProductService;

    @Mock
    private EditProductService editProductService;

    @Mock
    private GetProductIdService getProductIdService;

    @Mock 
    private GetProductService getProductService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void TestCreateProduct_Success() throws Exception {
        // Dado um DTO de produto
        CreateProductDTO productDTO = new CreateProductDTO(
                "Teste", 
                "Descrição do produto", 
                "Marca de Teste", 
                ProductCategory.BAKERY.getCategory(), 
                100.0, 
                100, 
                LocalDate.now().plusDays(1)
        );
        // Quando o serviço CreateProductService é chamado, ele não faz nada (simulamos sucesso)
        doNothing().when(createProductService).execute(any(CreateProductDTO.class));

        // Então, executamos a requisição POST para criar o produto
        mockMvc.perform(post("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{"
        + "\"name\":\"Teste\","
        + "\"description\":\"Descrição do produto\","
        + "\"brand\":\"Marca de Teste\","
        + "\"category\":\"" + ProductCategory.BAKERY.getCategory() + "\","
        + "\"unitPrice\":100.0,"
        + "\"stock\":100,"
        + "\"expiresAt\": [" + LocalDate.now().getYear() + ", " 
        + LocalDate.now().getMonthValue() + ", " 
        + (LocalDate.now().getDayOfMonth() + 1) + "]"
        + "}"))
       
            .andExpect(status().isCreated()) // Espera-se um código 201
            .andExpect(jsonPath("$.message").value("Product created successfully")); // A mensagem retornada deve ser "Product created successfully"
 
        // Verifica que o serviço foi chamado uma vez com o DTO
        verify(createProductService, times(1)).execute(any(CreateProductDTO.class));
    }

    @Test
    void TestEditProduct_Success() throws Exception {
        // Dado um DTO de produto editado
        EditProductDTO editProductDTO = new EditProductDTO(
                "Produto Editado", 
                "Descrição Editada", 
                "Marca Editada", 
                ProductCategory.BAKERY.getCategory(), 
                120.0, 
                110.0, // Preço promocional
                150, 
                LocalDate.now().plusDays(1),
                new HashSet<>(Arrays.asList(1L, 2L, 3L))
        );

        // Quando o serviço EditProductService é chamado, ele não faz nada (simulamos sucesso)
        doNothing().when(editProductService).execute(anyLong(), any(EditProductDTO.class));

        // Então, executamos a requisição PUT para editar o produto
        mockMvc.perform(put("/products/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{"
        + "\"name\":\"Produto Editado\","
        + "\"description\":\"Descrição Editada\","
        + "\"brand\":\"Marca Editada\","
        + "\"category\":\"" + ProductCategory.BAKERY.getCategory() + "\","
        + "\"unitPrice\":120.0,"
        + "\"promotionPrice\":110.0,"
        + "\"stock\":150,"
        + "\"expiresAt\": [" + LocalDate.now().getYear() + ", " + LocalDate.now().getMonthValue() + ", " + (LocalDate.now().getDayOfMonth() + 1) + "],"
        + "\"promotionsIds\": [1,2,3]"
        + "}"))
                .andExpect(status().isOk()) // Espera-se um código 200
                .andExpect(jsonPath("$.message").value("Produto editado com sucesso!")); // A mensagem retornada deve ser "Produto editado com sucesso!"
        
        // Verifica que o serviço foi chamado uma vez com o ID do produto e o DTO
        verify(editProductService, times(1)).execute(eq(1L), any(EditProductDTO.class));
    }

    @Test
    void TestDeleteProduct_Success() throws Exception {
        // Quando o serviço DeleteProductService é chamado, ele não faz nada (simulamos sucesso)
        doNothing().when(deleteProductService).execute(anyLong());

        // Então, executamos a requisição DELETE para deletar o produto
        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isOk()) // Espera-se um código 200
                .andExpect(jsonPath("$.message").value("Produto deletado com sucesso!")); // A mensagem retornada deve ser "Produto deletado com sucesso!"
        
        // Verifica que o serviço foi chamado uma vez com o ID do produto
        verify(deleteProductService, times(1)).execute((1L));
    }

    @Test
    void TestGetProductById_Success() throws Exception {
        // Dado um produto existente
        Product product = new Product(1L, "Produto Teste", "Descrição do Produto", "Marca Teste", ProductCategory.BAKERY, 100.0, null, 100, LocalDate.now().plusDays(1), null, null, null);

        // Quando o serviço GetProductIdService é chamado, ele retorna o produto
        when(getProductIdService.execute(anyLong())).thenReturn(product);

        // Então, executamos a requisição GET para buscar o produto
        mockMvc.perform(get("/products/{id}", 1L))
                .andExpect(status().isOk()) // Espera-se um código 200
                .andExpect(jsonPath("$.product.name").value("Produto Teste")) // Verifica o nome do produto
                .andExpect(jsonPath("$.product.description").value("Descrição do Produto")); // Verifica a descrição do produto
        
        // Verifica que o serviço foi chamado uma vez com o ID do produto
        verify(getProductIdService, times(1)).execute((1L));
    }

    @Test
    void TestGetAllProducts_Success() throws Exception {
        // Dado uma lista de produtos
        List<Product> products = List.of(
            new Product(1L, "Produto Teste", "Descrição do Produto", "Marca Teste", ProductCategory.BAKERY, 100.0, null, 100, LocalDate.now().plusDays(1), null, null, null),
            new Product(2L, "Produto Teste 2", "Descrição do Produto 2", "Marca Teste 2", ProductCategory.HYGIENE, 50.0, null, 200, LocalDate.now().plusDays(2), null, null, null)
        );

        // Quando o serviço GetProductService é chamado, ele retorna a lista de produtos
        when(getProductService.execute(any())).thenReturn(products);

        // Então, executamos a requisição GET para buscar todos os produtos
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk()) // Espera-se um código 200
                .andExpect(jsonPath("$.products.size()").value(2)) // Espera-se que haja 2 produtos
                .andExpect(jsonPath("$.products[0].name").value("Produto Teste")) // Verifica o nome do primeiro produto
                .andExpect(jsonPath("$.products[1].name").value("Produto Teste 2")); // Verifica o nome do segundo produto
        
        // Verifica que o serviço foi chamado uma vez
        verify(getProductService, times(1)).execute(any());
    }

    @Test
    void TestGetProductById_NotFound() throws Exception {
        // Quando o serviço GetProductIdService é chamado, ele lança uma exceção ProductNotFoundException
        when(getProductIdService.execute(anyLong())).thenThrow(new ProductNotFoundException("Produto não encontrado"));

        // Então, executamos a requisição GET para buscar o produto
        mockMvc.perform(get("/products/{id}", 999L))
                .andExpect(status().isNotFound()) // Espera-se um código 404
                .andExpect(jsonPath("$.error").value("Produto não encontrado")); // Verifica a mensagem de erro
        
        // Verifica que o serviço foi chamado uma vez com o ID do produto
        verify(getProductIdService, times(1)).execute((999L));
    }
}
