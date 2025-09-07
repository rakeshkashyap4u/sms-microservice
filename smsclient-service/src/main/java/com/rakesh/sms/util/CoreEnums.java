package com.rakesh.sms.util;

public class CoreEnums {

	public static enum SMSType {
		Service, // High Priority
		Alert, // Medium Priority
		Promotional, // Least Priority
		UNKNOWN;
		public static final SMSType values[] = values();
	}

	public static enum Protocol {
		SMPP, SOAP, XML, HTTP, UNKNOWN;
		public static final Protocol values[] = values();
	}

	public static enum Type {
		MT, // Mobile Terminated
		MO, // Mobile Originated
		UNKNOWN,
		DR;
		public static final Type values[] = values();
	}

	public static enum AlertType {
		UNKNOWN, NAMAZ, COMBO, SERVICE, FEATURE;
		public static final AlertType values[] = values();
	}

	public static enum SubscriptionStatus {
		NEW, ACTIVE, // 1
		GRACE, PENDING, PARKING, SUSPENDED, DEMO, UNSUB, // 7
		UNKNOWN;
		public static final SubscriptionStatus values[] = values();
	}

	public static enum LanguageScript {
		UNKNOWN, ARABIC, LATIN, HEX;
		public static final LanguageScript values[] = values();
	}

	public static enum SMSFlag {
		UNKNOWN, SILENT_MT, URL_LINK, FLASH_SMS, URGENT_SMS;
		public static final SMSFlag values[] = values();
	}

	public static enum ExpiryUnit {
		UNKNOWN, DATE, DAYS, HOURS, MINS;
		public static final ExpiryUnit values[] = values();
	}

	public static enum HttpMethod {
		GET, POST, DELETE;
		public static final HttpMethod values[] = values();
	}

	public static enum MSNetType {
		GSM, CDMA, TDMA, SMPP, InternalUser1, InternalUser2, SIP, UC, PHS, JJE, PSTN, CDMAWLL, Unknown;
		public static final MSNetType values[] = values();
	}

	public static enum athomeSMPPplus {
		NONE, ADDSUBR, MODSUBR, DELSUBR, ENQSUBR;
		public static final athomeSMPPplus values[] = values();
	}
	
	public static enum userStatus {
		NONE, ADDSUBR, MODSUBR, DELSUBR, ENQSUBR;
		public static final userStatus values[] = values();
	}

	public static enum athomeFuncFlag {
		NONE(0x000), NP(0x001), PPS(0x002), VIP(0x004), BLACKLIST(0x008), SM(0x010), ICC(0x020), NetType(
				0x040), MinAddr(0x080), VPN(0x100), ATHOME(0x800000); // 8388608

		public static final athomeFuncFlag values[] = values();
		private final int ordinal;

		private athomeFuncFlag(int ordinal) {
			this.ordinal = ordinal;
		}// End Of Constructor

		public int getValue() {
			return this.ordinal;
		}
	}// End Of ENUM Function Flag

}// End Of Class
