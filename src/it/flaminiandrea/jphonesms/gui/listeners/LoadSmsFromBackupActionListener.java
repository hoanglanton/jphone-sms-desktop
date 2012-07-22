package it.flaminiandrea.jphonesms.gui.listeners;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import it.flaminiandrea.jphonesms.costants.FromBackupConstants;
import it.flaminiandrea.jphonesms.db.queries.QueryFactory;
import it.flaminiandrea.jphonesms.domain.SmsBoard;
import it.flaminiandrea.jphonesms.gui.FileChooser;
import it.flaminiandrea.jphonesms.gui.MainWindow;
import it.flaminiandrea.jphonesms.gui.ShortMessagesTable;
import it.flaminiandrea.jphonesms.logger.RuntimeLogger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class LoadSmsFromBackupActionListener implements ActionListener {

	public static String LOADING_MESSAGE = "Loading Messages from Backup Directory... Please Wait.";
	private static String LOAD_FROM_BACKUP_LOGGER_MESSAGE_DEFAULT = "Error loading messages from backup.";
	private Logger logger = RuntimeLogger.getInstance().getLogger(this.getClass());

	private String fileSeparator = System.getProperties().getProperty("file.separator");
	private JFileChooser chooser;
	private final JButton exportToTXT, exportToHTML;
	private final MainWindow mainFrame;
	private final ShortMessagesTable smsTable;
	private int isChoosed;

	public LoadSmsFromBackupActionListener(MainWindow mainFrame, ShortMessagesTable smsTable, JButton toTxt, JButton toHtml) {
		this.mainFrame = mainFrame;
		this.smsTable = smsTable;
		this.exportToHTML = toHtml;
		this.exportToTXT = toTxt;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			loadFromBackup();
		} catch (Exception e1) {
			logger.error(LOAD_FROM_BACKUP_LOGGER_MESSAGE_DEFAULT, e1);
			JOptionPane.showMessageDialog(mainFrame, e1.getMessage(), "Error!", 0);
		}
	}

	private void loadFromBackup() throws Exception {
		final JFrame frame = new JFrame();
		chooser = new FileChooser(this.mainFrame, frame, LOADING_MESSAGE);
		chooser.setDialogTitle("Choose iPhone Backup Directory.");
		this.chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.chooser.setCurrentDirectory(getSystemBackupDirectory());
		this.chooser.setAcceptAllFileFilterUsed(false);
		this.isChoosed = this.chooser.showOpenDialog(mainFrame);
		final String choice = getChoose();
		if (!(choice == null || choice.endsWith(fileSeparator + "."))) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					File smsDB = retrieveSmsDbBackupFileName(choice);
					File addressBook = retrieveContactsDbBackupFileName(choice);
					QueryFactory qFactory = new QueryFactory(smsDB.getAbsolutePath(), addressBook.getAbsolutePath());
					SmsBoard smsBoard;
					try {
						smsBoard = qFactory.retrieveSmsBoard();
						frame.dispose();
						smsTable.getShortMessagesTableModel().setSmsBoard(smsBoard);
						smsTable.resizeAndRepaintMe();
						mainFrame.setSmsBoard(smsBoard);
						exportToTXT.setEnabled(true);
						exportToHTML.setEnabled(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
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
