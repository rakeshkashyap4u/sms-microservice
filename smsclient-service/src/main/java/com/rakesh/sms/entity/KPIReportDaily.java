package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "kpi_report_daily", catalog = "cdrlog")
public class KPIReportDaily {

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "reportdate")
	private String reportDate;

	@Column(name = "keyword")
	private String keyword;

	@Column(name = "shortcode")
	private String shortcode;

	@Column(name = "servicename")
	private String servicename;

	@Column(name = "type1")
	private String type;

	@Column(name = "total")
	private Integer total;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public String getType() {
		return type;
	}

	public void setType1(String type) {
		this.type = type;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
}
