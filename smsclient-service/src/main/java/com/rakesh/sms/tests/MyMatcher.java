package com.rakesh.sms.tests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rakesh.sms.beans.Response;
import com.rakesh.sms.util.Logger;

public class MyMatcher {

	// private static final String DlvryReportPattern = "^id:(.*) sub:(\\d{1,3})
	// dlvrd:(\\d{1,3}) submit date:(\\d{10}) done date:(\\d{10})
	// stat:([A-Z0-9]{1,10}) err:(\\d{1,3}) text:(.*)";
	// private static final String DlvryReportPattern =
	// "^id:([a-zA-Z0-9]*),(sub:\\d{1,3},)?(dlvrd:\\d{1,3},)?submitDate:(\\d{10}),doneDate:(\\d{10}),stat:([A-Z0-9]{1,10})(,err:\\d{1,3})?(,text:.*)?";
	private static final String DlvryReportPattern = "^id:([a-zA-Z0-9]+) (sub:\\d{1,3} )?(dlvrd:\\d{1,3} )?submitDate:(\\d{10}) doneDate:(\\d{10}) stat:([A-Z0-9]+)( err:\\d{1,3})?( text:.*)?";
	private static Pattern messagePattern;

	public static void main(String[] args) throws Exception {

		String sampleText = "id:Smsc2006 sub:1 dlvrd:1 submit date:1505051216 done date:1508051216 stat:0 err:0 text:Hi howr u";

		sampleText = "id:Smsc2006 sub:2 submit date:1505051216 done date:1508051216 stat:DLV text:Hi howr u";

		MyMatcher.messagePattern = Pattern.compile(DlvryReportPattern);

		Response resp = MyMatcher.parseRegEx(sampleText);

		if (resp != null)
			System.out.println(resp.toString());

	}// End Of Main

	public static Response parseRegEx(String text) throws Exception {

		text = text.replace("submit date", "submitDate").replace("done date", "doneDate");
		Response smscResponse;

		try {

			Matcher matcher = messagePattern.matcher(text);
			String temp = null;
			int group = 1;

			if (matcher.matches()) {

				smscResponse = new Response();
				smscResponse.setId(matcher.group(group++).trim());

				System.out.println(smscResponse.getId());

				temp = matcher.group(group++);

				if (temp != null && temp.startsWith("sub")) {
					smscResponse.setSub(Integer.parseInt(temp.trim().substring(temp.indexOf(':') + 1)));
					temp = null;
				}

				if (temp == null)
					temp = matcher.group(group++);
				if (temp != null && temp.startsWith("dlvrd")) {
					smscResponse.setDlvrd(Integer.parseInt(temp.trim().substring(temp.indexOf(':') + 1)));
					temp = null;
				}

				if (temp == null)
					temp = matcher.group(group++);

				smscResponse.setSubmitDate(Long.parseLong(temp.trim()));
				smscResponse.setDoneDate(Long.parseLong(matcher.group(group++).trim()));
				smscResponse.setStat(matcher.group(group++).trim());

				temp = matcher.group(group++);
				if (temp != null && temp.trim().startsWith("err")) {
					smscResponse.setErr(Integer.parseInt(temp.trim().substring(temp.indexOf(':'))));
					temp = null;
				}

				if (temp == null)
					temp = matcher.group(group++);

				if (temp != null && temp.trim().startsWith("text"))
					smscResponse.setText(temp.trim().substring(temp.indexOf(':')));

			} else
				smscResponse = null;

		} catch (Exception e) {
			System.out.println(
					" Parsing Error | Unexpected SMSC Response Received \n " + text + " \n" + Logger.getStack(e));
			smscResponse = null;
		}

		return smscResponse;

	}// End Of Method

}// End Of Class
