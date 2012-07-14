package it.flaminiandrea.jphonesms.gui.listeners;

import it.flaminiandrea.jphonesms.gui.MainWindow;
import it.flaminiandrea.jphonesms.logger.RuntimeLogger;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class SmsTableMouseListener implements MouseListener {

	private static String SHOW_MESSAGE_DETAILS_LOGGER_MESSAGE_DEFAULT = "Error showing messages details.";
	private Logger logger = RuntimeLogger.getInstance().getLogger(this.getClass());

	private MainWindow mainWindow;

	public SmsTableMouseListener(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		try {
			showMessageDetails(arg0);
		} catch (Exception e) {
			this.logger.error(SHOW_MESSAGE_DETAILS_LOGGER_MESSAGE_DEFAULT, e);
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error!", 0);
		}
	}

	private void showMessageDetails(MouseEvent arg0) {
		if (arg0.getClickCount() < 2) {
			return;
		}
		this.mainWindow.showSelectedMessage();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
