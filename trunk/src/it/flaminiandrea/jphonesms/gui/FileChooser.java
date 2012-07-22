package it.flaminiandrea.jphonesms.gui;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;
	private MainWindow mainWindow;
	private JFrame frame;
	private String loadingMessage;

	public FileChooser(MainWindow mainWindow, JFrame frame, String loadingMessage) {
		super();
		this.mainWindow = mainWindow;
		this.frame = frame;
		this.loadingMessage = loadingMessage;
	}

	@Override
	public void approveSelection() {
		super.approveSelection();
		GenericProcessWaiter genericProcessWaiter = new GenericProcessWaiter(this.frame, this.loadingMessage);
		genericProcessWaiter.run();
	}

	@Override
	public void cancelSelection() {
		super.cancelSelection();
		JOptionPane.showMessageDialog(mainWindow, "The directory you have choosen is invalid.", "Warning!", JOptionPane.WARNING_MESSAGE);
	}

}
