package com.rakesh.sms.tests;

import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.MessageMode;
import org.jsmpp.bean.MessageType;

public class UnicodeTest {

	public static void main(String args[]) {

		/*
		 * String str =
		 * "%D9%85%D9%88%D8%B9%D8%AF%20%D8%A3%D8%B0%D8%A7%D9%86%20%D8%A7%D9%84%D9%81%D8%AC%D8%B1%20%D9%84%D9%8A%D9%88%D9%85%20%D8%BA%D8%AF%20%D9%81%D9%8A%20%D8%AA%D9%85%D8%A7%D9%85%20%D8%A7%D9%84%D8%B3%D8%A7%D8%B9%D8%A9%204:00%20%D8%B5%D8%A8%D8%A7%D8%AD%D8%A7%D9%8B";
		 * String arabic = "موعد أذان الفجر ليوم غد في تمام الساعة 4:00 صباحاً";
		 * 
		 * String x = "موعد+أذان+الفجر+ليوم+غد+في+تمام+الساعة+4%3A00+صباحاً";
		 */

		// GeneralDataCoding dataCoding = new
		// GeneralDataCoding(Alphabet.parseDataCoding((byte)256), null, false );
		// DataCoding dataCoding = new RawDataCoding((byte)192);
		// System.out.println(dataCoding);

		ESMClass e = new ESMClass();
		e.setMessageMode(MessageMode.DEFAULT);
		e.setMessageType(MessageType.DEFAULT);
		e.setSpecificFeature(GSMSpecificFeature.UDHI);

	}

}
