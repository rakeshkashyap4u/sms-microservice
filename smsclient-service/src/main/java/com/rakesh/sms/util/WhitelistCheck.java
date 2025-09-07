package com.rakesh.sms.util;

import java.util.HashMap;
import java.util.List;

import com.rakesh.sms.entity.SMSWhitelist;

public class WhitelistCheck {

	private static final String Plus = String.valueOf('+');

	private HashMap<String, HashMap<String, Byte>> whiteUsers;
	private HashMap<String, HashMap<String, Byte>> whiteSeries;

	private HashMap<String, HashMap<String, Byte>> whiteUsersMT;
	private HashMap<String, HashMap<String, Byte>> whiteSeriesMT;

	public WhitelistCheck() {
		this.whiteUsers = new HashMap<String, HashMap<String, Byte>>();
		this.whiteSeries = new HashMap<String, HashMap<String, Byte>>();

		this.whiteUsersMT = new HashMap<String, HashMap<String, Byte>>();
		this.whiteSeriesMT = new HashMap<String, HashMap<String, Byte>>();
	}// End Of Constructor

	public WhitelistCheck(List<SMSWhitelist> list) {
		this();
		this.load(list);
	}// End Of Constructor

	public void load(List<SMSWhitelist> list) {
		int msisdnLength = CoreUtils.getValidMsisdnLength();

		for (int i = 0; list != null && i < list.size(); i++) {

			SMSWhitelist row = list.get(i);

			String msisdn = row.getMsisdn();
			String serviceCode = row.getShortcode();

			String msgType = row.getMsgType();

			if (msgType.equalsIgnoreCase("MO")) {

				if (row.isSeries()) {
					/** SERIES **/
					if (msisdn != null && msisdn.trim().startsWith(Plus))
						msisdn = msisdn.substring(1);

					if (whiteSeries.containsKey(serviceCode)) {
						whiteSeries.get(serviceCode).put(msisdn, (byte) 0);
					} else {
						HashMap<String, Byte> series = new HashMap<String, Byte>();
						series.put(msisdn, (byte) 0);

						whiteSeries.put(serviceCode, series);
					}

				} else {
					if (msisdn != null && msisdn.length() > msisdnLength) {
						msisdn = CoreUtils.stripCodes(msisdn);

						if (msisdn != null) {
							if (whiteUsers.containsKey(serviceCode))
								whiteUsers.get(serviceCode).put(msisdn, (byte) 0);
							else {
								HashMap<String, Byte> users = new HashMap<String, Byte>();
								users.put(msisdn, (byte) 0);

								whiteUsers.put(serviceCode, users);
							}
						}

					} // End Of length Check
				}
			} else if (msgType.equalsIgnoreCase("MT")) {
				if (row.isSeries()) {
					/** SERIES **/
					if (msisdn != null && msisdn.trim().startsWith(Plus))
						msisdn = msisdn.substring(1);

					if (whiteSeriesMT.containsKey(serviceCode)) {
						whiteSeriesMT.get(serviceCode).put(msisdn, (byte) 0);
					} else {
						HashMap<String, Byte> series = new HashMap<String, Byte>();
						series.put(msisdn, (byte) 0);

						whiteSeriesMT.put(serviceCode, series);
					}

				} else {
					if (msisdn != null && msisdn.length() > msisdnLength) {
						msisdn = CoreUtils.stripCodes(msisdn);

						if (msisdn != null) {
							if (whiteUsersMT.containsKey(serviceCode))
								whiteUsersMT.get(serviceCode).put(msisdn, (byte) 0);
							else {
								HashMap<String, Byte> users = new HashMap<String, Byte>();
								users.put(msisdn, (byte) 0);

								whiteUsersMT.put(serviceCode, users);
							}
						}

					} // End Of length Check
				}

				Logger.sysLog(LogValues.info, this.getClass().getName(),
						"Whitelisted series for MO : " + whiteSeries.toString());
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						"Whitelisted msisdn for MO : " + whiteUsers.toString());
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						"Whitelisted series for MT : " + whiteSeriesMT.toString());
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						"Whitelisted msisdn for MO : " + whiteUsersMT.toString());

			}
		} // End Of Loop

	}// End Of Method

	public boolean isWhitelistedUser(String shortcode, String msisdn, String msgType) {

		if (shortcode == null || msisdn == null || msisdn.trim().length() == 0)
			return false;

		boolean result = false;
		String series = null;
		String num = null;

		if (msisdn.startsWith(Plus)) {
			num = msisdn.substring(1);
			num = CoreUtils.stripCodes(num);
		} else {
			num = CoreUtils.stripCodes(msisdn);
		}

		series = num.substring(0, new Integer(CoreUtils.getProperty("msisdnSeriesLength")));

		if (msgType.equalsIgnoreCase("MO")) {
			if (num != null) {

				if (whiteUsers.isEmpty() && whiteSeries.isEmpty())
					return true;
				else {
					if (!this.whiteUsers.isEmpty() && this.whiteUsers.containsKey(shortcode.trim())) {
						result = this.whiteUsers.get(shortcode.trim()).containsKey(msisdn);
					} else {
						if (!this.whiteSeries.isEmpty() && this.whiteSeries.containsKey(shortcode.trim()))
							result = this.whiteSeries.get(shortcode.trim()).containsKey(series);
						else
							result = true;
					}
				}
			}
		} else if (msgType.equalsIgnoreCase("MT")) {

			if (num != null) {

				if (whiteUsersMT.isEmpty() && whiteSeriesMT.isEmpty())
					return true;
				else {
					if (!this.whiteUsersMT.isEmpty() && this.whiteUsersMT.containsKey(shortcode.trim())) {
						result = this.whiteUsersMT.get(shortcode.trim()).containsKey(msisdn);
					} else {
						if (!this.whiteSeriesMT.isEmpty() && this.whiteSeriesMT.containsKey(shortcode.trim()))
							result = this.whiteSeriesMT.get(shortcode.trim()).containsKey(series);
						else
							result = true;
					}
				}
			}

		} else {
			Logger.sysLog(LogValues.warn, this.getClass().getName(), "MSG Type not defined!");
			result = false;
		} // End Of MsgType check

		Logger.sysLog(LogValues.info, this.getClass().getName(),
				"[" + shortcode + "][" + msisdn + "][" + msgType.toUpperCase() + "]  Is Whitelisted: " + result);

		return result;

	}// End Of Method

}
