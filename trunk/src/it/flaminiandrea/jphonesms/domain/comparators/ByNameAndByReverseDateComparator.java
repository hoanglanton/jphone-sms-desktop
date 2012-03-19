package it.flaminiandrea.jphonesms.domain.comparators;

import it.flaminiandrea.jphonesms.domain.ShortMessage;

import java.util.Comparator;

public class ByNameAndByReverseDateComparator implements Comparator<ShortMessage> {

	@Override
	public int compare(ShortMessage sms1, ShortMessage sms2) {
		String name1 = sms1.getContactName();
		String name2 = sms2.getContactName();
		int result = name1.compareToIgnoreCase(name2);
		if (result == 0)
			result = sms2.getDate().compareTo(sms1.getDate());
		return result;
	}

}
