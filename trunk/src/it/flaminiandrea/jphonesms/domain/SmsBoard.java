package it.flaminiandrea.jphonesms.domain;

import java.util.*;

public class SmsBoard {
	private List<ShortMessage> messages;

	public SmsBoard() {
		this.messages = new ArrayList<ShortMessage>();
	}

	public List<ShortMessage> getMessagesGivenAddress(String address) {
		List<ShortMessage> result = new ArrayList<ShortMessage>();
		for (ShortMessage shortMessage : this.messages) {
			if (shortMessage.getAddress().equals(address)) {
				result.add(shortMessage);
			}
		}
		return result;
	}
	
	public boolean addShortMessage(ShortMessage sms) {
		return this.messages.add(sms);
	}

	public List<ShortMessage> getMessages() {
		return messages;
	}
}
