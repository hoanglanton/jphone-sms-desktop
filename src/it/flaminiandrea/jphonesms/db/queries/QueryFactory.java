package it.flaminiandrea.jphonesms.db.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;
import java.util.Vector;

import it.flaminiandrea.jphonesms.costants.FromBackupConstants;
import it.flaminiandrea.jphonesms.domain.Attachment;
import it.flaminiandrea.jphonesms.domain.ShortMessage;
import it.flaminiandrea.jphonesms.domain.SmsBoard;


public class QueryFactory {

	private String smsDBPath, contactsDBPath;

	public QueryFactory(String smsDBPath, String contactsDBPath) {
		this.smsDBPath = smsDBPath;
		this.contactsDBPath = contactsDBPath;
	}

	public SmsBoard retrieveSmsBoard() throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:"+smsDBPath);
		Statement stat = conn.createStatement();
		conn.setAutoCommit(true);
		ResultSet rs = stat.executeQuery("SELECT * " +
				"FROM message " +
				"WHERE text is not null OR is_madrid <> 1 " +
				"ORDER BY date");

		SmsBoard smsBoard = new SmsBoard();
		while (rs.next()) {

			int messageId = rs.getInt("ROWID");
			int isMadrid = rs.getInt("is_madrid");
			ShortMessage currentMessage = null;
			String address = null;
			String text = null;
			Date date = null;
			String contactName = null;
			Vector<Attachment> attachments = null;
			int flags = -1;
			boolean isSent = true;

			switch (isMadrid) {
			case FromBackupConstants.IS_NOT_MADRID:

				address = rs.getString("address");
				text = rs.getString("text");
				date = timeStampToDate(rs.getLong("date"));
				flags = rs.getInt("flags");
				if (flags == 2) { 
					isSent = false;
				}
				attachments = retrieveAttachmentsByMessageId(messageId);
				contactName = retrieveContactNameByPhoneAddress(address);
				currentMessage = new ShortMessage(address, date, text, isSent, false, contactName, attachments);
				break;

			case FromBackupConstants.IS_MADRID:

				address = rs.getString("madrid_handle");
				text = rs.getString("text");
				date = timeStampToDate(rs.getLong("date"));
				flags = rs.getInt("madrid_flags");
				if (flags == 12289) { 
					isSent = false;
				}
				attachments = retrieveMadridAttachments(rs.getString("date"));
				contactName = retrieveContactNameByPhoneAddress(address);
				currentMessage = new ShortMessage(address, date, text, isSent, true, contactName, attachments);
				break;

			default:
				break;
			}
			smsBoard.addShortMessage(currentMessage);
		}
		conn.close();
		return smsBoard;
	}

	private Vector<Attachment> retrieveMadridAttachments(String date) throws SQLException, ClassNotFoundException {
		Vector<Attachment> attachments = new Vector<Attachment>();
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:"+smsDBPath);
		Statement stat = conn.createStatement();
		conn.setAutoCommit(true);
		ResultSet rs = stat.executeQuery("SELECT * " +
				"FROM madrid_attachment " +
				"WHERE created_date LIKE '" + date.substring(0, 4) + "%'");
		while (rs.next()) {
			String backupDirectory = smsDBPath.substring(0, smsDBPath.lastIndexOf(System.getProperty("file.separator")));
			String mobilePath = rs.getString("filename");
			String mimeType = rs.getString("mime_type");
			Attachment currentAttachment = new Attachment(mobilePath, backupDirectory);
			currentAttachment.setMimeType(mimeType);
			currentAttachment.setContent("");
			attachments.add(currentAttachment);
		}
		return attachments;
	}

	private Vector<Attachment> retrieveAttachmentsByMessageId(int messageId) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Vector<Attachment> attachments = new Vector<Attachment>();
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:"+smsDBPath);
		Statement stat = conn.createStatement();
		conn.setAutoCommit(true);
		ResultSet rs = stat.executeQuery("SELECT * " +
				"FROM msg_pieces " +
				"WHERE message_id = " + messageId);
		while (rs.next()) {
			String backupDirectory = smsDBPath.substring(0, smsDBPath.lastIndexOf(System.getProperty("file.separator")));
			String mobilePath = retrieveMobilePathFromContentLoc(rs.getString("content_loc"));
			String mimeType = rs.getString("content_type");
			Attachment currentAttachment = new Attachment(mobilePath, backupDirectory);
			currentAttachment.setMimeType(mimeType);
			currentAttachment.setContent(rs.getString("data"));
			attachments.add(currentAttachment);
		}
		return attachments;
	}

	private String retrieveMobilePathFromContentLoc(String contentLoc) {
		// TODO Auto-generated method stub
		String result = "/var/mobile/asdjhkakjhhdakhsjakhdj";
		return result;
	}

	public Date timeStampToDate(long timeStampDate) {
		return new Date(timeStampDate*1000);
	}

	public String retrieveContactNameByPhoneAddress(String address) throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:"+contactsDBPath);
		Statement stat = conn.createStatement();
		conn.setAutoCommit(true);
		String addressSubString = address;
		if (address.length() > 4 ) {
			addressSubString = address.substring(address.length()-4);
		}
		ResultSet rs = stat.executeQuery(
				"SELECT ABPhoneLastFour.value, First, Last, Middle " +
						"FROM ABPhoneLastFour, ABMultiValue, ABPerson " +
						"WHERE ABPerson.ROWID=ABMultiValue.record_id " +
						"AND ABMultiValue.UID=ABPhoneLastFour.multivalue_id " +
						"AND ABPhoneLastFour.value = '" + addressSubString + "'" );
		String name = address;
		if (rs.next()) {
			String first = rs.getString("First");
			String last = rs.getString("Last");
			String middle = rs.getString("Middle");
			name = makeName(first, middle, last);
		}
		conn.close();
		return name;
	}

	private String makeName(String first, String middle, String last) {
		String result = "";
		if (!(first == null || first.equals(""))) {
			result+=first;
		}
		if (!(middle == null || middle.equals(""))) {
			result+=" \""+middle+"\"";
		}
		if (!(last == null || last.equals(""))) {
			result+=" "+last;
		}
		if (result.equals("")) {
			return "Unknown";
		} else {
			return result;
		}
	}
}