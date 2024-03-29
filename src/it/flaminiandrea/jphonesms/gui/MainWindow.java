package it.flaminiandrea.jphonesms.gui;

import it.flaminiandrea.jphonesms.domain.ShortMessage;
import it.flaminiandrea.jphonesms.domain.SmsBoard;
import it.flaminiandrea.jphonesms.export.Exporter;
import it.flaminiandrea.jphonesms.export.html.HtmlExporter;
import it.flaminiandrea.jphonesms.export.txt.TxtExporter;
import it.flaminiandrea.jphonesms.gui.listeners.CreditsActionListener;
import it.flaminiandrea.jphonesms.gui.listeners.ExportActionListener;
import it.flaminiandrea.jphonesms.gui.listeners.LoadSmsFromBackupActionListener;
import it.flaminiandrea.jphonesms.gui.listeners.LoadSmsViaSftpActionListener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;


public class MainWindow extends JPanel {

	private static final long serialVersionUID = -9035743766726029237L;
	private static final Font IL_FONT = new Font("Arial",Font.PLAIN,12);
	private SmsBoard smsBoard;

	// COMPONENTI GRAFICI
	private JToolBar tools;
	private JButton loadSmsFromBackup, loadSmsViaSFTP, exportToTxt, exportToHtml, credits;
	private JPanel formPanel;
	private ShortMessagesTable smsTable;
	private JLabel userLabel, passwordLabel, ipAddressLabel;
	private JTextField userField, passwordField, ipAddressField;

	public MainWindow() {
		this.makeGraphics();
	}

	private void makeGraphics() {
		this.setLayout(new BorderLayout());

		smsTable = new ShortMessagesTable(new ShortMessagesTableModel(this.smsBoard), this);
		smsTable.setFont(IL_FONT);
		smsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		formPanel = new JPanel();
		formPanel.setLayout(null);
		this.formPanel.setPreferredSize(new Dimension(1,80));

		userLabel = new JLabel("User:");
		addElement(formPanel, userLabel, 10, 20, 70, 20);

		userField = new JTextField();
		userField.setText("root");
		addElement(formPanel, userField, 50, 20, 100, 20);

		passwordLabel = new JLabel("Password:");
		addElement(formPanel, passwordLabel, 160, 20, 70, 20);

		passwordField = new JTextField();
		passwordField.setText("alpine");
		addElement(formPanel, passwordField, 230, 20, 100, 20);

		ipAddressLabel = new JLabel("iPhone IP Address:");
		addElement(formPanel, ipAddressLabel, 340, 20, 130, 20);

		ipAddressField = new JTextField();
		addElement(formPanel, ipAddressField, 460, 20, 100, 20);

		this.tools = new JToolBar();

		this.exportToTxt = new JButton("Export to TXT");
		exportToTxt.setFocusPainted(false);
		Exporter txtExporter = new TxtExporter();
		ExportActionListener textExportActionListener = new ExportActionListener("Save in", this, txtExporter);
		exportToTxt.addActionListener(textExportActionListener);

		this.exportToHtml = new JButton("Export to HTML");
		exportToHtml.setFocusPainted(false);
		Exporter htmlExporter = new HtmlExporter();
		ExportActionListener htmlExportActionListener = new ExportActionListener("Save in", this, htmlExporter);
		exportToHtml.addActionListener(htmlExportActionListener);

		this.loadSmsViaSFTP = new JButton("Load SMS using SFTP");
		loadSmsViaSFTP.setFocusPainted(false);
		LoadSmsViaSftpActionListener loadSmsViaSftpListener = createLoadSmsActionListener();
		loadSmsViaSFTP.addActionListener(loadSmsViaSftpListener);
		
		this.loadSmsFromBackup = new JButton("Load SMS from Backup");
		loadSmsFromBackup.setFocusPainted(false);
		LoadSmsFromBackupActionListener loadSmsListener = createLoadSmsFromBackupActionListener();
		loadSmsFromBackup.addActionListener(loadSmsListener);
		
		tools.add(loadSmsViaSFTP);
		tools.add(loadSmsFromBackup);
		tools.add(exportToTxt);
		tools.add(exportToHtml);

		this.credits = new JButton("Credits");
		credits.setFocusPainted(false);
		credits.addActionListener(new CreditsActionListener());
		tools.add(credits);

		this.exportToTxt.setEnabled(false);
		this.exportToHtml.setEnabled(false);

		JPanel toolbarETabella = new JPanel();
		toolbarETabella.setLayout(new BorderLayout());
		toolbarETabella.add(new JScrollPane(smsTable), BorderLayout.CENTER);
		toolbarETabella.add(tools, BorderLayout.NORTH);

		//		// il tabbedPane generale
		//		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		//		tabbedPane.addTab("Viewer",toolbarETabella);
		//		this.add(tabbedPane, BorderLayout.CENTER);

		this.add(toolbarETabella, BorderLayout.CENTER);
		this.add(formPanel, BorderLayout.SOUTH);

	}

	private LoadSmsFromBackupActionListener createLoadSmsFromBackupActionListener() {
		LoadSmsFromBackupActionListener loadSmsFromBackupListener = new LoadSmsFromBackupActionListener(this, smsTable, this.exportToTxt, this.exportToHtml);
		return loadSmsFromBackupListener;
	}

	private LoadSmsViaSftpActionListener createLoadSmsActionListener() {
		LoadSmsViaSftpActionListener loadSmsListener = new LoadSmsViaSftpActionListener(userField, passwordField, ipAddressField, this, smsTable, this.exportToTxt, this.exportToHtml);
		return loadSmsListener;
	}

	private void addElement (Container c, Component component, int x, int y, int h, int w) {
		component.setBounds(x, y, h, w);
		c.add(component);
	}

	public SmsBoard getSmsBoard() {
		return smsBoard;
	}

	public void setSmsBoard(SmsBoard smsBoard) {
		this.smsBoard = smsBoard;
	}

	public void showSelectedMessage() {
		int selectedRowIndex = this.smsTable.getSelectedRow();
		if (this.smsBoard.getEntriesByNameAndByReverseDate().size() > 0 && selectedRowIndex >= 0) {
			List<ShortMessage> messages = this.smsBoard.getEntriesByNameAndByReverseDate();
			if (selectedRowIndex < messages.size() && selectedRowIndex >= 0) {
				ShortMessage selectedSms = (ShortMessage) messages.get(selectedRowIndex);
				if (selectedSms != null) {
					String mess = selectedSms.toString();
					JOptionPane.showMessageDialog(null, mess, "Message Number #" + selectedRowIndex, JOptionPane.PLAIN_MESSAGE, null);
				}
			}
		}

	}


}
