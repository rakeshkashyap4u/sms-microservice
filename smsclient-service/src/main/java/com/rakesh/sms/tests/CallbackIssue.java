package com.rakesh.sms.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;

public class CallbackIssue {

	/*
	 * private static Unmarshaller marshallerR;
	 * 
	 * public static void main(String args[]) throws Exception {
	 * 
	 * JAXBContext rContext = JAXBContext.newInstance(ReceivedSmsBean.class);
	 * marshallerR = rContext.createUnmarshaller();
	 * 
	 * File dir = new File("/home/siddhart/Desktop/ZainJordan/CDR/DR/"); File[] Drs
	 * = dir.listFiles();
	 * 
	 * 
	 * for( int i=0; i<Drs.length; i++ ) {
	 * 
	 * File current = Drs[i]; ReceivedSmsBean bean =
	 * (ReceivedSmsBean)marshallerR.unmarshal(current); String val =
	 * bean.getMessageId() +" "+ bean.getSender() +" "+ bean.getStatus() +" "+
	 * bean.getTime() +"\n"; System.out.println(val); // all.append();
	 * 
	 * }//End Of Loop
	 * 
	 * // System.out.println(all.toString());
	 * 
	 * }//End Of Main
	 */
	public static void main(String args[]) throws Exception {

		File file = new File("/home/siddhart/Desktop/renewalFailure.txt");
		FileReader reader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(reader);
		String msisdn = null;
		long msi;
		HashMap<Long, Long> map = new HashMap<Long, Long>();

		do {

			msisdn = buffer.readLine();
			if (msisdn != null) {
				try {
					msi = Long.parseLong(msisdn);
					map.put(msi, msi);
				} catch (Exception e) {
					// System.err.println("ERR");
					msi = 0;
				}
			}

		} while (msisdn != null);

		buffer.close();

		Iterator<Long> iter = map.keySet().iterator();

		while (iter.hasNext()) {
			System.out.println(iter.next());
		}

	}// End Of Main

}
