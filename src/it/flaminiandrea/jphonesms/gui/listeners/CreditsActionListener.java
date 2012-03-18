package it.flaminiandrea.jphonesms.gui.listeners;

import it.flaminiandrea.jphonesms.costants.ProjectCostants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class CreditsActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
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
