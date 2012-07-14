package it.flaminiandrea.jphonesms.gui.listeners;

import it.flaminiandrea.jphonesms.costants.ProjectCostants;
import it.flaminiandrea.jphonesms.logger.RuntimeLogger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class CreditsActionListener implements ActionListener {

	private static String CREDITS_LOGGER_MESSAGE_DEFAULT = "Error showing Credits.";
	private Logger logger = RuntimeLogger.getInstance().getLogger(this.getClass());

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			showCredits();
		} catch (Exception e2) {
			this.logger.error(CREDITS_LOGGER_MESSAGE_DEFAULT, e2);
			JOptionPane.showMessageDialog(null, e2.getMessage(), "Error!", 0);
		}
	}

	private void showCredits() {
		Date currentDate = new Date();
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		String mess = 
				"Copyright (C) "+yearFormat.format(currentDate)+"\n" +
						"Author: "+ProjectCostants.AUTHOR_NAME+"\n"+
						"A.K.A. "+ProjectCostants.AUTHOR_AKA_NAME+"\n"+
						"Author's email: "+ProjectCostants.AUTHOR_EMAIL+"\n"+
						"Author's site: "+ProjectCostants.AUTHOR_SITE+"\n"+
						"\n";
		JOptionPane.showMessageDialog(null, mess, "Credits", JOptionPane.PLAIN_MESSAGE, null);
	}

}
