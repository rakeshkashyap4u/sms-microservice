package com.rakesh.sms.beans;

import java.util.Calendar;
import java.util.Date;

import com.rakesh.sms.entity.BlackoutHours;
import com.rakesh.sms.util.CoreUtils;

public class BlackoutHour {

	private Date start, end;

	public BlackoutHour(BlackoutHours entity) {
		this.start = new Date(entity.getBlackout_start().getTime());
		this.end = new Date(entity.getBlackout_end().getTime());
	}// End Of Constructor

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getStart() {
		Calendar now = Calendar.getInstance();

		Calendar cal = Calendar.getInstance();
		cal.setTime(this.start);

		cal.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	public String getFormattedStarttime() {
		return CoreUtils.getTimeStamp(this.getStart());
	}// End Of Method

	public void setEnd(Date end) {
		this.end = end;
	}

	public Date getEnd() {
		Calendar now = Calendar.getInstance();
		if (this.end.before(this.start)) {
			now.add(Calendar.DATE, 1);
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(this.end);

		cal.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	public String getFormattedEndtime() {
		return CoreUtils.getTimeStamp(this.getEnd());
	}// End Of Method

	public String toString() {
		return "BlackoutHour :: Start= " + this.getFormattedStarttime() + "  End= " + this.getFormattedEndtime();
	}// End Of Method

}
