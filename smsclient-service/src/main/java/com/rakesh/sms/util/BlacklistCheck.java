package com.rakesh.sms.util;

import java.util.HashMap;
import java.util.List;

import com.rakesh.sms.beans.Trie;
import com.rakesh.sms.entity.SMSBlacklist;

public class BlacklistCheck {

	/**
	 * 
	 * It by default assumes that the number is National In case of International
	 * number. + should be appended.
	 * 
	 **/
	private static final String Plus = String.valueOf('+');
	private HashMap<String, Byte> blackUsers;
	private Trie blackSeries;

	public BlacklistCheck() {
		this.blackUsers = new HashMap<String, Byte>();
		this.blackSeries = new Trie();
	}// End Of Constructor

	public BlacklistCheck(List<SMSBlacklist> list) {
		this();
		this.load(list);
	}// End Of Constructor

	public void load(List<SMSBlacklist> list) {

		int msisdnLength = CoreUtils.getValidMsisdnLength();

		for (int i = 0; list != null && i < list.size(); i++) {

			SMSBlacklist row = list.get(i);
			String msisdn = row.getMsisdn();

			if (row.isSeries()) {
				/** SERIES **/
				if (msisdn != null && msisdn.trim().startsWith(Plus)) {
					this.blackSeries.insert(msisdn.substring(1));
				} else {
					this.blackSeries.insert(msisdn);
				}
			} else {
				/** MSISDN **/
				if (msisdn != null && msisdn.length() > msisdnLength) {
					if (msisdn.startsWith(Plus)) {
						msisdn = msisdn.substring(1).trim();
					} else {
						msisdn = CoreUtils.stripCodes(msisdn);
					}
				}

				if (msisdn != null) {
					this.blackUsers.put(msisdn.trim(), (byte) 0);
				}

			} // End Of Series Check
		} // End Of Loop

	}// End Of Method

	public boolean isBlacklistedUser(String msisdn) {

		if (msisdn == null || msisdn.trim().length() == 0) {
			return false;
		} else if (this.blackUsers.isEmpty() && this.blackSeries.isEmpty()) {
			return false;
		} else {

			boolean result = false;
			String num = null;

			if (msisdn.startsWith(Plus)) {
				num = msisdn.substring(1);
			} else {
				num = CoreUtils.stripCodes(msisdn);
			}

			if (num != null) {
				result = this.blackUsers.containsKey(num.trim()) || this.blackSeries.search(num);
			} else {
				Logger.sysLog(LogValues.warn, this.getClass().getName(),
						"  [" + msisdn + "]  Is Blacklist:: Invalid Number => false ");
			}

			if (result)
				Logger.sysLog(LogValues.info, this.getClass().getName(), "  [" + msisdn + "]  Is Blacklist: " + result);

			return result;

		} // End Of If Else

	}// End Of Method
}
