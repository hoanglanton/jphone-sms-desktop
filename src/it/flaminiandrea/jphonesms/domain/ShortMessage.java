package it.flaminiandrea.jphonesms.domain;

import java.util.Date;

public class ShortMessage { 
	private String text;
	private String address;
	private int flags;
	private Date date;
	private boolean isIMessage;

	public ShortMessage(String address, Date date, String text, int flags, boolean isIMessage) {
		super();
		this.text = text;
		this.address = address;
		this.flags = flags;
		this.date = date;
		this.setIMessage(isIMessage);
	}

	public String getText() {
		return text;
	}

	public String getAddress() {
		return address;
	}

	public int getFlags() {
		return flags;
	}

	public Date getDate() {
		return date;
	}

	public boolean isIMessage() {
		return isIMessage;
	}

	public void setIMessage(boolean isIMessage) {
		this.isIMessage = isIMessage;
	}


}
