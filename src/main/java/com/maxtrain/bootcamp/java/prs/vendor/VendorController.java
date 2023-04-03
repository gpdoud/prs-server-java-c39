package com.maxtrain.bootcamp.java.prs.vendor;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.bootcamp.java.prs.product.*;
import com.maxtrain.bootcamp.java.prs.request.*;
import com.maxtrain.bootcamp.java.prs.requestline.*;
import com.maxtrain.bootcamp.java.prs.vendor.po.*;

@CrossOrigin
@RestController
@RequestMapping("/api/vendors")
public class VendorController {

	@Autowired
	private VendorRepository vndRepo;
	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	private RequestlineRepository reqlRepo;
	@Autowired
	private RequestRepository reqRepo;
	
	@GetMapping("po/{vendorId}")
	public ResponseEntity<Po> getPo(@PathVariable int vendorId) {
		Po po = new Po();
		// Get the vendor
		Optional<Vendor> optVendor = vndRepo.findById(vendorId);
		if(optVendor.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Vendor vendor = optVendor.get();
		po.setVendor(vendor);
		// get the product from that vendor
		Iterable<Product> products = prodRepo.findByVendorId(vendor.getId());
		HashMap<Integer, Product> dictProducts = new HashMap<Integer, Product>();
		for(Product product : products) {
			dictProducts.put(product.getId(), product);
		}
		// get approved requests	
		Iterable<Request> requests = reqRepo.findByStatus(Request.STATUS_APPROVED);
		// get the request lines from approved requests
		ArrayList<Requestline> requestlinesFromApprovedRequests = new ArrayList<Requestline>();
		for(Request request : requests) {
			// get requestlines from approved requests
			Iterable<Requestline> reqlines = reqlRepo.findByRequestId(request.getId());
			for(Requestline rl : reqlines) {
				requestlinesFromApprovedRequests.add(rl);
			}
		}
		// eliminate request lines with products from another vendor
		ArrayList<Requestline> requestlinesFromVendor = new ArrayList<Requestline>();
		for(Requestline rl : requestlinesFromApprovedRequests) {
			if(dictProducts.containsKey(rl.getProduct().getId())) {
				requestlinesFromVendor.add(rl);
			}
		}
		// the request lines that have products from the selected vendor
		// can be used to calculate the total
		for(Requestline requestline : requestlinesFromVendor) {
			// the Po instance was created above
			// create a new Poline
			Poline poline = new Poline();
			// fill it with data (this Id can be omitted)
			poline.setId(requestline.getId());
			poline.setProductId(requestline.getProduct().getId());
			poline.setProductName(requestline.getProduct().getName());
			poline.setProductPrice(requestline.getProduct().getPrice());
			poline.setQuantity(requestline.getQuantity());
			// calculate the line total by multiplying the quantity times the price
			double lineTotal = requestline.getQuantity() * requestline.getProduct().getPrice();
			poline.setLineTotal(lineTotal);
			// add new new Poline to the Po.polines collection
			po.getPolines().add(poline);
			// accumulate the request total by adding the line total
			po.setTotal(po.getTotal() + lineTotal);
		}
		
		return new ResponseEntity<Po>(po, HttpStatus.OK);
	}
		
	@GetMapping
	public ResponseEntity<Iterable<Vendor>> getVendors() {
		Iterable<Vendor> vendors = vndRepo.findAll();
		return new ResponseEntity<Iterable<Vendor>>(vendors, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Vendor> getVendor(@PathVariable int id) {
		Optional<Vendor> vendor = vndRepo.findById(id);
		if(vendor.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Vendor>(vendor.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Vendor> postVendor(@RequestBody Vendor vendor) {
		Vendor newVendor = vndRepo.save(vendor);
		return new ResponseEntity<Vendor>(newVendor, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putVendor(@PathVariable int id, @RequestBody Vendor vendor) {
		if(vendor.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		vndRepo.save(vendor);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteVendor(@PathVariable int id) {
		Optional<Vendor> vendor = vndRepo.findById(id);
		if(vendor.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		vndRepo.delete(vendor.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
