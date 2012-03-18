package it.flaminiandrea.jphonesms.exec;

import it.flaminiandrea.jphonesms.costants.ProjectCostants;
import it.flaminiandrea.jphonesms.gui.MainWindow;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Launcher {

	public static void main(String[] args) {
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
