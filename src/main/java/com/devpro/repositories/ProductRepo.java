package com.devpro.repositories;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;
import com.devpro.entities.Product;

import java.util.List;

@Repository // -> Bean
public interface ProductRepo extends JpaRepository<Product, Integer> {
    public List<Product> findBySeo(String seo);
}

