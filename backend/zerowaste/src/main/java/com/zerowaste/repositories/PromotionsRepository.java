package com.zerowaste.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import com.zerowaste.models.promotion.Promotion;

@Repository
public interface PromotionsRepository extends JpaRepository<Promotion, Long> {
    @NativeQuery("SELECT * FROM promotions WHERE deleted_at IS NULL")
    List<Promotion> findAllNotDeleted();
    Optional<Promotion> findById(Long id);
    List<Promotion> findByPercentage(Long percentage);
    Optional<Promotion> findByProducts_Id(Long productsIds);
}
