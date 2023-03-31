package com.maxtrain.bootcamp.java.prs.vendor.po;

import java.util.ArrayList;
import java.util.List;

import com.maxtrain.bootcamp.java.prs.vendor.Vendor;

public class Po {

	private Vendor vendor;
	private List<Poline> polines = new ArrayList<Poline>();
	private double total;
	
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public List<Poline> getPolines() {
		return polines;
	}
	public void setPolines(List<Poline> polines) {
		this.polines = polines;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
	
}
