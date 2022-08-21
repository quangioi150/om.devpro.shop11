package com.devpro.repositories;

import com.devpro.entities.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    public List<ProductDetail> findByProductId(int productId);
}
