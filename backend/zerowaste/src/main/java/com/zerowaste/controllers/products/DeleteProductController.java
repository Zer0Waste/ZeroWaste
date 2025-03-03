package com.zerowaste.controllers.products;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.services.products.ProductService;
import com.zerowaste.services.products.exceptions.ProductDeletedException;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

@RestController
@RequestMapping("/products")
public class DeleteProductController {
    @Autowired
    private ProductService productService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, ?>> deleteProduct(@PathVariable Long id) {
        try {
            productService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Produto deletado com sucesso!"));
        } 
        catch (ProductNotFoundException err) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", err.getMessage()));
        }
        catch (ProductDeletedException err) {
            return ResponseEntity
                .status(HttpStatus.GONE)
                .body(Map.of("error", err.getMessage()));
        }
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", err.getMessage()));
        }
    }
}
