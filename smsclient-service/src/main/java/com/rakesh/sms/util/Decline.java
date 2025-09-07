package com.rakesh.sms.util;

import java.util.ArrayList;
import java.util.Collections;

public class Decline {

	private int declineRatio;
	
	private int reqcount;
	
	private ArrayList<Integer> rcount=new ArrayList<Integer>();
	
	public static Decline instance;

	private Decline(int declineRatio) {
		super();
		this.declineRatio = declineRatio;
		
		for (int i=1;i<=100;i++)
			rcount.add(i);
		
		Collections.shuffle(rcount);

		//for log only
		{
		StringBuffer rr = new StringBuffer();
		for(int r:rcount)
			rr.append(",").append(r);
		Logger.sysLog(LogValues.info, this.getClass().getName(), "Decline Ratio: "+declineRatio+" Decline Order: "+rr.toString());
		}
	}
	
	public int getDeclineRatio() {
		return declineRatio;
	}

	public void setDeclineRatio(int declineRatio) {
		this.declineRatio = declineRatio;
	}


	public boolean isRejected() {
		boolean rej=false;
		reqcount++;
		
		if(rcount.get(reqcount-1)<=declineRatio) rej=true;
		else rej=false;
		
		if(reqcount>=100) {
			reqcount = 0;
			Collections.shuffle(rcount);
		}
		
		return rej;
	}
	
	public static Decline getInstance(int declineRatio) {
		
		if(instance == null) instance = new Decline(declineRatio);
		
		return instance;
	}
	
}
