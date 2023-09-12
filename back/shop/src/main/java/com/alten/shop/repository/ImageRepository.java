package com.alten.shop.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.alten.shop.model.Image;
import com.alten.shop.model.Product;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ImageRepository extends JpaRepository<Image, Integer> {

	Optional<Image> findByFilename(String filename);

	Optional<List<Image>> findByProductId(Product product );

	boolean existsByFilename(String filename);

	@Modifying
	@Transactional
	@Query("delete from image where filename = :filename")
	int deleteByFilename(@Param("filename") String filename);
}