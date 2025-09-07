package com.rakesh.sms.boImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsmpp.bean.BindType;
import org.springframework.beans.factory.annotation.Autowired;

import com.rakesh.sms.bo.GatewayBo;
import com.rakesh.sms.dao.GatewayDao;
import com.rakesh.sms.entity.MatchContent;
import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.entity.SMSCFormats;

public class GatewayBoImpl implements GatewayBo {

	@Autowired
	private GatewayDao gatewayDaoImpl;

	public void setGatewayDaoImpl(GatewayDao gatewayDaoImpl) {
		this.gatewayDaoImpl = gatewayDaoImpl;
	}

	public String addFormatDetails(SMSCFormats format) {
		return this.gatewayDaoImpl.addFormatDetails(format);
	}// End Of Method

	public SMSCConfigs getConfigDetails() {
		return this.gatewayDaoImpl.getConfigDetails(null);
	}// End Of Method

	public SMSCConfigs getConfigDetails(String circle) {
		return this.gatewayDaoImpl.getConfigDetails(circle);
	}// End Of Method

	public SMSCFormats getFormatDetails(String cid) {
		return this.gatewayDaoImpl.getFormatDetails(cid.trim());
	}// End Of Method

	public List<SMSCConfigs> getAllCircleDetails() {
		return this.gatewayDaoImpl.getAllCircles();
	}// End Of Method
	
	public MatchContent getMatchContentDetails(int id, String matchType) {
		return this.gatewayDaoImpl.getMatchContent(id,matchType);
	}// End Of Method

	public List<String> getAllCircleNames() {

		List<SMSCConfigs> circles = this.gatewayDaoImpl.getAllCircles();
		HashMap<String, String> map = new HashMap<String, String>(10);
		List<String> circleNames = new ArrayList<String>(10);

		for (int i = 0; circles != null && i < circles.size(); i++) {
			SMSCConfigs config = (SMSCConfigs) circles.get(i);
			String circle = config.getCircle().toUpperCase().trim();

			if (map.containsKey(circle) == false) {
				map.put(circle, circle);
				circleNames.add(circle);
			} // End Of Unique Check

		} // End Of Loop

		map.clear();
		return circleNames;

	}// End Of Method

	public List<String> getTXCircleNames() {

		List<SMSCConfigs> circles = this.gatewayDaoImpl.getAllCircles();
		HashMap<String, String> map = new HashMap<String, String>(10);
		List<String> circleNames = new ArrayList<String>(10);

		for (int i = 0; circles != null && i < circles.size(); i++) {
			SMSCConfigs config = (SMSCConfigs) circles.get(i);
			String circle = config.getCircle().toUpperCase().trim();
			int bindMode = config.getBindMode();

			if (map.containsKey(circle) == false && bindMode != BindType.BIND_RX.ordinal()) {
				map.put(circle, circle);
				circleNames.add(circle);
			} // End Of Unique Check

		} // End Of Loop

		map.clear();
		return circleNames;

	}// End Of Method

	public List<String> getRXCircleNames() {

		List<SMSCConfigs> circles = this.gatewayDaoImpl.getAllCircles();
		HashMap<String, String> map = new HashMap<String, String>(10);
		List<String> circleNames = new ArrayList<String>(10);

		for (int i = 0; circles != null && i < circles.size(); i++) {
			SMSCConfigs config = (SMSCConfigs) circles.get(i);
			String circle = config.getCircle().toUpperCase().trim();
			int bindMode = config.getBindMode();

			if (map.containsKey(circle) == false && bindMode == BindType.BIND_RX.ordinal()) {
				map.put(circle, circle);
				circleNames.add(circle);
			} // End Of Unique Check

		} // End Of Loop

		map.clear();
		return circleNames;

	}// End Of Method

	public List<String> getTRXCircleNames() {

		List<SMSCConfigs> circles = this.gatewayDaoImpl.getAllCircles();
		HashMap<String, String> map = new HashMap<String, String>(10);
		List<String> circleNames = new ArrayList<String>(10);

		for (int i = 0; circles != null && i < circles.size(); i++) {
			SMSCConfigs config = (SMSCConfigs) circles.get(i);
			String circle = config.getCircle().toUpperCase().trim();
			int bindMode = config.getBindMode();

			if (map.containsKey(circle) == false && bindMode == BindType.BIND_TRX.ordinal()) {
				map.put(circle, circle);
				circleNames.add(circle);
			} // End Of Unique Check

		} // End Of Loop

		map.clear();
		return circleNames;

	}// End Of Method

}
