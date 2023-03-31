package com.maxtrain.bootcamp.java.prs.request;

import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Integer> {
	Iterable<Request> findByStatusAndUserIdNot(String status, int userId);
	Iterable<Request> findByStatus(String status);
}
