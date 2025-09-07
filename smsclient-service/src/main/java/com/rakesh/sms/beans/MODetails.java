package com.rakesh.sms.beans;

import java.util.ArrayList;
import java.util.List;

import com.rakesh.sms.entity.MessageActions;
import com.rakesh.sms.util.CoreUtils;

public class MODetails {

	private String moId;
	private String servicecode;
	private String keyword;
	private String serviceid;
	private List<MOAction> actions;
	private String countryCode;

	public MODetails() {
		this.actions = new ArrayList<MOAction>(5);
	}// End Of Constructor

	public String getMoId() {
		return moId;
	}

	public void setMoId(String moId) {
		this.moId = moId;
	}

	public void setMoId(int moId) {
		this.moId = String.valueOf(moId);
	}

	public String getServicecode() {
		return servicecode;
	}

	public void setServicecode(String serviceCode) {
		this.servicecode = serviceCode;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getServiceid() {
		return serviceid;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	class MOAction {

		private String aid;
		private String type;
		private String details;

		public String getAid() {
			return aid;
		}

		public void setAid(String aid) {
			this.aid = aid;
		}

		public void setAid(int aid) {
			this.aid = String.valueOf(aid);
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDetails() {
			return details;
		}

		public void setDetails(String details) {
			this.details = details;
		}

		@Override
		public String toString() {
			return CoreUtils.GSON.toJson(this);
		}// End Of Method

	}// End Of Inner Class

	public void addAction(MessageActions action) {

		MOAction act = new MOAction();
		act.setAid(action.getAid());
		act.setType(action.getType());
		act.setDetails(action.getDetails());

		this.actions.add(act);

	}// End Of Method

	@Override
	public String toString() {
		return CoreUtils.GSON.toJson(this);
	}// End Of Method


}
