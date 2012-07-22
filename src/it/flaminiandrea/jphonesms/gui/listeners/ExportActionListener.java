package it.flaminiandrea.jphonesms.gui.listeners;

import it.flaminiandrea.jphonesms.export.Exporter;
import it.flaminiandrea.jphonesms.gui.FileChooser;
import it.flaminiandrea.jphonesms.gui.MainWindow;
import it.flaminiandrea.jphonesms.logger.RuntimeLogger;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;


public class ExportActionListener implements ActionListener {

	private static final String EXPORTING_MESSAGE = "Exporting Messages... Please Wait.";
	private static final String EXPORT_LOGGER_MESSAGE_DEFAULT = "Error exporting messages.";
	private final Logger logger = RuntimeLogger.getInstance().getLogger(this.getClass());

	private String choosertitle;
	private JFileChooser chooser;
	private MainWindow panel;
	private int isChoosed;
	private final Exporter exporter;

	public ExportActionListener(String choosertitle, MainWindow panel, Exporter exporter) {
		this.choosertitle = choosertitle;
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
		final JFrame frame = new JFrame();
		String fileSeparator = System.getProperty("file.separator");
		java.io.File currentDir = new java.io.File(System.getProperty("user.home"));
		this.chooser = new FileChooser(this.panel, frame, EXPORTING_MESSAGE);
		this.chooser.setCurrentDirectory(currentDir);
		this.chooser.setDialogTitle(this.choosertitle);
		this.chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.chooser.setAcceptAllFileFilterUsed(false);
		this.isChoosed = this.chooser.showOpenDialog(panel);
		final String choice = getChoose();
		if (!(choice == null || choice.endsWith(fileSeparator + "."))) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						exporter.setPathToDirectory(choice);
						exporter.setSmsBoard(panel.getSmsBoard());
						exporter.export();
						frame.dispose();
					} catch (Exception e) {
						logger.error(EXPORT_LOGGER_MESSAGE_DEFAULT, e);
						JOptionPane.showMessageDialog(null, e.getMessage(), "Error!", 0);
					}
				}
			});
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
