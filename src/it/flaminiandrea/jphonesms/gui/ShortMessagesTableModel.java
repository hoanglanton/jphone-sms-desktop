package it.flaminiandrea.jphonesms.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import it.flaminiandrea.jphonesms.domain.Data;
import it.flaminiandrea.jphonesms.domain.Entry;

public class ShortMessagesTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 5592285361462851066L;
	private Data smsData;
	private static final int DIRECTION = 0;
	private static final int NAME = 1;
	private static final int ADDRESS = 2;
	private static final int DATE = 3;
	private static final int TEXT = 4;

	private List<String> fields;

	public ShortMessagesTableModel(Data smsData) {
		this.smsData = smsData;
		this.fields = new ArrayList<String>();
		this.fields.add("Direction");
		this.fields.add("Name");
		this.fields.add("Address");
		this.fields.add("Date");
		this.fields.add("Text");
	}

	@Override
	public String getColumnName(int column) {
		return this.fields.get(column);
	}

	@Override
	public int getColumnCount() {
		return this.fields.size();
	}

	@Override
	public int getRowCount() {
		try {
			return this.smsData.getEntriesByNameAndByReverseDate().size();
		} catch (java.lang.NullPointerException e) {
			return 0;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		List<Entry> entries = this.smsData.getEntriesByNameAndByReverseDate();
		Entry entry = entries.get(rowIndex);
		switch (columnIndex) {
		case DIRECTION:
			return entry.getFlags();
		case NAME:
			return entry.getName();
		case ADDRESS:
			return entry.getAddress();
		case DATE:
			return entry.getFormattedDate();
		case TEXT:
			return entry.getText();
		default:
			return null;
		}
	}

	public Data getSmsData() {
		return smsData;
	}

	public void setSmsData(Data smsData) {
		this.smsData = smsData;
	}
}
