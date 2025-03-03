package com.zerowaste.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import com.zerowaste.models.product.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {

    @NativeQuery("SELECT * FROM products WHERE deleted_at IS NULL")
    List <Product> findAllNotDeleted();
    
    Optional<Product> findById(Long id);
    List<Product> findAllByNameLike(String name);
}
