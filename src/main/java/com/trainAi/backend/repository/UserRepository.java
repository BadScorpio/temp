package com.trainAi.backend.repository;

import com.trainAi.backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByAddress(String address);
}
