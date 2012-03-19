package it.flaminiandrea.jphonesms.export;

import it.flaminiandrea.jphonesms.domain.SmsBoard;

public interface Exporter {
	public boolean export();
	public SmsBoard getSmsBoard();
	public void setSmsBoard(SmsBoard smsBoard);
	public String getPathToDirectory();
	public void setPathToDirectory(String pathToDirectory);
}
