package it.flaminiandrea.jphonesms.domain;

public class Contact {
	private String address;
	private String name;
	
	public Contact(String address, String name) {
		super();
		this.address = this.formatAddress(address);
		this.name = name;
	}

	private String formatAddress(String address) {
		return address.replaceAll(" ", "");
	}

	public String getAddress() {
		return address;
	}

	public String getName() {
		return name;
	}
}
