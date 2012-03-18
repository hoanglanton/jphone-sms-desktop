package it.flaminiandrea.jphonesms.domain;


import it.flaminiandrea.jphonesms.domain.comparators.ByNameAndByReverseDateComparator;

import java.util.*;

public class Data {
	List<Entry> entries;

	public Data(SmsBoard smsBoard, Map<String, String> contactsMap) {
		this.entries = new ArrayList<Entry>();
		for (ShortMessage shortMessage : smsBoard.getMessages()) {
			String address = shortMessage.getAddress();
			String key = retrieveKey(address);
			String name = contactsMap.get(key);
			if (name == null) {
				name = address;
			} 
			Entry newEntry = new Entry(shortMessage, name);
			entries.add(newEntry);
		}
	}

	private String retrieveKey(String address) {
		String key = address;
		if (address.length() > 4) {
			key = address.substring(address.length()-4);
		}
		return key;
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
