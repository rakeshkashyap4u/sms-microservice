package com.rakesh.sms.beans;

import java.util.Date;

import com.rakesh.sms.entity.LanguageSpecification;
import com.google.gson.annotations.Expose;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class Message {

	public static final String DEFAULT_SERVICE_TYPE = new String("CMT");
	public static final byte DEFAULT_DATA_CODING_VALUE = 8;
	public static final int DEFAULT_FAILURE_RETRIES = 1;
	@Expose
	private boolean callback, reschedule, longMessage;
	@Expose
	private String cli, msisdn, message, serviceType, lang, subserviceid;
	@Expose
	private String productid, pricepointid;
	@Expose
	private LongMessageParameters optionalLongParams;
	@Expose
	private SessionParameters optionalSessParams;
	@Expose
	private Callback callbackDetails;
	@Expose
	private CoreEnums.Protocol mode;
	@Expose
	private CoreEnums.SMSType type;
	@Expose
	private CoreEnums.SMSFlag flag;
	@Expose
	private int remainingRetries;
	@Expose
	private CoreEnums.Type usage;
	@Expose
	private String extraDetail;
	@Expose
	private Date expiryTime;
	@Expose
	private byte dataCoding;
	@Expose
	private String encoding;
	@Expose
	private String circle;
	@Expose
	private String serviceid;
	
	@Expose
	private long time;
	
	private String subEndDate;
	private String authkey;
	@Expose
	private String corelatorId;  //Burkina Faso
	@Expose
	private String token;  //Burkina Faso
	@Expose
	private String senderName;  //Burkina Faso
	@Expose
	private String source;    //Elsalvador
	
	
	public String getSubserviceid() {
		return subserviceid;
	}

	public void setSubserviceid(String subserviceid) {
		this.subserviceid = subserviceid;
	}
	

	public String getPricepointid() {
		return pricepointid;
	}

	public void setPricepointid(String pricepointid) {
		this.pricepointid = pricepointid;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCorelatorId() {
		return corelatorId;
	}

	public void setCorelatorId(String corelatorId) {
		this.corelatorId = corelatorId;
	}

	//Mexico
	@Expose
	private String msgid;
	
	
	@Expose
	private String txnId;
	
	public String getMsgid() {
		return msgid;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnid(String txnId) {
		this.txnId = txnId;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getAuthkey() {
		return authkey;
	}

	public void setAuthkey(String authkey) {
		this.authkey = authkey;
	}

	public Message() {
		this.remainingRetries = Message.DEFAULT_FAILURE_RETRIES;
		this.dataCoding = Message.DEFAULT_DATA_CODING_VALUE;
		this.serviceType = Message.DEFAULT_SERVICE_TYPE;
		this.cli = this.msisdn = this.message = new String("");
		this.mode = CoreEnums.Protocol.UNKNOWN;
		this.type = CoreEnums.SMSType.UNKNOWN;
		this.flag = CoreEnums.SMSFlag.UNKNOWN;
		this.time = System.currentTimeMillis();
		this.usage = CoreEnums.Type.UNKNOWN;
		this.optionalLongParams = null;
		this.optionalSessParams = null;
		this.callbackDetails = null;
		this.longMessage = false;
		this.subEndDate = null;
		this.expiryTime = null;
		this.serviceid = null;
		this.extraDetail = "";
		this.callback = false;
		this.txnId = null;
		this.productid = null;
	}// End Of Constructor

	public Message(String cli, String msisdn) {
		this.remainingRetries = Message.DEFAULT_FAILURE_RETRIES;
		this.dataCoding = Message.DEFAULT_DATA_CODING_VALUE;
		this.serviceType = Message.DEFAULT_SERVICE_TYPE;
		this.mode = CoreEnums.Protocol.UNKNOWN;
		this.type = CoreEnums.SMSType.UNKNOWN;
		this.flag = CoreEnums.SMSFlag.UNKNOWN;
		this.time = System.currentTimeMillis();
		this.usage = CoreEnums.Type.UNKNOWN;
		this.message = new String("");
		this.optionalLongParams = null;
		this.optionalSessParams = null;
		this.callbackDetails = null;
		this.longMessage = false;
		this.expiryTime = null;
		this.subEndDate = null;
		this.serviceid = null;
		this.callback = false;
		this.extraDetail = "";
		this.msisdn = msisdn;
		this.cli = cli;
		this.productid = null;
	}// End Of Constructor

	public Message(String cli, String msisdn, CoreEnums.SMSType type, String message, CoreEnums.Protocol mode,
			CoreEnums.Type usage) {
		this.cli = cli;
		this.msisdn = msisdn;
		this.message = message;
		this.type = type;
		this.mode = mode;
		this.usage = usage;
		this.time = System.currentTimeMillis();
		this.remainingRetries = Message.DEFAULT_FAILURE_RETRIES;
		this.dataCoding = Message.DEFAULT_DATA_CODING_VALUE;
		this.serviceType = Message.DEFAULT_SERVICE_TYPE;
		this.flag = CoreEnums.SMSFlag.UNKNOWN;
		this.optionalLongParams = null;
		this.optionalSessParams = null;
		this.callbackDetails = null;
		this.longMessage = false;
		this.expiryTime = null;
		this.subEndDate = null;
		this.serviceid = null;
		this.callback = false;
		this.extraDetail = "";
		this.circle = "";
		this.productid = null;
	}// End Of Constructor

	public Message(String cli, String msisdn, int type, String message, int mode, int usage) {
		this.cli = cli;
		this.msisdn = msisdn;
		this.message = message;
		this.type = CoreEnums.SMSType.values[type];
		this.mode = CoreEnums.Protocol.values[mode];
		this.usage = CoreEnums.Type.values[usage];
		this.time = System.currentTimeMillis();
		this.remainingRetries = Message.DEFAULT_FAILURE_RETRIES;
		this.dataCoding = Message.DEFAULT_DATA_CODING_VALUE;
		this.serviceType = Message.DEFAULT_SERVICE_TYPE;
		this.flag = CoreEnums.SMSFlag.UNKNOWN;
		this.optionalLongParams = null;
		this.optionalSessParams = null;
		this.callbackDetails = null;
		this.longMessage = false;
		this.expiryTime = null;
		this.subEndDate = null;
		this.serviceid = null;
		this.callback = false;
		this.extraDetail = "";
		this.circle = "";
		this.productid = null;
	}// End Of Constructor

	public String getCli() {
		return cli;
	}

	public void setCli(String cli) {
		this.cli = cli;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public CoreEnums.SMSType getType() {
		return type;
	}

	public void setType(CoreEnums.SMSType type) {
		this.type = type;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CoreEnums.Protocol getMode() {
		return mode;
	}

	public void setMode(CoreEnums.Protocol mode) {
		this.mode = mode;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public CoreEnums.Type getUsage() {
		return usage;
	}

	public void setUsage(CoreEnums.Type usage) {
		this.usage = usage;
	}

	public boolean isCallback() {
		return callback;
	}

	public void setCallback(boolean callback) {
		this.callback = callback;
		if (callback)
			this.callbackDetails = new Callback();
		else
			this.callbackDetails = null;
	}

	public Callback getCallbackDetails() {
		return callbackDetails;
	}

	public void setCallbackDetails(Callback callbackDetails) {
		this.callbackDetails = callbackDetails;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public boolean isReschedule() {
		return reschedule;
	}

	public void setReschedule(boolean reschedule) {
		this.reschedule = reschedule;
	}

	public int getRemainingRetries() {
		return remainingRetries;
	}

	public void setRemainingRetries(int remainingRetries) {
		this.remainingRetries = remainingRetries;
	}

	public void decreaseRetry() {
		this.remainingRetries--;

		if (this.remainingRetries <= 0)
			this.reschedule = false;
	}// End Of Method

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setEncoding(boolean encoding) {
		this.encoding = String.valueOf(encoding);
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public CoreEnums.SMSFlag getFlag() {
		return flag;
	}

	public void setFlag(CoreEnums.SMSFlag flag) {
		this.flag = flag;
	}

	public byte getDataCoding() {
		return dataCoding;
	}

	public void setDataCoding(byte dataCoding) {
		this.dataCoding = dataCoding;
	}

	public Date getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}

	public boolean isExpired() {
		Date now = new Date();
		if (expiryTime == null) {
			return false;
		} else if (expiryTime.before(now)) {
			return true;
		} else
			return false;
	}// End Of Method

	public String getExtraDetail() {
		return extraDetail;
	}

	public void setExtraDetail(String extraDetail) {
		this.extraDetail = extraDetail;
	}

	public String getSubEndDate() {
		return subEndDate;
	}

	public void setSubEndDate(String subEndDate) {
		this.subEndDate = subEndDate;
	}

	public boolean isLongMessage() {
		return longMessage;
	}
	
	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public LongMessageParameters getOptionalLongParams() {
		return optionalLongParams;
	}

	public void setOptionalLongParams(LongMessageParameters optionalLongParams) {
		this.optionalLongParams = optionalLongParams;
		if (this.optionalLongParams != null) {
			this.optionalLongParams.setSendingTime();
			this.longMessage = true;
		}
	}// End Of Setter

	public SessionParameters getOptionalSessParams() {
		return optionalSessParams;
	}

	public void setOptionalSessParams(SessionParameters optionalSessParams) {
		this.optionalSessParams = optionalSessParams;
	}

	public String getRequiredMsisdn() {

		if (this.usage == CoreEnums.Type.MT && this.msisdn != null)
			return this.msisdn;
		else if (this.usage == CoreEnums.Type.MO && this.cli != null)
			return this.cli;
		else
			return null;

	}// End Of Method

	/** Prevents Multiple Hits */
	public void disableRetry() {
		this.remainingRetries = -1;
		this.reschedule = false;
	}// End Of Method

	public void disableLongSegmentRetry() {
		if (this.optionalLongParams != null) {
			this.optionalLongParams.setRescheduled(false);
		}
	}// End Of Method

	public boolean isMobileTerminating() {
		if (this.usage == CoreEnums.Type.MT)
			return true;
		else
			return false;
	}// End Of Method

	public boolean isSilent() {
		if (this.flag == CoreEnums.SMSFlag.SILENT_MT)
			return true;
		else
			return false;
	}// End Of Method

	public boolean containsHyperlink() {
		if (this.flag == CoreEnums.SMSFlag.URL_LINK)
			return true;
		else
			return false;
	}// End Of Method

	public boolean isFlashSMS() {
		if (this.flag == CoreEnums.SMSFlag.FLASH_SMS)
			return true;
		else
			return false;
	}// End Of Method

	public boolean isUrgentSMS() {
		if (this.flag == CoreEnums.SMSFlag.URGENT_SMS)
			return true;
		else
			return false;
	}// End Of Method

	public boolean setLanguageSpecifications(LanguageSpecification spec) {
		if (spec != null) {
			this.dataCoding = spec.getDataCodingValue();
			this.serviceType = spec.getServiceType();
			this.encoding = spec.getEncoding();
			return true;
		}
		return false;
	}// End Of Method

	public String toString() {

		try {
			String json = CoreUtils.eGSON.toJson(this);
			return json;
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}

		return "{}";

	}// End Of Method

	public static Message parse(String json) {

		try {
			return CoreUtils.GSON.fromJson(json, Message.class);
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, Message.class.getName(), Logger.getStack(e));
		}

		return null;

	}// End Of Method

	// End Of Method

}// End Of Class
