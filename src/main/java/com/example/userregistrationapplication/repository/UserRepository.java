package com.example.userregistrationapplication.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.userregistrationapplication.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

	User findByEmail(String email);

}
