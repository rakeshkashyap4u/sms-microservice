package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "MsisdnSeries")
public class MsisdnSeries {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private String id;

	@Column(name = "series")
	private String series;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "circleId")
	private SMSCConfigs circle;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series.trim();
	}

	public SMSCConfigs getCircle() {
		return circle;
	}

	public void setCircle(SMSCConfigs circle) {
		this.circle = circle;
	}

}
