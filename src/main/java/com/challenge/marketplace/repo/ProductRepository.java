package com.challenge.marketplace.repo;

import com.challenge.marketplace.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
