package it.flaminiandrea.jphonesms.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import it.flaminiandrea.jphonesms.costants.FromBackupConstants;
import it.flaminiandrea.jphonesms.db.queries.QueryFactory;
import it.flaminiandrea.jphonesms.domain.SmsBoard;
import it.flaminiandrea.jphonesms.gui.MainWindow;
import it.flaminiandrea.jphonesms.gui.ShortMessagesTable;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class LoadSmsFromBackupActionListener implements ActionListener {
	private String fileSeparator = System.getProperties().getProperty("file.separator");
	private JFileChooser chooser;
	private JButton exportToTXT, exportToHTML;
	private MainWindow mainFrame;
	private ShortMessagesTable smsTable;
	private int isChoosed;

	public LoadSmsFromBackupActionListener(MainWindow mainFrame, ShortMessagesTable smsTable, JButton toTxt, JButton toHtml) {
		this.mainFrame = mainFrame;
		this.smsTable = smsTable;
		this.exportToHTML = toHtml;
		this.exportToTXT = toTxt;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		chooser = new JFileChooser();
		chooser.setDialogTitle("Choose iPhone Backup Directory.");
		this.chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.chooser.setCurrentDirectory(getSystemBackupDirectory());
		this.chooser.setAcceptAllFileFilterUsed(false);
		this.isChoosed = this.chooser.showOpenDialog(mainFrame);
		String choice = getChoose();
		if (choice == null || choice.endsWith(fileSeparator + ".")) {
			JOptionPane.showMessageDialog(mainFrame, "The directory you have choosen is invalid.", "Warning!", 2);
		} else {
			try {
				File smsDB = retrieveSmsDbBackupFileName(choice);
				File addressBook = retrieveContactsDbBackupFileName(choice);
				QueryFactory qFactory = new QueryFactory(smsDB.getAbsolutePath(), addressBook.getAbsolutePath());
				SmsBoard smsBoard = qFactory.retrieveSmsBoard();
				this.smsTable.getShortMessagesTableModel().setSmsBoard(smsBoard);
				this.smsTable.resizeAndRepaintMe();
				this.mainFrame.setSmsBoard(smsBoard);
				this.exportToTXT.setEnabled(true);
				this.exportToHTML.setEnabled(true);
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(mainFrame, e1.getMessage(), "Error!", 0);
			}
		}

	}

	private File retrieveContactsDbBackupFileName(String choice) {
		File addressBook = new File(choice + fileSeparator + FromBackupConstants.SQLITE3_DB_CONTACTS_FILE_NAME_MDDATA);
		if (!addressBook.exists()) {
			addressBook = new File(choice + fileSeparator + FromBackupConstants.SQLITE3_DB_CONTACTS_FILE_NAME);
		}
		if (!addressBook.exists()) {
			addressBook = new File(choice + fileSeparator + FromBackupConstants.SQLITE3_DB_CONTACTS_FILE_NAME_MDBACKUP);
		}
		return addressBook;
	}

	private File retrieveSmsDbBackupFileName(String choice) {
		File smsDB = new File(choice + fileSeparator + FromBackupConstants.SQLITE3_DB_SMS_FILE_NAME_MDDATA);
		if (!smsDB.exists()) {
			smsDB = new File(choice + fileSeparator + FromBackupConstants.SQLITE3_DB_SMS_FILE_NAME);
		}
		if (!smsDB.exists()) {
			smsDB = new File(choice + fileSeparator + FromBackupConstants.SQLITE3_DB_SMS_FILE_NAME_MDBACKUP);
		}
		return smsDB;
	}

	private File getSystemBackupDirectory() {
		String osName = System.getProperty("os.name");
		String userHome = System.getProperty("user.home");
		String result = ".";
		if (osName.equalsIgnoreCase(FromBackupConstants.MAC_OS_X_OS_NAME)) {
			result = userHome+FromBackupConstants.MAC_OS_X_PATH_TO_BACKUP;
		}
		return new java.io.File(result);
	}

	public String getChoose() {
		if (this.isChoosed == JFileChooser.APPROVE_OPTION) { 
			return this.chooser.getSelectedFile().getPath();
		}
		else {
			return null;
		}
	}
}
