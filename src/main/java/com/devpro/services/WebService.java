package com.devpro.services;

import com.devpro.entities.Collection;  
import com.devpro.repositories.CategoryRepo;
import com.devpro.repositories.ProductRepo;
import com.devpro.entities.Category;
import com.devpro.entities.Product;
import com.devpro.entities.Tag;
import com.devpro.repositories.CollectionRepository;
import com.devpro.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebService {
	
	@Autowired
	ProductRepo productRepository;
	
	@Autowired
	CategoryRepo categoryRepository;
	
	@Autowired
	TagRepository tagRepository;
	
	@Autowired
	CollectionRepository collectionRepository;
	
	public List<Product> getAllProduct() {
		return productRepository.findAll();
	}
	
	public List<Product> getAllProduct(PageRequest page) {
		return productRepository.findAll(page).getContent();
	}
	
	public List<Category> getAllCategory() {
		return categoryRepository.findAll();
	}
	
	public List<Collection> getAllCollection() {
		return collectionRepository.findAll();
	}
	
	public List<Tag> getAllTag() {
		return tagRepository.findAll();
	}
}
