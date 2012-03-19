package it.flaminiandrea.jphonesms.domain;

import it.flaminiandrea.jphonesms.util.encryption.SHA1Encrypter;

public class Attachment {

	private String mobilePath;
	private String backupDirectoryPath;
	private String backupPath;
	private String mimeType;
	private String content;

	public Attachment(String mobilePath, String backupDirectoryPath) {
		this.setMobilePath(mobilePath);
		this.setBackupDirectoryPath(backupDirectoryPath);
		setBackupPath(this.backupDirectoryPath+System.getProperty("file.separator")+retrieveBackupPathByMobilePath());
	}

	public String retrieveBackupPathByMobilePath() {
		String result = "";
		if (getMobilePath() != null && !getMobilePath().equalsIgnoreCase("")) {
			String truncatedMobilePath = "MediaDomain-" +
					getMobilePath().replaceFirst("/var/mobile/", "");
			result = SHA1Encrypter.SHA1(truncatedMobilePath);
		}
		return result;
	}

	public String getMobilePath() {
		return mobilePath;
	}

	public void setMobilePath(String mobilePath) {
		this.mobilePath = mobilePath;
	}

	public String getBackupPath() {
		return backupPath;
	}

	public void setBackupPath(String backupPath) {
		this.backupPath = backupPath;
	}

	public String getBackupDirectoryPath() {
		return backupDirectoryPath;
	}

	public void setBackupDirectoryPath(String backupDirectoryPath) {
		this.backupDirectoryPath = backupDirectoryPath;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
