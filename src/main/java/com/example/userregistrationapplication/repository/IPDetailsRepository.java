package com.example.userregistrationapplication.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.userregistrationapplication.entity.IPDetails;

public interface IPDetailsRepository extends MongoRepository<IPDetails, String> {

	IPDetails findByIpAndCreatedDate(String ip, String createdDate);

}
