package com.challenge.marketplace.repo;

import com.challenge.marketplace.models.Product;
import com.challenge.marketplace.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
