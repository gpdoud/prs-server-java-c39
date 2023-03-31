package com.maxtrain.bootcamp.java.prs.requestline;

import org.springframework.data.repository.CrudRepository;

public interface RequestlineRepository extends CrudRepository<Requestline, Integer> {
	Iterable<Requestline> findByRequestId(int requestId);
	Iterable<Requestline> findByProductId(int productId);
}
