package it.flaminiandrea.jphonesms.gui.listeners;

import it.flaminiandrea.jphonesms.export.Exporter;
import it.flaminiandrea.jphonesms.gui.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class ExportActionListener implements ActionListener {
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
		String fileSeparator = System.getProperties().getProperty("file.separator");
		this.chooser.setCurrentDirectory(new java.io.File("."));
		this.chooser.setDialogTitle(this.choosertitle);
		this.chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.chooser.setAcceptAllFileFilterUsed(false);
		this.isChoosed = this.chooser.showOpenDialog(panel);
		String choice = getChoose();
		if (choice == null || choice.endsWith(fileSeparator + ".")) {
			JOptionPane.showMessageDialog(panel, "The directory you have choosen is invalid.", "Warning!", 2);
		} else {
			exporter.setPathToDirectory(choice);
			exporter.setData(this.panel.getSmsData());
			exporter.export();
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
