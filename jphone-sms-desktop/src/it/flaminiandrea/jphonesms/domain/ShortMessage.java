package it.flaminiandrea.jphonesms.domain;

import java.util.Date;

public class ShortMessage { 
	private String text;
	private String address;
	private int flags;
	private Date date;

	public ShortMessage(String address, Date date, String text, int flags) {
		super();
		this.text = text;
		this.address = address;
		this.flags = flags;
		this.date = date;
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


}
