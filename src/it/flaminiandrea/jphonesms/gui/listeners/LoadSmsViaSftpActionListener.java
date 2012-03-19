package it.flaminiandrea.jphonesms.gui.listeners;

import it.flaminiandrea.jphonesms.db.queries.QueryFactory;
import it.flaminiandrea.jphonesms.domain.SmsBoard;
import it.flaminiandrea.jphonesms.gui.MainWindow;
import it.flaminiandrea.jphonesms.gui.ShortMessagesTable;
import it.flaminiandrea.jphonesms.sftp.ClientSFTP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class LoadSmsViaSftpActionListener implements ActionListener {

	private JTextField userField, passwordField, ipAddressField;
	private JButton exportToTXT, exportToHTML;
	private MainWindow mainFrame;
	private ShortMessagesTable smsTable;

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
		String fileSeparator = System.getProperties().getProperty("file.separator");
		String pathToDirectory = System.getProperties().getProperty("user.dir")+fileSeparator+"databases"+fileSeparator;
		java.io.File cartellaDati = new java.io.File(pathToDirectory);
		if (!cartellaDati.exists()) {
			cartellaDati.mkdirs();
		}
		String user = userField.getText();
		String password = passwordField.getText();
		String ipAddress = ipAddressField.getText();
		if (ipAddress == null || ipAddress.equals("")) {
			JOptionPane.showMessageDialog(mainFrame, "Please Enter the iPhone IP Address.", "Warning!", 2);
		} else {
			ClientSFTP client = new ClientSFTP();
			File smsDB;
			File addressBook;
			try {
				smsDB = client.getFile(user, password, ipAddress, "/var/mobile/Library/SMS/sms.db", pathToDirectory);
				addressBook = client.getFile(user, password, ipAddress, "/var/mobile/Library/AddressBook/AddressBook.sqlitedb", pathToDirectory);
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

}
