package com.maxtrain.bootcamp.java.prs.requestline;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.bootcamp.java.prs.product.Product;
import com.maxtrain.bootcamp.java.prs.product.ProductRepository;
import com.maxtrain.bootcamp.java.prs.request.Request;
import com.maxtrain.bootcamp.java.prs.request.RequestRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/requestlines")
public class RequestlineController {

	@Autowired
	private RequestlineRepository reqlRepo;
	@Autowired
	private RequestRepository reqRepo;
	@Autowired
	private ProductRepository prodRepo;
	
	private boolean recalculateRequestTotal(int requestId) {
		// read the request
		Optional<Request> optRequest = reqRepo.findById(requestId);
		// check that we read a request
		if(optRequest.isEmpty()) {
			return false;
		}
		Request request = optRequest.get();
		// get the requestlines from the request
		Iterable<Requestline> requestlines = reqlRepo.findByRequestId(requestId);
		// calculate the total
		double total = 0;
		for(Requestline rl : requestlines) {
			// fill an empty product instance
			if(rl.getProduct().getName() == null) {
				Product product = prodRepo.findById(rl.getProduct().getId()).get();
				rl.setProduct(product);
			}
			total += rl.getQuantity() * rl.getProduct().getPrice();
		}
		request.setTotal(total);
		reqRepo.save(request);
		return true;
	}
		
	@GetMapping
	public ResponseEntity<Iterable<Requestline>> getRequestlines() {
		Iterable<Requestline> requestlines = reqlRepo.findAll();
		return new ResponseEntity<Iterable<Requestline>>(requestlines, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Requestline> getRequestline(@PathVariable int id) {
		Optional<Requestline> requestline = reqlRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Requestline>(requestline.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Requestline> postRequestline(@RequestBody Requestline requestline) {
		Requestline newRequestline = reqlRepo.save(requestline);
		if(!recalculateRequestTotal(requestline.getRequest().getId())) {
			return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
		}
		return new ResponseEntity<Requestline>(newRequestline, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putRequestline(@PathVariable int id, @RequestBody Requestline requestline) {
		if(requestline.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		reqlRepo.save(requestline);
		if(!recalculateRequestTotal(requestline.getRequest().getId())) {
			return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteRequestline(@PathVariable int id) {
		Optional<Requestline> requestline = reqlRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		reqlRepo.delete(requestline.get());
		if(!recalculateRequestTotal(requestline.get().getRequest().getId())) {
			return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
