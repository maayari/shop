package com.alten.shop.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alten.shop.exception.ErrorMessage;
import com.alten.shop.model.Product;
import com.alten.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.alten.shop.util.Constants; 
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ProductController {

	@Autowired
	ProductService productService;

	

	/** HTTP GET to retrieve product from product table.
	 *  /api/products/{id}
	 * @param id
	 * @return
	 */
	@GetMapping("/products/{id}")
	public ResponseEntity<Object> getProductById(@PathVariable("id") long id) {
		Product product = productService.getProduct(id);
		if(product == null) {
			log.info(String.format("Retrieve product failed , there is no product with id = {}."), id );
			return new ResponseEntity<>( new ErrorMessage(404, Constants.RESOURCE_NOT_FOUNT_MSG) , HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	/** HTTP DELETE to delete product from product table and all images of product in image table.
	 *  /api/products/{id}
	 * @param id
	 * @return
	 */
	@DeleteMapping("/products/{id}")
	public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") long id) {
		try {
			productService.deleteProductById(id);
			log.info(String.format("Product with id = {} is deleted with success."), id );
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/** HTTP GET to retrieve all product from product table.
	 *  /api/products
	 * @param id
	 * @return
	 */
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> products = new ArrayList<>();
		productService.findAllProduct().forEach(products::add);
		if (products.isEmpty()) {
			log.info(String.format("There is no products in data base."));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	/** HTTP POST to create product in product table.
	 *  /api/products
	 * @param id
	 * @return
	 */
	@PostMapping("/products")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		try {
			Product _product = productService.saveProduct(new Product(product.getCode(), product.getName(),
					product.getDescription(), product.getPrice(), product.getCategory(), product.getInventoryStatus(),
					product.getQuantity(), product.getRating()));
			log.info(String.format(String.format("Product created with success.")));
			return new ResponseEntity<>(_product, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/** HTTP PUT to update product in product table.
	 *  /api/products
	 * @param id
	 * @return
	 */
	@PutMapping("/products")
	public ResponseEntity<Object> updateProduct(@RequestBody Product product) {
		Product _product = productService.getProduct(product.getId());
		if (_product != null) {			 
			_product.setCode(product.getCode());
			_product.setDescription(product.getDescription());
			_product.setName(product.getName());
			_product.setQuantity(product.getQuantity());
			_product.setCategory(product.getCategory());
			_product.setInventoryStatus(product.getInventoryStatus());
			_product.setPrice(product.getPrice());
			Product savedProduct = productService.updateProduct(_product) ;
			log.info(String.format("Product with id = {} is updated with success."), product.getId() );
			return new ResponseEntity<>(savedProduct, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ErrorMessage(404, Constants.RESOURCE_NOT_FOUNT_MSG), HttpStatus.NOT_FOUND);
		}
	}

	/** HTTP DELETE to delete all products in product table and images in image table.
	 *  /products/deleteAll
	 * @param id
	 * @return
	 */
	@DeleteMapping("/products/deleteAll")
	public ResponseEntity<Object> deleteAllProducts() {
		try {
			productService.deleteAllProduct();
			log.info(String.format("All products are deleted with success."));
			return new ResponseEntity<>(new ErrorMessage(200, Constants.RESOURCE_DELETED_PRODUCTS_MSG), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorMessage(500, Constants.INTERNAL_ERROR_MSG), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
