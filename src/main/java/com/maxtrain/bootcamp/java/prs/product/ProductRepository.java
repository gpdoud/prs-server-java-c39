package com.maxtrain.bootcamp.java.prs.product;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
	Iterable<Product> findByVendorId(int vendorId);
}
