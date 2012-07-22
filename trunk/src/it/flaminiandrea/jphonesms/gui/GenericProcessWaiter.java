package it.flaminiandrea.jphonesms.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GenericProcessWaiter extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private String waitingMessage;
	public static final String WAITING_TITLE = "Wait";
	
	private JFrame frame;

	public GenericProcessWaiter(JFrame frame, String waitingMessage) {
		this.frame = frame;
		this.frame.setTitle(WAITING_TITLE);
		this.waitingMessage = waitingMessage;
	}

	@Override
	public void run() {
		JLabel label = new JLabel(this.waitingMessage);
		this.add(label, BorderLayout.PAGE_START);
		setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
		this.setOpaque(true);
		this.frame.setContentPane(this);
		showFrame();
	}

	private void showFrame() {
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame.pack();
		int w = this.frame.getSize().width;
		int h = this.frame.getSize().height;
		int x = (dim.width-w)/2;
		int y = (dim.height-h)/2;
		this.frame.setLocation(x, y);
		this.frame.setVisible(true);
	}

}
