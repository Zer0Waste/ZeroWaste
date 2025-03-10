package com.zerowaste.dtos.promotions;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record EditPromotionDTO(
        
        @NotEmpty(message = "O campo \"name\" é obrigatório")
        @Size(max = 100, message = "O campo \"name\" deve ter no máximo 100 caracteres")
        String name,

        @Min(0)
        @Max(1)
        Double percentage,

        @FutureOrPresent
        LocalDate startsAt,

        @Future
        LocalDate endsAt,

        Set<Long> productIds
        
) {}
