package com.rakesh.sms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.rakesh.sms.beans.Message;
import com.rakesh.sms.bo.SmsPromoBo;
import com.bng.sms.queue.SmsQueue;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.entity.PromotionMsisdn1;
import com.rakesh.sms.entity.SMSPromotion1;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;
import com.rakesh.sms.util.CoreEnums.Protocol;

public class PromotionTask extends TimerTask{

	private SmsPromoBo smspromobo;
	private Object lock;
	private String name ;
	private SMSPromotion1 sp;
	private boolean isRunning;

	private int chunkSize;

	public PromotionTask(String n,SMSPromotion1 smspromo, SmsPromoBo smsPromoBo, int chunkSize){
		this.name=n;
		this.sp = smspromo;
		this.smspromobo = smsPromoBo;
		this.isRunning = false;
		this.lock = new Object();
		this.chunkSize = chunkSize;
		Logger.sysLog(LogValues.info, this.getClass().getName(),
				Thread.currentThread().getName()+" promotion task created");

	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SMSPromotion1 getSp() {
		return sp;
	}
	public void setSp(SMSPromotion1 sp) {
		this.sp = sp;
	}
	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}


	public Object getLock() {
		return lock;
	}
	public void setLock(Object lock) {
		this.lock = lock;
	}

	@Override
	public void run() {

		String spStatus = "failed";
		this.isRunning = true;

		Logger.sysLog(LogValues.info, this.getClass().getName(),
				Thread.currentThread().getName()+" "+name+" the task getting executing...."+ new Date());

		String promoName = sp.getPromotionName();
		long promoId = sp.getId();
		CoreEnums.Protocol mode = CoreUtils.getProtocol();
		LanguageSpecification spec = CoreUtils.getLanguageSpecifications(sp.getLanguage());

		String serviceName = sp.getService();
		List<String> msisdns = new ArrayList<>();

		if(sp.getStatus().equals("new") && serviceName!=null && serviceName.length()>0) {

			smspromobo.updateStatus(promoName,"running");
			sp.setStatus("running");

			Date msgFromDate = sp.getMessageFrom();
			Date msgToDate = sp.getMessageTo();
			int mostActive = sp.getMostActive();

			String smsPromotionType = CoreUtils.getProperty("smsPromotionType");

			if(smsPromotionType!= null && smsPromotionType.length() > 0) {
				msisdns = smspromobo.getActiveUser(sp.getService(),smsPromotionType,null,null);
			}
			else if(smsPromotionType==null || smsPromotionType.length()==0){
				if(mostActive<=0)
					msisdns = smspromobo.getActiveUser(sp.getService(),sp.getUserStatus(),msgFromDate,msgToDate);
				else
					msisdns = smspromobo.getMostActiveUser(mostActive);

			}

			else {
				Logger.sysLog(LogValues.error, this.getClass().getName(),"msisdns null for promoId: "+promoId + "promotionName: "+promoName);
				return;
			}

			if(msisdns != null)	{
				if(smspromobo.insertServiceMsisdns(sp,promoName,promoId, msisdns))
					Logger.sysLog(LogValues.info, this.getClass().getName(),"service task msisdns pushed in promoMsisdn with id: "+promoId + "promotionName: "+promoName);
				else {
					Logger.sysLog(LogValues.error, this.getClass().getName(),"error while pushing msisdns for promoId: "+promoId + "promotionName: "+promoName);
					return;
				}
			}
		}


		List<PromotionMsisdn1> promoMsisdns = smspromobo.getPromoMsisdn(promoName);
		List<String> pmsisdns = new ArrayList<>();
		for(int i = 0 ; i<promoMsisdns.size() ; i++)
			pmsisdns.add(promoMsisdns.get(i).getMsisdn());

		Logger.sysLog(LogValues.info, this.getClass().getName(),"promotion task executing with "+ "promotionName: "+promoName);



		int count =0 ;
		int msisdnSize = pmsisdns.size();
		int extrachunk = msisdnSize%chunkSize;
		int chunkNo = msisdnSize/chunkSize;
		int k =0;
		int i ;
		boolean flag = false;
		List<String> ms = new ArrayList<>(); 

		if(pmsisdns!= null && !pmsisdns.isEmpty()) {
			for(i = 0 ; i<pmsisdns.size() ; i++) {

				int j = i%3;
				String codes[] = CoreUtils.getProperty("countryCodes").split(",");
				String msisdn = CoreUtils.stripCodes(pmsisdns.get(i));
				msisdn = codes[0]+msisdn;
				Message msg = new Message(sp.getCallerId(), msisdn , j,
						sp.getMsgText(), mode.ordinal(), CoreEnums.Type.MT.ordinal());

				msg.setLanguageSpecifications(spec);
				msg.setCircle(sp.getCircle());
				SmsQueue queue = new SmsQueue();


				flag = queue.push(msg);
				if(flag) {

					if(++count<chunkSize) {
						ms.add(msisdn);
					}

					if(count==chunkSize || count == msisdnSize) {
						if(count==chunkSize && k<chunkNo) {
							k++;
							smspromobo.updatePromoMsisdnStatus(promoName, ms, "proceed",count);
						}
						else if( k == chunkNo && extrachunk>0) {
							smspromobo.updatePromoMsisdnStatus(promoName, ms, "proceed",extrachunk);
						}
						count = 0;
						ms.clear();
					}
					Logger.sysLog(LogValues.info, this.getClass().getName(),"promotion messages pushed");
				}
				else if(!flag && count>0) {
					if(serviceName!=null)
						smspromobo.updateSubscribedUserStatus(promoName, ms, "proceed",count);
					else
						smspromobo.updatePromoMsisdnStatus(promoName, ms, "proceed",count);
					count=0;
					k++;
					ms.clear();
				}

			}
			if(i==msisdnSize) {
				spStatus = "proceed";
			}
			else if(i>0 && i<msisdnSize) {
				spStatus = "running";
				this.isRunning = false;
			}
			else if(i==0) {
				spStatus = "new";
			}
			else {
				this.isRunning = false;
				spStatus = "failed";
			}

		}
		else {
			spStatus = "failed";
			this.isRunning = false;
			Logger.sysLog(LogValues.error, this.getClass().getName(),"no user found for promotion: "+promoName);
		}



		try {
			sp.setStatus(spStatus);
			smspromobo.updateStatus(promoName,spStatus);
			Thread.sleep(3);
		} catch (InterruptedException e) {
			this.isRunning = false;
			sp.setStatus("failed");
			smspromobo.updateStatus(promoName,spStatus);
			Logger.sysLog(LogValues.info,this.getClass().getName(),
					e.getMessage() + "::  promotion Scheduler Stopped Unexpectedly ");	
		}catch(Exception e) {
			this.isRunning = false;
			sp.setStatus("failed");
			smspromobo.updateStatus(promoName,spStatus);
			Logger.sysLog(LogValues.info,this.getClass().getName(),
					e.getMessage() + "::  promotion Scheduler Stopped Unexpectedly ");	
		}
	}

}
