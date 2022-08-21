package com.devpro.repositories;

import com.devpro.entities.Product;
import com.devpro.entities.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ProductImages, Integer> {

    public List<ProductImages> findByProduct(Product product);
}