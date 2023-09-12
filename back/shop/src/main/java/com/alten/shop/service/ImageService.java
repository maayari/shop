package com.alten.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.alten.shop.exception.ResourceNotFoundException;
import com.alten.shop.model.Image;
import com.alten.shop.model.Product;
import com.alten.shop.repository.ImageRepository;
import com.alten.shop.repository.ProductRepository;
import java.util.List;
import java.util.Optional;

/**
 * @author ayarima
 *
 */
@Service
@Slf4j
public class ImageService {

	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	ProductRepository productRepository;

	/** Method to retrieve image by it's name from database.
	 * @param filename
	 * @return
	 */
	public Image getImage(String filename) {
		return imageRepository.findByFilename(filename).orElseThrow(() -> new ResourceNotFoundException("Failed to get image from database."));
	}

	/** Method to retrieve all images of a product from database.
	 * @param productId
	 * @return
	 */
	public Optional<List<Image>> retrieveImage(long productId) {
		try {
			Optional<Product> productData = productRepository.findById(productId);
			if (productData != null && productData.isPresent()) {
				return imageRepository.findByProductId(productData.get());
			}else {
				throw new ResourceNotFoundException("Product does not exist in database." );
			}
		} catch (Exception e) {
			log.error(String.format("Failed to get image from database.") , e);
			throw new ResourceNotFoundException("Failed to get image from database." );
		}
	}

	/** Method to save image in database.
	 * @param file
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public Image saveImageInDataBase(MultipartFile file, long productId) throws Exception {
		Optional<Product> productData = productRepository.findById(productId);
		if (productData.isPresent()) {
			var image = Image.builder().filename(file.getOriginalFilename()).productId(productData.get())
					.mimeType(file.getContentType()).data(file.getBytes()).build();
			return imageRepository.save(image);
		} else {
			return null;
		}
	}

	/** Method to delete image by name from database.
	 * @param filename
	 * @return
	 */
	public int delete(String filename) {
		return imageRepository.deleteByFilename(filename);
	}
}