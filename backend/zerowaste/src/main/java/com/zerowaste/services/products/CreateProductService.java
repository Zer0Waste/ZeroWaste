package com.zerowaste.services.products;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.products.CreateProductDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.repositories.PromotionsRepository;

@Service
public class CreateProductService {
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private PromotionsRepository promotionsRepository;
    
    public void execute(CreateProductDTO dto) {
        var product = new Product();

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setBrand(dto.brand());
        product.setCategory(ProductCategory.valueOf(dto.category()));
        product.setUnitPrice(dto.unitPrice());
        product.setStock(dto.stock());
        product.setExpiresAt(dto.expiresAt());

        Set<Promotion> promotions = new HashSet<>();
        if (dto.promotionsIds() != null && !dto.promotionsIds().isEmpty()) {
            promotions = new HashSet<>(promotionsRepository.findAllById(dto.promotionsIds()));

            if (promotions.isEmpty()) {
                throw new IllegalArgumentException("Nenhuma promoção válida foi encontrada!");
            }
        }

        product.setPromotions(promotions);
        

        productsRepository.save(product);
    }
}
