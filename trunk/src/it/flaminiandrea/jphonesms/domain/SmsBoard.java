package it.flaminiandrea.jphonesms.domain;

import it.flaminiandrea.jphonesms.domain.comparators.ByNameAndByReverseDateComparator;

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
	
	public List<ShortMessage> getEntriesByNameAndByReverseDate() {
		List<ShortMessage> result = new ArrayList<ShortMessage>(this.messages);
		Comparator<ShortMessage> comparator = new ByNameAndByReverseDateComparator();
		Collections.sort(result, comparator);
		return result;
	}

	public List<ShortMessage> getEntriesBySenderName(String name) {
		List<ShortMessage> result = new ArrayList<ShortMessage>();
		for (ShortMessage entry : this.getEntriesByNameAndByReverseDate()) {
			if (entry.getContactName().equals(name)) {
				result.add(entry);
			}
		}
		return result;
	}
}
