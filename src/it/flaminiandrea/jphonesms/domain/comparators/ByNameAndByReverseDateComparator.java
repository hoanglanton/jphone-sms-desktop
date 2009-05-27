package it.flaminiandrea.jphonesms.domain.comparators;

import it.flaminiandrea.jphonesms.domain.Entry;

import java.util.Comparator;

public class ByNameAndByReverseDateComparator implements Comparator<Entry> {

	@Override
	public int compare(Entry entry1, Entry entry2) {
		String name1 = entry1.getName();
		String name2 = entry2.getName();
		int result = name1.compareToIgnoreCase(name2);
		if (result == 0)
		result = entry2.getDate().compareTo(entry1.getDate());
		return result;
	}

}
