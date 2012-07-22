package it.flaminiandrea.jphonesms.gui.listeners;

import it.flaminiandrea.jphonesms.db.queries.QueryFactory;
import it.flaminiandrea.jphonesms.domain.SmsBoard;
import it.flaminiandrea.jphonesms.gui.GenericProcessWaiter;
import it.flaminiandrea.jphonesms.gui.MainWindow;
import it.flaminiandrea.jphonesms.gui.ShortMessagesTable;
import it.flaminiandrea.jphonesms.logger.RuntimeLogger;
import it.flaminiandrea.jphonesms.sftp.ClientSFTP;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

public class LoadSmsViaSftpActionListener implements ActionListener {

	public static final String LOADING_MESSAG_SFTP = "Loading Messages via SFTP Connection... Please Wait.";
	private static String LOAD_FROM_SFTP_LOGGER_MESSAGE_DEFAULT = "Error loading messages from SFTP connection.";
	private final Logger logger = RuntimeLogger.getInstance().getLogger(this.getClass());

	private JTextField userField, passwordField, ipAddressField;
	private final JButton exportToTXT, exportToHTML;
	private final MainWindow mainFrame;
	private final ShortMessagesTable smsTable;

	public LoadSmsViaSftpActionListener(JTextField userField, JTextField passwordField, JTextField ipAddressField, MainWindow mainFrame, ShortMessagesTable smsTable, JButton toTxt, JButton toHtml) {
		this.userField = userField;
		this.passwordField = passwordField;
		this.ipAddressField = ipAddressField;
		this.mainFrame = mainFrame;
		this.smsTable = smsTable;
		this.exportToHTML = toHtml;
		this.exportToTXT = toTxt;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			loadSmsFromSFTP();
		} catch (Exception e2) {
			this.logger.error(LOAD_FROM_SFTP_LOGGER_MESSAGE_DEFAULT, e2);
			JOptionPane.showMessageDialog(mainFrame, e2.getMessage(), "Error!", 0);
		}
	}

	private void loadSmsFromSFTP() throws Exception {
		String fileSeparator = System.getProperties().getProperty("file.separator");
		final String pathToDirectory = System.getProperties().getProperty("user.dir")+fileSeparator+"databases"+fileSeparator;
		java.io.File cartellaDati = new java.io.File(pathToDirectory);
		if (!cartellaDati.exists()) {
			cartellaDati.mkdirs();
		}
		final String user = userField.getText();
		final String password = passwordField.getText();
		final String ipAddress = ipAddressField.getText();
		if (ipAddress == null || ipAddress.equals("")) {
			JOptionPane.showMessageDialog(mainFrame, "Please Enter the iPhone IP Address.", "Warning!", 2);
		} else {
			final GenericProcessWaiter genericProcessWaiter = new GenericProcessWaiter(LOADING_MESSAG_SFTP);
			genericProcessWaiter.run();
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						ClientSFTP client = new ClientSFTP();
						File smsDB;
						File addressBook;
						smsDB = client.getFile(user, password, ipAddress, "/var/mobile/Library/SMS/sms.db", pathToDirectory);
						addressBook = client.getFile(user, password, ipAddress, "/var/mobile/Library/AddressBook/AddressBook.sqlitedb", pathToDirectory);
						QueryFactory qFactory = new QueryFactory(smsDB.getAbsolutePath(), addressBook.getAbsolutePath());
						SmsBoard smsBoard = qFactory.retrieveSmsBoard();
						smsTable.getShortMessagesTableModel().setSmsBoard(smsBoard);
						smsTable.resizeAndRepaintMe();
						mainFrame.setSmsBoard(smsBoard);
						exportToTXT.setEnabled(true);
						exportToHTML.setEnabled(true);
					} catch (Exception e) {
						logger.error(LOAD_FROM_SFTP_LOGGER_MESSAGE_DEFAULT, e);
						JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error!", 0);
					}
				}
			});

		}
	}

}
