package com.zerowaste.controllers.promotions;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.services.promotions.PromotionService;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;

@RestController
@RequestMapping("/promotions")

public class GetPromotionProductIdController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping("/{productsIds}")
    public ResponseEntity<Map<String, ?>> getPromotionByProductId(@RequestParam Long productsIds) {
        try {
            return ResponseEntity.ok(Map.of("promotion", promotionService.getPromotionByProductId(productsIds)));
        }
        catch(PromotionNotFoundException err) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", err.getMessage()));
        }
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", err.getMessage()));
        }
    }
    
}
