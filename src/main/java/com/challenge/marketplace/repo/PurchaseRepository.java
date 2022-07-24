package com.challenge.marketplace.repo;

import com.challenge.marketplace.models.Purchase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
    @Query(value="SELECT * FROM `purchase` WHERE `user_id`=?1", nativeQuery = true)
    List<Purchase> findByUserId(Long userId);

    @Query(value="SELECT * FROM `purchase` WHERE `product_id`=?1", nativeQuery = true)
    List<Purchase> findByProductId(Long userId);
}
