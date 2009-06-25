package it.flaminiandrea.jphonesms.db.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.flaminiandrea.jphonesms.domain.ShortMessage;
import it.flaminiandrea.jphonesms.domain.SmsBoard;


public class QueryFactory {

	public SmsBoard retrieveSmsBoard(String smsDBPath) throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:"+smsDBPath);
		Statement stat = conn.createStatement();
		conn.setAutoCommit(true);
		ResultSet rs = stat.executeQuery("SELECT address, date, text, flags FROM message WHERE text is not null ORDER BY date");

		SmsBoard smsBoard = new SmsBoard();
		while (rs.next()) {
			String address = rs.getString("address");
			String text = rs.getString("text");
			Date date = timeStampToDate(rs.getLong("date"));
			int flags = rs.getInt("flags");
			ShortMessage currentMessage = new ShortMessage(address, date, text, flags);
			smsBoard.addShortMessage(currentMessage);
		}
		conn.close();
		return smsBoard;
	}
	
	public Date timeStampToDate(long timeStampDate) {
		return new Date(timeStampDate*1000);
	}

	public Map<String,String> retrieveMapValueName(String contactsDBPath) throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:"+contactsDBPath);
		Statement stat = conn.createStatement();
		conn.setAutoCommit(true);
		ResultSet rs = stat.executeQuery(
				"SELECT ABPhoneLastFour.value, First, Last, Middle " +
				"FROM ABPhoneLastFour, ABMultiValue, ABPerson " +
				"WHERE ABPerson.ROWID=ABMultiValue.record_id " +
				"AND ABMultiValue.UID=ABPhoneLastFour.multivalue_id");
		Map<String,String> map = new HashMap<String,String>();
		while (rs.next()) {
			String first = rs.getString("First");
			String last = rs.getString("Last");
			String middle = rs.getString("Middle");
			String value = rs.getString("value");
			String name = makeName(first, middle, last);
			map.put(value, name);
		}
		conn.close();
		return map;
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