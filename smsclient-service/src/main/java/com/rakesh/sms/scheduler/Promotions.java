package com.rakesh.sms.scheduler;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.springframework.jms.core.JmsTemplate;

import com.rakesh.sms.beans.Message;
import com.rakesh.sms.beans.SmsPromotion;
import com.rakesh.sms.bo.AlertsBo;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.queue.QueueManager;
import com.rakesh.sms.queue.SmsQueue;
import com.rakesh.sms.util.AppContext;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class Promotions implements JobScheduler {

	private boolean started, paused;
	private AlertsBo alertsboimpl;
	private SmsPromotion details;
	private int uploadCount;
	private Object lock;
	private int id;


	public Promotions(SmsPromotion details) {
		this.alertsboimpl = (AlertsBo) AppContext.getBean("alertsBo");
		this.started = this.paused = false;
		this.lock = new Object();
		this.details = details;
		this.uploadCount = 0;
		this.id = (int) details.getStarttime().getTime() * -1;
	}// End Of Constructor

	public String getName() {
		return this.details.getJobName();
	}// End Of Method

	public int getId() {
		return this.id;
	}// End Of Method

	public int getPushCount() {
		return this.uploadCount;
	}// End Of Method

	public String getStartTime() {
		return CoreUtils.getTimeStamp(this.details.getStarttime());
	}// End Of Method

	public boolean hasStarted() {
		return this.started;
	}// End Of Method

	public boolean isPaused() {
		return this.paused;
	}// End Of Method

	public void run() {

		final int CHUNK_CAPACITY = 2 * QueueManager.TPS * QueueManager.TPS * 60;
		final int CHUNK_SIZE = CHUNK_CAPACITY;


		//For sending promotions as USSD push

			try {

				Logger.sysLog(LogValues.info, this.getClass().getName(), " [" + this.details.getJobName()
				+ "]  Promotion Start Time :: " + CoreUtils.getTimeStamp(this.details.getStarttime()));

				long starttime = this.details.getStarttime().getTime();
				long diff = starttime - System.currentTimeMillis() + 100;

				/**
				 * Note: Do Not Handle the Thread.sleep() in Try-Catch or ELSE Stop promotion
				 * will not work.
				 */
				if (diff >= 1000) {
					Thread.sleep(diff);
				} // End Of Start Sleep

				this.started = true;
				this.details.setCurrentTimestamp();
				this.id = this.alertsboimpl.addPromotionLog(details);

				if (this.details.isSuccessful() && this.started) {

					LanguageSpecification specs = null;
					SmsQueue queue = new SmsQueue();
					String messageContent = null;
					int i = 0, chunkCount = 0;

					if (this.details.getLanguage() != null) {

						specs = CoreUtils.getLanguageSpecifications(this.details.getLanguage());

						if (specs != null)
						{
							Logger.sysLog(LogValues.info, this.getClass().getName(), " [" + this.details.getJobName()
							+ "]  Found optional settings for Language :: " + specs.toString());
							messageContent = this.correctify(this.details.getMessage(), specs.getScript());
						} else {
							Logger.sysLog(LogValues.info, this.getClass().getName(), " [" + this.details.getJobName()
							+ "]  NO Language specification for " + this.details.getLanguage());
						}
					} // End Of Language Specification

					if (messageContent == null) {
						messageContent = this.details.getMessage().trim();
					}
					
					//burkinaFaso
					if(CoreUtils.getProperty("country").equalsIgnoreCase("bfa")) 
					{
						if(CoreUtils.token1 == null || CoreUtils.token2 == null) {
							CoreUtils.generateAndSaveToken();
						}
					}

					for (i = 0; i < details.baseSize() && this.started; i++)
					{

						if (this.paused == true) {
							synchronized (this.lock) {
								Logger.sysLog(LogValues.info, this.getClass().getName(),
										" [" + this.details.getJobName() + "] Paused...");
								this.lock.wait();
							}
						} // End Of Paused Clause

						Message sms = new Message(details.getCallerId(), details.getBase().get(i),
								CoreEnums.SMSType.Promotional, messageContent, details.getProtocol(), CoreEnums.Type.MT);
						sms.setExpiryTime(details.getExpiry());
						sms.setCircle(details.getCircle());
						sms.setExtraDetail(this.getName());
						sms.setFlag(details.getFlag());

						if (details.getServiceid() != null && details.getServiceid().length() > 0)
							sms.setServiceid(details.getServiceid());

						if (sms.setLanguageSpecifications(specs) == false) {
							sms.setEncoding("true");
						} // End Of Language Check
						
						

						queue.push(sms);
						chunkCount++;
						this.uploadCount = i + 1;

						if (chunkCount >= CHUNK_SIZE) {
							Logger.sysLog(LogValues.info, this.getClass().getName(),
									" [" + this.details.getJobName() + "] Chunk of " + CHUNK_SIZE + " Complete :: Pushed "
											+ String.valueOf((int) (i + 1)) + " MSISDN ");
							chunkCount = 0;
							Thread.sleep(60000);
						} // End Of Chunk Check

					} // End Of Loop

				} // End Of Promotion

				StartScheduler.finishJob(this);

			} catch (InterruptedException ie) {
				Logger.sysLog(LogValues.info, this.getClass().getName(), " Promotion [" + this.details.getJobName()
				+ "] Interrupted!!  ::  " + this.uploadCount + " MSISDN Pushed/Processed ");
			} catch (SecurityException se) {
				Logger.sysLog(LogValues.info, this.getClass().getName(), " Promotion [" + this.details.getJobName()
				+ "] Interrupted!!  ::  " + this.uploadCount + " MSISDN Pushed/Processed ");
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" Error during Promotion [" + this.details.getJobName() + "] \n" + Logger.getStack(e));
				StartScheduler.finishJob(this);
			} // End Of Try Catch





		
	}// End Of Thread

	private String correctify(String message, Integer script) {

		String str, content;

		try {

			String encodedContent = URLEncoder.encode(message, "UTF-8");

			if (script == CoreEnums.LanguageScript.ARABIC.ordinal()) {
				content = encodedContent.replaceAll("%C2", "").replaceAll("%C3%8", "%C").replaceAll("%C3%9", "%D")
						.replaceAll("%C3%A", "%E").replaceAll("%C3%B", "%F");
				str = URLDecoder.decode(content, "UTF-8").trim();
			} else if (script == CoreEnums.LanguageScript.LATIN.ordinal()) {
				content = encodedContent.replaceAll("%83%C2", "");
				str = URLDecoder.decode(content, "UTF-8").trim();
			} else
				str = message.trim();

		} catch (Exception e) {
			Logger.sysLog(LogValues.warn, this.getClass().getName(),
					" Promotion [" + this.details.getJobName() + "]  Unable to correct SMS Content ");
			str = message;
		} // End Of Try Catch

		return str;

	}// End Of Method

	public void resume() {
		this.paused = false;

		try {
			synchronized (this.lock) {
				this.lock.notify();
			}
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" [" + this.details.getJobName() + "] ERROR Resuming Promotion!!! " + e.getMessage());
		} // End Of Try Catch
	}// End Of Method

	public void pause() {
		this.paused = true;
	}// End Of Method

	public void end() {
		this.started = false;
	}// End Of Method

}
