package com.rakesh.sms.util;

import java.io.StringReader;


import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;

public class XmlParser {

	private static Unmarshaller unmarshaller;

	@SuppressWarnings("unchecked")
	public static Object parseXml(String xml, Object obj) {

		Object object = null;
		try {

			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			unmarshaller = context.createUnmarshaller();
			JAXBElement<Object> root = (JAXBElement<Object>) unmarshaller
					.unmarshal(new StreamSource(new StringReader(xml.toString())), obj.getClass());
			object = root.getValue();

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, XmlParser.class.getName(), Logger.getStack(e));
		} // End Of Try-Catch

		return object;

	}// End Of Method

}// End Of Class
