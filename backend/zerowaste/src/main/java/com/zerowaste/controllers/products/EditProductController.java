package com.zerowaste.controllers.products;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.dtos.EditProductDTO;
import com.zerowaste.services.products.ProductService;
import com.zerowaste.services.products.exceptions.ProductDeletedException;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/products")
public class EditProductController {
    @Autowired
    private ProductService productService;

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, ?>> editProduct(@PathVariable Long id, @RequestBody @Valid EditProductDTO dto) {
        try {
            productService.edit(id, dto);
            return ResponseEntity.ok(Map.of("message", "Produto editado com sucesso!"));
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
