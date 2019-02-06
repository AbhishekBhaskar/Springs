package com.ecomWebsite.modal;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="products")
public class products {

	@Id
	public int pr_id;
	private String name;
	private String url;
//	@Lob
//	private byte[] image;
	private String license_number;
	private float cost_per_item;
	private Date created;
	private int warranty_period;
	
	public products()
	{
		
	}

	public products(String name, String url, String license_number, float cost_per_item, Date created,byte[] image,
			int warranty_period) {
		super();
		this.name = name;
		this.url = url;
	//	this.image = image;
		this.license_number = license_number;
		this.cost_per_item = cost_per_item;
		this.created = created;
		this.warranty_period = warranty_period;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

/*	public byte[] getImage() {
		return image;
	}   

	public void setImage(byte[] image) {
		this.image = image;
	}  */   

	public String getLicense_number() {
		return license_number;
	}

	public void setLicense_number(String license_number) {
		this.license_number = license_number;
	}

	public float getCost_per_item() {
		return cost_per_item;
	}

	public void setCost_per_item(float cost_per_item) {
		this.cost_per_item = cost_per_item;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getWarranty_period() {
		return warranty_period;
	}

	public void setWarranty_period(int warranty_period) {
		this.warranty_period = warranty_period;
	}

	
	
}
