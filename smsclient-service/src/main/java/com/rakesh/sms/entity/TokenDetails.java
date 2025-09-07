package com.rakesh.sms.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "tokendetails")
public class TokenDetails {
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "client_id", unique = false, nullable = false)
	private String clientId;
	
	@Column(name = "client_secret", unique = false, nullable = false)
	private String clientSecret;
	
	@Column(name = "service_id", unique = true, nullable = false)
	private String serviceId;
	
	@Column(name = "service_name", unique = true, nullable = false)
	private String serviceName;
	
	@Column(name = "token", unique = false, nullable = true)
	private String token;
	
	@Column(name = "generated_date", unique = false, nullable = false)
	private Date generatedDate;

	public TokenDetails(Integer id, String clientId, String clientSecret, String serviceId, String serviceName,
			String token, Date generatedDate) {
		super();
		this.id = id;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.token = token;
		this.generatedDate = generatedDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}
	
}
