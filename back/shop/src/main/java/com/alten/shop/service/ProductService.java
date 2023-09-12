package com.alten.shop.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alten.shop.exception.ResourceNotFoundException;
import com.alten.shop.model.Product;
import com.alten.shop.repository.ProductRepository;
import com.alten.shop.util.Constants;

/**
 * @author ayarima
 *
 */
@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;
	
	
	/** Method to to get product by it's id from database.
	 * @param id
	 * @return
	 */
	public Product getProduct(long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.RESOURCE_NOT_FOUNT_MSG));
	}

	/**  Method to to save from database.
	 * @param _product
	 * @return
	 */
	public Product saveProduct(Product _product) {
		return productRepository.save(_product);
	}

	/** Method to to update from database.
	 * @param _product
	 * @return
	 */
	public Product updateProduct(Product _product) {
		return productRepository.save(_product);
	}

	/** Method to get all products from database.
	 * @return
	 */
	public List<Product> findAllProduct() {
		return productRepository.findAll();
	}
	
	/** Method to delete all product by it's id in database.
	 * @param id
	 */
	public void deleteProductById(long id) {
		productRepository.deleteById(id);
	}

	/**
	 *  Method to delete all products in database.
	 */
	public void deleteAllProduct() {
		productRepository.deleteAll();
	}
	
}
