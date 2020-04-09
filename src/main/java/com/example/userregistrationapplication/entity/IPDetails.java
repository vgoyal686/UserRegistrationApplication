package com.example.userregistrationapplication.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ipdetails")
@CompoundIndex(def = "{'ip':1, 'createdDate':-1}", name = "compound_index", unique = true)
public class IPDetails {

	@Id
	private String id;

	@NotNull
	private String ip;

	@NotNull
	private String createdDate = new String(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

	private int count;

	IPDetails() {

	}

	public IPDetails(@NotNull String ip, int count) {
		super();
		this.ip = ip;
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
