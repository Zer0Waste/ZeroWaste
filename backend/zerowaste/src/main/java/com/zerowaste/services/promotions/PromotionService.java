package com.zerowaste.services.promotions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerowaste.dtos.promotions.AddPromotionDTO;
import com.zerowaste.dtos.promotions.PromotionDTO;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;

@Service
public class PromotionService {
    @Autowired
    private PromotionsRepository promotionsRepository;

    public void createPromotion(AddPromotionDTO dto) {
        Promotion p = new Promotion();
        p.setName(dto.name());
        p.setPercentage(dto.percentage());
        p.setStartsAt(dto.startsAt());
        p.setEndsAt(dto.endsAt());
        p.setCreatedAt(LocalDate.now());

        promotionsRepository.save(p);
    }

    public List<PromotionDTO> getAllPromotions() {
        List<Promotion> promos = promotionsRepository.findAllNotDeleted();
        List<PromotionDTO> promotionDTOs = new ArrayList<>();
        for (var p : promos) {
            promotionDTOs.add(new PromotionDTO(p.getName(), p.getPercentage(), p.getStartsAt(), p.getEndsAt()));
        }

        return promotionDTOs;
    }

    public Promotion getPromotionById(Long id) throws PromotionNotFoundException {
        Optional<Promotion> p = promotionsRepository.findById(id);
        if(!p.isPresent() || p.get().getDeletedAt() != null) 
            throw new PromotionNotFoundException("Promoção não encontrada!");
        
        return p.get();
    }

    
    public List<Promotion> getPromotionsByPercentage(Long percentage) throws PromotionNotFoundException {
        List<Promotion> promotions = promotionsRepository.findByPercentage(percentage);
        if (promotions.isEmpty()) {
            throw new PromotionNotFoundException("Nenhuma promoção encontrada para a porcentagem indicada.");
        }
        return promotions;
    }

    public Promotion getPromotionByProductId(Long productsIds) throws PromotionNotFoundException {
        Optional<Promotion> p = promotionsRepository.findByProducts_Id(productsIds);
        if(!p.isPresent() || p.get().getDeletedAt() != null) 
            throw new PromotionNotFoundException("Promoção não encontrada!");
        
        return p.get();
    }
}
