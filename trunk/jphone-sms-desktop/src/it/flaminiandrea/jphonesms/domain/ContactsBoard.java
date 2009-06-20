package it.flaminiandrea.jphonesms.domain;

import java.util.*;

public class ContactsBoard {
	private List<Contact> contacts;

	public ContactsBoard() {
		this.contacts = new ArrayList<Contact>();
	}

	public boolean addContact(Contact contact) {
		return this.contacts.add(contact);
	}

	public Contact getContactGivenAddress(String address) {
		for (Contact currentContact : this.contacts) {
			if (currentContact.getAddress().equals(address)) {
				return currentContact;
			}
		}
		return null;
	}

	public Contact getContactGivenAddressLastNCyphres(String address, int n) {
		for (Contact currentContact : this.contacts) {
			String currentAddress = currentContact.getAddress();
			if (!currentAddress.startsWith("+")) {
				currentAddress = "+39" + currentAddress;
			}
			String smallGivenAddress;
			String smallCurrentAddress;
			try {
				smallGivenAddress = address.replaceAll(" ", "").substring(address.length()-(n+1));
				smallCurrentAddress = currentAddress.replaceAll(" ", "").substring(address.length()-(n+1));
			} catch (StringIndexOutOfBoundsException e) {
				return null;
			}
			if (smallGivenAddress.equals(smallCurrentAddress)) {
				return currentContact;
			}
		}
		return null;
	}
}
