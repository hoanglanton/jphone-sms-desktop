package it.flaminiandrea.jphonesms.gui.listeners;

import it.flaminiandrea.jphonesms.export.Exporter;
import it.flaminiandrea.jphonesms.gui.MainWindow;
import it.flaminiandrea.jphonesms.logger.RuntimeLogger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;


public class ExportActionListener implements ActionListener {

	private static String EXPORT_LOGGER_MESSAGE_DEFAULT = "Error exporting messages.";
	private Logger logger = RuntimeLogger.getInstance().getLogger(this.getClass());

	private String choosertitle;
	private JFileChooser chooser;
	private MainWindow panel;
	private int isChoosed;
	private Exporter exporter;

	public ExportActionListener(String choosertitle, MainWindow panel, Exporter exporter) {
		this.choosertitle = choosertitle;
		this.chooser = new JFileChooser();
		this.panel = panel;
		this.isChoosed = 1;
		this.exporter = exporter;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			exportMessages();
		} catch (Exception e2) {
			this.logger.error(EXPORT_LOGGER_MESSAGE_DEFAULT, e2);
			JOptionPane.showMessageDialog(null, e2.getMessage(), "Error!", 0);
		}
	}

	private void exportMessages() {
		String fileSeparator = System.getProperty("file.separator");
		java.io.File currentDir = new java.io.File(System.getProperty("user.home"));
		this.chooser.setCurrentDirectory(currentDir);
		this.chooser.setDialogTitle(this.choosertitle);
		this.chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.chooser.setAcceptAllFileFilterUsed(false);
		this.isChoosed = this.chooser.showOpenDialog(panel);
		String choice = getChoose();
		if (choice == null || choice.endsWith(fileSeparator + ".")) {
			JOptionPane.showMessageDialog(panel, "The directory you have choosen is invalid.", "Warning!", 2);
		} else {
			this.exporter.setPathToDirectory(choice);
			this.exporter.setSmsBoard(this.panel.getSmsBoard());
			this.exporter.export();
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
