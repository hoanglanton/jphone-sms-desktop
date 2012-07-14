package it.flaminiandrea.jphonesms.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import it.flaminiandrea.jphonesms.domain.Attachment;
import it.flaminiandrea.jphonesms.domain.ShortMessage;
import it.flaminiandrea.jphonesms.domain.SmsBoard;
import it.flaminiandrea.jphonesms.logger.RuntimeLogger;

public class ShortMessagesTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 5592285361462851066L;
	private SmsBoard smsBoard;
	private static final int DIRECTION = 0;
	private static final int IMESSAGE = 1;
	private static final int NAME = 2;
	private static final int ADDRESS = 3;
	private static final int DATE = 4;
	private static final int TEXT = 5;
	private static final int ATTACHMENTS = 6;

	private List<String> fields;

	public ShortMessagesTableModel(SmsBoard smsBoard) {
		this.smsBoard = smsBoard;
		this.fields = new ArrayList<String>();
		this.fields.add("Direction");
		this.fields.add("iMessage");
		this.fields.add("Name");
		this.fields.add("Address");
		this.fields.add("Date");
		this.fields.add("Text");
		this.fields.add("Attachments");
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
			return this.smsBoard.getEntriesByNameAndByReverseDate().size();
		} catch (java.lang.NullPointerException e) {
			return 0;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		List<ShortMessage> messages = this.smsBoard.getEntriesByNameAndByReverseDate();
		ShortMessage sms = messages.get(rowIndex);
		switch (columnIndex) {
		case DIRECTION:
			return sms.getFlowDescription();
		case IMESSAGE:
			return (sms.isIMessage() ? "YES" : "NO");
		case NAME:
			return sms.getContactName();
		case ADDRESS:
			return sms.getAddress();
		case DATE:
			return sms.getFormattedDate();
		case TEXT:
			return sms.getText();
		case ATTACHMENTS:
			String result = "";
			Vector<Attachment> attachments = sms.getAttachments();
			if (attachments != null && attachments.size() > 0) {
				for (Attachment attachment : attachments) {
					result += attachment.getMimeType()+"; ";
				}
				return result.substring(0, result.length()-1);
			}
			return "NO";
		default:
			return null;
		}
	}

	public SmsBoard getSmsBoard() {
		return this.smsBoard;
	}

	public void setSmsBoard(SmsBoard smsBoard) {
		this.smsBoard = smsBoard;

	}
}
