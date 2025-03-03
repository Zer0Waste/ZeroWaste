package com.zerowaste.controllers.products;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.services.products.ProductService;
import com.zerowaste.services.products.exceptions.ProductDeletedException;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/products")
public class GetProductIdController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, ?>> getProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(Map.of("product", productService.getById(id)));
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
