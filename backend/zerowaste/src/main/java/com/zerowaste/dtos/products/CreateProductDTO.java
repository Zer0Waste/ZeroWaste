package com.zerowaste.dtos.products;

import java.time.LocalDate;
import java.util.Set;

import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.utils.validation.ValidEnum.ValidEnum;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductDTO(
    @NotEmpty(message = "O campo \"name\" é obrigatório")
    @Size(max = 100, message = "O campo \"name\" deve ter entre 3 e 100 caracteres")
    String name,

    @NotEmpty(message = "O campo \"name\" é obrigatório")
    @Size(max = 255, message = "O campo \"description\" deve ter entre 3 e 100 caracteres")
    String description,

    @NotEmpty(message = "O campo \"brand\" é obrigatório")
    @Size(max = 100, message = "O campo \"brand\" deve ter entre 3 e 100 caracteres")
    String brand,

    @NotEmpty(message = "O campo \"category\" é obrigatório")
    @ValidEnum(targetClassType = ProductCategory.class, message = "O campo \"category\" deve ser um dos valores: \"DAIRY\", \"FRUIT\", \"HYGIENE\", \"CLEANING\", \"DRINK\", \"MEAT\", \"BAKERY\"")
    String category,

    @NotNull(message = "O campo \"unitPrice\" é obrigatório")
    @DecimalMin("0")
    Double unitPrice,

    @NotNull(message = "O campo \"stock\" é obrigatório")
    @Min(0)
    Integer stock,

    @NotNull(message = "O campo \"expiresAt\" é obrigatório")
    @Future(message = "O campo \"expiresAt\" deve ser uma data futura")
    LocalDate expiresAt,

    Set<Long> promotionsIds
) {}
