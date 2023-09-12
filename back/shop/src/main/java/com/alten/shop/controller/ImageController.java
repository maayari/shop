package com.alten.shop.controller;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.alten.shop.exception.ErrorMessage;
import com.alten.shop.exception.ResourceNotFoundException;
import com.alten.shop.model.Image;
import com.alten.shop.service.ImageService;
import com.alten.shop.util.Constants;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api")
public class ImageController {

	
	@Autowired
	ImageService imageService;

    /**
     * @param filename
     * @return
     */
    @GetMapping("/images/{filename}")
	public ResponseEntity<Object> retrieve(@PathVariable String filename) {
		try {
			var image = imageService.getImage(filename);
			if (image != null) {
				var body = new ByteArrayResource(image.getData());
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, image.getMimeType())
						.cacheControl(CacheControl.maxAge(Duration.ofSeconds(60)).cachePrivate().mustRevalidate())
						.body(body);
			}
		} catch (ResourceNotFoundException e) {
			log.error(String.format("Retrieve Image failed."), e);
			return new ResponseEntity<>(new ErrorMessage(404, Constants.RESOURCE_NOT_FOUNT_MSG), HttpStatus.NOT_FOUND);

		}
		return new ResponseEntity<>(new ErrorMessage(500, "Retrieve Image failed."), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * @param productId
	 * @return
	 */
	@GetMapping("/images/product/{productId}")
	public ResponseEntity<Object> retrieveByProductId(@PathVariable long productId) {
		try {
			Optional<List<Image>> images =	imageService.retrieveImage(productId);
			if (images != null  && images.isPresent() ) {
				var body = new ByteArrayResource(images.get().get(0).getData());
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, images.get().get(0).getMimeType())
						.cacheControl(CacheControl.maxAge(Duration.ofSeconds(60)).cachePrivate().mustRevalidate())
						.body(body);
			}
		} catch (Exception e) {			
			log.error("Retrieve Image failed." , e);
			return new ResponseEntity<>(new ErrorMessage(404, Constants.RESOURCE_NOT_FOUNT_MSG), HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<>(new ErrorMessage(500, "Retrieve Image failed."), HttpStatus.INTERNAL_SERVER_ERROR);
	}

    /**
     * @param file
     * @param productId
     * @return
     */
    @PostMapping("/images/upload/{productId}")
    public ResponseEntity<Object>  saveImageInDataBase(@RequestPart MultipartFile file, @PathVariable long productId) {
        try {
            var image = imageService.saveImageInDataBase(file, productId);
            if(image != null ) {
            	return  new ResponseEntity<>(image, HttpStatus.CREATED);
            }else {
            	return new ResponseEntity<>(new ErrorMessage(500, String.format("Save image for the product %d failed. Product does not exist.", productId)), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Failed to save image in database.", e);
            return  new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}