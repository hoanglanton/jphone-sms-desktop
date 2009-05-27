package it.flaminiandrea.jphonesms.domain;


import it.flaminiandrea.jphonesms.domain.comparators.ByNameAndByReverseDateComparator;

import java.util.*;

public class Data {
	List<Entry> entries;

	public Data(SmsBoard smsBoard, ContactsBoard contactsBoard) {
		this.entries = new ArrayList<Entry>();
		createData(smsBoard, contactsBoard);
	}

	private void createData(SmsBoard smsBoard, ContactsBoard contactsBoard) {
		for (ShortMessage shortMessage : smsBoard.getMessages()) {
			String address = shortMessage.getAddress();
			Contact resulContact = contactsBoard.getContactGivenAddressLastNCyphres(address, 7);
			String name;
			if (resulContact == null) {
				name = "Unknown";
			} else {
				name = contactsBoard.getContactGivenAddressLastNCyphres(address, 7).getName();
			}
			Entry newEntry = new Entry(shortMessage, name);
			entries.add(newEntry);
		}
	}

	public List<Entry> getEntriesByNameAndByReverseDate() {
		List<Entry> result = new ArrayList<Entry>(this.entries);
		Comparator<Entry> comparator = new ByNameAndByReverseDateComparator();
		Collections.sort(result, comparator);
		return result;
	}

	public List<Entry> getEntriesBySenderName(String name) {
		List<Entry> result = new ArrayList<Entry>();
		for (Entry entry : this.getEntriesByNameAndByReverseDate()) {
			if (entry.getName().equals(name)) {
				result.add(entry);
			}
		}
		return result;
	}
}
