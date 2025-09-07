package com.rakesh.sms.beans;

import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameters;

public class LongMessageParameters {

	private int sarSequenceNumber, sarTotalSegments, segmentIndex;
	private short messageReferenceNumber;
	private boolean rescheduled;
	private long sendingTime;

	public LongMessageParameters() {
		this.rescheduled = true;
		this.sendingTime = 0L;
	}// End Of Constructor

	public int getSarSequenceNumber() {
		return sarSequenceNumber;
	}

	public void setSarSequenceNumber(int sarSequenceNumber) {
		this.sarSequenceNumber = sarSequenceNumber;
	}

	public int getSarTotalSegments() {
		return sarTotalSegments;
	}

	public void setSarTotalSegments(int sarTotalSegments) {
		this.sarTotalSegments = sarTotalSegments;
	}

	public int getSegmentIndex() {
		return segmentIndex;
	}

	public void setSegmentIndex(int segmentIndex) {
		this.segmentIndex = segmentIndex;
	}

	public short getMessageReferenceNumber() {
		return messageReferenceNumber;
	}

	public void setMessageReferenceNumber(short messageReferenceNumber) {
		this.messageReferenceNumber = messageReferenceNumber;
	}

	public long getSendingTime() {
		return sendingTime;
	}

	public void setSendingTime() {
		this.sendingTime = System.currentTimeMillis();
	}

	public boolean isRescheduled() {
		return rescheduled;
	}

	public void setRescheduled(boolean rescheduled) {
		this.rescheduled = rescheduled;
	}

	public OptionalParameter getSarTotalSegmentsObject() {
		return OptionalParameters.newSarTotalSegments(this.sarTotalSegments);
	}

	public OptionalParameter getSarSequenceNumberObject() {
		return OptionalParameters.newSarSegmentSeqnum(this.sarSequenceNumber);
	}

	public OptionalParameter getMessageReferenceNumberObject() {
		return OptionalParameters.newSarMsgRefNum(this.messageReferenceNumber);
	}

}// End Of Class
