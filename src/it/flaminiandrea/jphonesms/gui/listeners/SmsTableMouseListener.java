package it.flaminiandrea.jphonesms.gui.listeners;

import it.flaminiandrea.jphonesms.gui.MainWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SmsTableMouseListener implements MouseListener {
	private MainWindow mainWindow;

	public SmsTableMouseListener(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
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
