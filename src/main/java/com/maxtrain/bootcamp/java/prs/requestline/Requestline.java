package com.maxtrain.bootcamp.java.prs.requestline;

import com.fasterxml.jackson.annotation.*;
import com.maxtrain.bootcamp.java.prs.product.Product;
import com.maxtrain.bootcamp.java.prs.request.Request;

import jakarta.persistence.*;

@Entity
@Table(name="requestlines")
public class Requestline {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private int quantity;
	
	@JsonBackReference
	//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
	@ManyToOne(optional=false)
	@JoinColumn(name="requestId")
	private Request request;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="productId")
	private Product product;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	
}
