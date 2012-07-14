package it.flaminiandrea.jphonesms.db.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;
import java.util.Vector;

import it.flaminiandrea.jphonesms.costants.DatabaseConstants;
import it.flaminiandrea.jphonesms.domain.Attachment;
import it.flaminiandrea.jphonesms.domain.ShortMessage;
import it.flaminiandrea.jphonesms.domain.SmsBoard;


public class QueryFactory {

	private static final String LABEL_ADDRESS_NULL = "NO ADDRESS";
	private static final String LABEL_CONTACTNAME_NULL = "NO CONTACT NAME";

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
				"FROM message");

		SmsBoard smsBoard = new SmsBoard();
		while (rs.next()) {
			int isMadrid;
			try {
				isMadrid = rs.getInt("is_madrid");
			} catch (SQLException e) {
				isMadrid = DatabaseConstants.IS_NOT_MADRID;
				//TODO LOGGER
			}
			if (isMadrid == DatabaseConstants.IS_NOT_MADRID) {
				boolean isSent = true;
				int messageId = rs.getInt("ROWID");
				String address = rs.getString("address");
				String text = rs.getString("text");
				String timestamp = rs.getString("date");
				Date date = timeStampToDate(timestamp);
				int flags = rs.getInt("flags");
				if (flags == 2) { 
					isSent = false;
				}
				Vector<Attachment> attachments = retrieveAttachmentsByMessageId(messageId);
				Vector<Attachment> mediaAttachments = new Vector<Attachment>();
				for (Attachment attachment: attachments) {
					if (attachment.getMimeType() != null && attachment.getMimeType().equalsIgnoreCase("text/plain")) {
						if (text == null) {
							text = attachment.getContent();
						} else {
							text += " "+attachment.getContent();
						}
					} else if (attachment.getMimeType() != null && (!attachment.getMimeType().equalsIgnoreCase("application/smil") && attachment.getMobilePath() != null)) {
						mediaAttachments.add(attachment);
					}
				}
				createShortMessage(smsBoard, isSent, address, text, date, mediaAttachments);
			} else if (isMadrid == DatabaseConstants.IS_MADRID) {
				boolean isSent = true;
				String address = rs.getString("madrid_handle");
				String madrid_attachment = rs.getString("madrid_attachmentInfo");
				String text = rs.getString("text");
				if (text == null) {
					text = "";
				}
				String timestamp = "1"+rs.getString("date");
				Date date = timeStampToDate(String.valueOf(Long.parseLong((timestamp)) - (251*24*60*60)));
				int flags = rs.getInt("madrid_flags");
				if (flags == 12289) { 
					isSent = false;
				}
				Vector<Attachment> attachments = new Vector<Attachment>();
				if (madrid_attachment != null) {
					attachments = retrieveMadridAttachments(timestamp, isSent);
				}
				createShortMessage(smsBoard, isSent, address, text, date, attachments);
			}
		}
		conn.close();
		return smsBoard;
	}

	private void createShortMessage(SmsBoard smsBoard, boolean isSent,
			String address, String text, Date date,
			Vector<Attachment> attachments) throws Exception {
		String contactName = LABEL_CONTACTNAME_NULL;
		if (address != null) {
			contactName = retrieveContactNameByAddress(address);
		} else {
			address = LABEL_ADDRESS_NULL;
		}
		if(text != null || attachments.size() > 0) {
			ShortMessage currentMessage = new ShortMessage(address,
					date, text, isSent, false, contactName,
					attachments);
			smsBoard.addShortMessage(currentMessage);
		}
	}

	private Vector<Attachment> retrieveMadridAttachments(String timestamp, boolean isSent) throws SQLException, ClassNotFoundException {
		Vector<Attachment> attachments = new Vector<Attachment>();
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:"+smsDBPath);
		Statement stat = conn.createStatement();
		conn.setAutoCommit(true);

		//		String querySr = "SELECT * " +
		//				"FROM madrid_attachment " +
		//				// "WHERE created_date LIKE '" + timestamp.substring(1, 5) + "%'");
		//				//"WHERE (( " + timestamp.substring(1) + " - created_date) < " + FromBackupConstants.DATE_ACCEPTABLE_RANGE_UPPER_BOUND+") " +
		//				//"AND (( " + timestamp.substring(1) + " - created_date) > " + FromBackupConstants.DATE_ACCEPTABLE_RANGE_LOWER_BOUND+")";
		//				"WHERE (( " + timestamp.substring(1) + " - created_date) BETWEEN " + FromBackupConstants.DATE_ACCEPTABLE_RANGE_LOWER_BOUND+" AND "+ FromBackupConstants.DATE_ACCEPTABLE_RANGE_UPPER_BOUND + ")";

		String querySr = "SELECT " +
				"*, " +
				"abs(M.date - MA.created_date) AS ABS_DIFF, " +
				"(M.date - MA.created_date) AS DIFF " +
				"FROM madrid_attachment MA, message M " +
				"WHERE M.is_madrid = 1 " +
				"AND M.madrid_attachmentInfo IS NOT NULL " +
				"AND ABS_DIFF < " + DatabaseConstants.DATE_ACCEPTABLE_RANGE_UPPER_BOUND + " " +
				"AND M.date = " + timestamp.substring(1);

		ResultSet rs = stat.executeQuery(querySr);
		while (rs.next()) {
			String backupDirectory = smsDBPath.substring(0, smsDBPath.lastIndexOf(System.getProperty("file.separator")));
			String mobilePath = rs.getString("filename");
			String mimeType = rs.getString("mime_type");
			Attachment currentAttachment = new Attachment(mobilePath, backupDirectory);
			currentAttachment.setMimeType(mimeType);
			currentAttachment.setContent("");
			Long timestampDiff = rs.getLong("DIFF");

			if( (isSent && timestampDiff > 0) || (!isSent && timestampDiff < 0 )) {
				attachments.add(currentAttachment);
			}
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
		ResultSet rs = stat.executeQuery("SELECT M.group_id, MP.* " +
				"FROM msg_pieces MP, message M " +
				"WHERE MP.message_id = M.ROWID " +
				"AND MP.message_id = " + messageId);
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

	public Date timeStampToDate(String timestamp) {
		return new Date(Long.parseLong(timestamp)*1000);
	}

	public String retrieveContactNameByAddressLastFourDigits(String address) throws Exception {
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

	public String retrieveContactNameByAddress(String address) throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:"+contactsDBPath);
		Statement stat = conn.createStatement();
		conn.setAutoCommit(true);
		ResultSet rs = stat.executeQuery(
				"SELECT ABMultiValue.value, ABPerson.First, ABPerson.Last, ABPerson.Middle " +
						"FROM ABMultiValue, ABPerson " +
						"WHERE ABPerson.ROWID=ABMultiValue.record_id " +
						"AND ABMultiValue.value = '" + address + "'" );
		String name = address;
		if (rs.next()) {
			String first = rs.getString("First");
			String last = rs.getString("Last");
			String middle = rs.getString("Middle");
			name = makeName(first, middle, last);
		}
		if (name.equalsIgnoreCase(address)) {
			name = retrieveContactNameByAddressLastFourDigits(address); 
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