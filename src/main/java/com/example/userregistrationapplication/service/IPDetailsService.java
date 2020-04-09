package com.example.userregistrationapplication.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.userregistrationapplication.entity.IPDetails;
import com.example.userregistrationapplication.repository.IPDetailsRepository;

@Service
public class IPDetailsService {

	@Autowired
	IPDetailsRepository ipDetailsRepository;

	public IPDetails getIPDetails(String ip) {

		return ipDetailsRepository.findByIpAndCreatedDate(ip,
				new String(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

	}

	public void saveIPDetails(String ip) {

		IPDetails ipDetails = getIPDetails(ip);

		if (ipDetails != null) {
			ipDetails.setCount(ipDetails.getCount() + 1);
		} else {
			ipDetails = new IPDetails(ip, 1);
		}
		ipDetailsRepository.save(ipDetails);
	}

	public boolean checkAttempts(String ip) {

		IPDetails ipDetails = getIPDetails(ip);
		if (ipDetails != null && ipDetails.getCount() >= 3) {
			return true;
		}
		return false;
	}

}
