package it.flaminiandrea.jphonesms.exec;

import it.flaminiandrea.jphonesms.costants.ProjectCostants;
import it.flaminiandrea.jphonesms.gui.MainWindow;
import it.flaminiandrea.jphonesms.logger.RuntimeLogger;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

public class Launcher {

	public static void main(String[] args) {
		try {
			start();
		} catch (Exception e) {
			Logger logger = RuntimeLogger.getInstance().getLogger(Launcher.class);
			logger.error("Error starting application.", e);
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error!", 0);
			System.exit(0);
		}
	}

	private static void start() {
		JFrame frame = new JFrame();
		final MainWindow ex = new MainWindow();
		frame.getContentPane().add(ex);
		frame.setTitle(ProjectCostants.SW_NAME+" "+ProjectCostants.SW_VERSION);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int larg = 600;
		int alt = screenSize.height/2;
		frame.setSize(larg, alt);
		frame.setLocation(screenSize.width/2-larg/2,screenSize.height/2-alt/2);

		frame.setVisible(true);
		frame.setResizable(true);
	}

}
