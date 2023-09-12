package com.alten.shop.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alten.shop.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
