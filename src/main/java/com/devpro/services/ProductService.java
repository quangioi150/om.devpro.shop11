package com.devpro.services;

import java.io.File; 
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.devpro.WebConstants;
import com.devpro.model.ProductFilterModel;
import com.devpro.repositories.ImageRepository;
import com.devpro.repositories.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devpro.entities.Product;
import com.devpro.entities.ProductImages;
import com.devpro.model.ProductSearch;
import com.devpro.repositories.ProductRepo;
import com.github.slugify.Slugify;



@Service // -> Bean
public class ProductService {
	
//	@PersistenceContext protected EntityManager entityManager;
	@Autowired
	private ProductRepo productRepo;

	@Autowired
	EntityManager entityManager;

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	ProductDetailRepository detailRepository;

	public ProductService() {
	}
//	private boolean isEmptyUploadFile(MultipartFile[] images) {
//		if(images == null || images.length <= 0) return true;
//		if(images.length == 1 && images[0].getOriginalFilename().isEmpty()) return true;
//		return false;
//	}
	
	/**
	 * Lưu sản phẩm và ảnh sản phẩm.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Product> filterProduct(final ProductFilterModel proFilter) {
		StringBuilder sql = new StringBuilder("FROM Product p WHERE 1=1");
		if (proFilter.getCategoryId() != null)
			sql.append(" AND p.category.id = " + proFilter.getCategoryId());
		if (proFilter.getCollectionId() != null)
			sql.append(" AND p.collection.id = " + proFilter.getCollectionId());
		if (proFilter.getBeginPrice() != null)
			sql.append(" AND p.price >= " + proFilter.getBeginPrice());
		if (proFilter.getEndPrice() != null && proFilter.getEndPrice() > proFilter.getBeginPrice())
			sql.append(" AND p.price <= " + proFilter.getEndPrice());
		if (proFilter.getSearchKeyword() != null) {
			Slugify slug = new Slugify();
			String keyword = slug.slugify(proFilter.upperKeyword());
			sql.append(" AND upper(p.seo) LIKE '%" + keyword + "%'");
		}

		if (proFilter.getTag() != null) {
			Slugify slug = new Slugify();
			String keyword = slug.slugify(proFilter.getTag().toUpperCase());
			sql.append(" AND upper(p.seo) LIKE '%" + keyword + "%'");
		}

		if (proFilter.getSort() != null)
			sql.append(" ORDER BY p.price " + proFilter.getSort());

		Query query = entityManager.createQuery(sql.toString());
		if (proFilter.getCurrenPage() == 1) {
			List<Product> initializeProducts = query.getResultList();
			proFilter.setSize(initializeProducts.size());
			proFilter.setTotalPage((int) Math.ceil((double) proFilter.getSize() / WebConstants.NUM_OF_PRODUCTS));
		}
		query.setFirstResult((proFilter.getCurrenPage() - 1) * WebConstants.NUM_OF_PRODUCTS);
		query.setMaxResults(WebConstants.NUM_OF_PRODUCTS);
		return query.getResultList();
	}

	public List<Product> findBySeo(String seo) {
		return productRepo.findBySeo(seo);
	}

	private boolean isEmptyUploadFile(MultipartFile[] images) {
		if (images == null || images.length <= 0)
			return true;
		if (images.length == 1 && images[0].getOriginalFilename().isEmpty())
			return true;
		return false;
	}

@Transactional
	public void save(MultipartFile[] productImages, Product product) throws IllegalStateException, IOException {
		
		if(product.getId() != null) { // chỉnh sửa
			// lấy dữ liệu cũ của sản phẩm
			Product productInDb = productRepo.findById(product.getId()).get();
			
			if(!isEmptyUploadFile(productImages)) { // nếu admin sửa ảnh sản phẩm
				// lấy danh sách ảnh cũ của sản phẩm
				List<ProductImages> oldProductImages = productInDb.getProductImages();
				
				// xoá ảnh cũ trên vật lí(host)
				for(ProductImages _images : oldProductImages) {//D:\tai lieu\Java\Java_Traing\om.devpro.shop11
					new File("D:\\tai lieu\\Java\\Java_Traing\\om.devpro.shop11\\images" + _images.getPath()).delete();
				}
				
				// xoá ảnh trên database
				product.removeProductImages();
				imageRepository.deleteAll(oldProductImages);
			} else { // ảnh phải giữ nguyên
				product.setProductImages(productInDb.getProductImages());
			}
			
		}

		if(!isEmptyUploadFile(productImages)) { // có upload ảnh lên.
			for(MultipartFile productImage : productImages) {//D:\tai lieu\Java\Java_Traing\om.devpro.shop11
				
				// lưu vật lí
				productImage.transferTo(new File("D:\\tai lieu\\Java\\Java_Traing\\om.devpro.shop11\\images" + productImage.getOriginalFilename()));
				
				ProductImages _productImages = new ProductImages();
				_productImages.setPath(productImage.getOriginalFilename());
				_productImages.setTitle(productImage.getOriginalFilename());
				_productImages.setCreatedDate(LocalDateTime.now());
				_productImages.setProduct(product);
				imageRepository.save(_productImages);
				product.addProductImages(_productImages);
			}
		}
		
		Slugify slg = new Slugify();
		String seo = slg.slugify(product.getTitle() + "-" + System.currentTimeMillis());
		product.setSeo(seo);
		
		productRepo.save(product);
	}
	
	/**
	 * Tìm kiếm sản phẩm.
	 * 
	 * @param productSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Product> search(final ProductSearch productSearch) {
//		String jpql = "Select caijcungduoc from Product caijcungduoc";
//		Query query = entityManager.createQuery(jpql, Product.class);

		String sql = "select * from tbl_products where 1=1";

		// tìm kiếm theo category ID.
		if(productSearch != null && productSearch.getCategoryId() != null) {
			sql = sql + " and category_id=" + productSearch.getCategoryId();
		} 
		
		// tìm kiếm theo seo của product
		if (productSearch != null && productSearch.getSeoProduct() != null) {
			sql = sql + " and seo='" + productSearch.getSeoProduct() + "'";
		}
		// tìm kiếm theo ID của sản phẩm.
		if(productSearch != null && productSearch.getId() != null) {
			sql = sql + " and id=" + productSearch.getId();
		}
		
		// tìm kiếm theo seo của category
		if(productSearch != null && productSearch.getSeoCategoty() != null) {
			sql = sql + " and category_id in (select id from tbl_category where seo='"+productSearch.getSeoCategoty()+"')";
		}

		Query query = entityManager.createNativeQuery(sql, Product.class);
		
		if(productSearch.getCurrentPage() != null && productSearch.getCurrentPage() > 0) {
			query.setFirstResult((productSearch.getCurrentPage()-1) * ProductSearch.SIZE_ITEMS_ON_PAGE);
			query.setMaxResults(ProductSearch.SIZE_ITEMS_ON_PAGE);
		}
		if(productSearch != null && productSearch.getKeyword() != null) {
			sql = sql + " and upper(title) like '%" + ((String) productSearch.getKeyword()).toUpperCase() + "%'";
		}
		return query.getResultList();
	}
	
}
