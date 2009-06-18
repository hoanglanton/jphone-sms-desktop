package it.flaminiandrea.jphonesms.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import it.flaminiandrea.jphonesms.db.queries.QueryFactory;
import it.flaminiandrea.jphonesms.domain.ContactsBoard;
import it.flaminiandrea.jphonesms.domain.Data;
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
		this.chooser.setCurrentDirectory(new java.io.File("."));
		this.chooser.setAcceptAllFileFilterUsed(false);
		this.isChoosed = this.chooser.showOpenDialog(mainFrame);
		String choice = getChoose();
		if (choice == null || choice.endsWith(fileSeparator + ".")) {
			JOptionPane.showMessageDialog(mainFrame, "The directory you have choosen is invalid.", "Warning!", 2);
		} else {
			try {
				File smsDB = new File(choice + fileSeparator + "3d0d7e5fb2ce288813306e4d4636395e047a3d28.mddata");
				File addressBook = new File(choice + fileSeparator + "31bb7ba8914766d4ba40d6dfb6113c8b614be442.mddata");
				QueryFactory qFactory = new QueryFactory();
				SmsBoard smsBoard = qFactory.retrieveSmsBoard(smsDB.getAbsolutePath());
				ContactsBoard contactsBoard = qFactory.retrieveContactsBoard(addressBook.getAbsolutePath());
				Data data = new Data(smsBoard, contactsBoard);
				this.smsTable.getShortMessagesTableModel().setSmsData(data);
				this.smsTable.resizeAndRepaintMe();
				this.mainFrame.setSmsData(data);
				this.exportToTXT.setEnabled(true);
				this.exportToHTML.setEnabled(true);
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(mainFrame, e1.getMessage(), "Error!", 0);
			}
		}

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
