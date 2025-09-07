package com.rakesh.sms.entity;

import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "BlackoutHours")

public class BlackoutHours {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@Column(name = "blackout_start", nullable = false)
	private Time blackout_start;

	@Column(name = "blackout_end", nullable = false)
	private Time blackout_end;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Time getBlackout_start() {
		return blackout_start;
	}

	public void setBlackout_start(Time blackout_start) {
		this.blackout_start = blackout_start;
	}

	public Time getBlackout_end() {
		return blackout_end;
	}

	public void setBlackout_end(Time blackout_end) {
		this.blackout_end = blackout_end;
	}

}// End Of Entity
