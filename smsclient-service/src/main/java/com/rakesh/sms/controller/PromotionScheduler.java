package com.rakesh.sms.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rakesh.sms.bo.SmsPromoBo;
import com.rakesh.sms.entity.SMSPromotion1;
import com.rakesh.sms.scheduler.JobScheduler;
import com.rakesh.sms.scheduler.StartScheduler;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;


public class PromotionScheduler extends Thread{
	
	@Autowired
	private static SmsPromoBo smspromobo;
	
	static int promotioncount;
	public String promoSchedulerName ; 
	
private static int chunkSize;

private static int promoSchedulerTime;
	
	public int getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}
	
	
	
	public int getPromoSchedulerTime() {
		return promoSchedulerTime;
	}

	public void setPromoSchedulerTime(int promoSchedulerTime) {
		this.promoSchedulerTime= promoSchedulerTime;
	}



	private static Map<String, PromotionTask> tasks;
	
	public PromotionScheduler() {
		promotioncount++; 
		this.promoSchedulerName = "promoScheduler_"+promotioncount;
		tasks = new HashMap<String, PromotionTask>();
	}
	
	public SmsPromoBo getSmspromobo() {
		return smspromobo;
	}

	public void setSmspromobo(SmsPromoBo smspromobo) {
		this.smspromobo = smspromobo;
	}

	public static int getPromotioncount() {
		return promotioncount;
	}

	public static void setPromotioncount(int promotioncount) {
		PromotionScheduler.promotioncount = promotioncount;
	}

	public String getPromoSchedulerName() {
		return promoSchedulerName;
	}

	public void setPromoSchedulerName(String promoSchedulerName) {
		this.promoSchedulerName = promoSchedulerName;
	}

	
	
	public void run() {
		
		while(true) {
			
			String promoName = null;
			try {
				Thread.sleep(1000 * this.promoSchedulerTime);
			} catch (InterruptedException e1) {
				Logger.sysLog(LogValues.info, this.getClass().getName(),"Exception: "+Logger.getStack(e1));
			} 
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Logger.sysLog(LogValues.info, this.getClass().getName(),"promotionScheduler running: date: "+formatter.format(new Date()));	
				
				DateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
				Date today = new Date();

				String currentDateTimeStr = formatter.format(today);
				String currentDateStr = formatter1.format(today);
				
				List<SMSPromotion1> promotions = smspromobo.getSmsPromotion(currentDateTimeStr);
				
				if(promotions!= null) {
					
				Logger.sysLog(LogValues.info, SMSController.class.getName(),"today promotions: "+promotions.size());	
				
				for(int i = 0 ; i<promotions.size() ; i++) {
					
					SMSPromotion1 s = promotions.get(i);
					promoName = s.getPromotionName();
					
					if(!tasks.containsKey(promoName)) {
						PromotionTask te1=new PromotionTask(promoName,promotions.get(i),smspromobo,chunkSize);
						Thread t = new Thread(te1);
						tasks.put(promoName,te1);
						
					}
				}
				for (Map.Entry<String,PromotionTask> entry : tasks.entrySet())  
				{
					
		            promoName = entry.getKey();
		            PromotionTask task = entry.getValue();
		            SMSPromotion1 s = smspromobo.getPromotion(promoName);
		            
					String promoDate = formatter1.format(s.getStartDateTime());
					
					if(promoDate.equals(currentDateStr)) {
						
						String promoDateTime = s.getStartDateTime() + ":00.000";
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						
						Date promodate= sdf.parse(promoDateTime);
						
						long currentInMillis = today.getTime()/1000;
						long promoInMillis = promodate.getTime()/1000;
						
						long timeDiff = Math.abs(promoInMillis-currentInMillis);
						
						Logger.sysLog(LogValues.info, SMSController.class.getName(),"promotion datetime1: "+promodate+" current time: "+today+" Scheduling Promotion task after: "+timeDiff + "s");	
						
						if(s.getStatus().equals("new")&&  timeDiff<=this.promoSchedulerTime) { //timeDiff in second
							
							Timer timer1=new Timer();
							long timeDiffInMillis = timeDiff*1000;
							timer1.schedule(task, timeDiffInMillis);
						}
						/*else if(s.getStatus().equals("running")  && !task.isRunning() ) {
							
							Timer timer2=new Timer();
							s.setStatus("running");
							smspromobo.updateStatus(promoName,"running");
							
							long delay1 = 1;
							timer2.schedule(task, delay1);
						}*/
						
						else if(s.getStatus().equals("proceed")) {
							tasks.remove(promoName);
						}
					}
					Thread.sleep(3);
				}
				}
				
				} catch (ParseException e) {
					smspromobo.updateStatus(promoName,"failed");
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							e.getMessage() + "::  promotion Scheduler Stopped Unexpectedly ");	
				} catch (InterruptedException e) {
					smspromobo.updateStatus(promoName,"failed");
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							e.getMessage() + "::  promotion Scheduler Stopped Unexpectedly ");	
				}
			 catch (Exception e) {
				 smspromobo.updateStatus(promoName,"failed");
				 Logger.sysLog(LogValues.info,this.getClass().getName(),
							e.getMessage() + "::  promotion Scheduler Stopped Unexpectedly ");	
			} 
		}
		
	}
}
