package it.flaminiandrea.jphonesms.gui;

import it.flaminiandrea.jphonesms.gui.listeners.SmsTableMouseListener;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class ShortMessagesTable extends JTable {
	private static final long serialVersionUID = -5974641756192692786L;
	private ShortMessagesTableModel shortMessagesTableModel;
	private MainWindow mainWindow;
	
	public ShortMessagesTable(ShortMessagesTableModel shortMessagesTableModel, MainWindow mainWindow) {
		super(shortMessagesTableModel);
		this.shortMessagesTableModel = shortMessagesTableModel;
		this.mainWindow = mainWindow;
		
        TableColumnModel tcm = this.getColumnModel();
        tcm.setColumnMargin(20);
        TableColumn tc = tcm.getColumn(4);
        tc.setPreferredWidth(500);

        tc = tcm.getColumn(3);
        tc.setPreferredWidth(70);

        this.setShowVerticalLines(false);
        SmsTableMouseListener mouseListener = new SmsTableMouseListener(this.mainWindow);
        this.addMouseListener(mouseListener);
        
        String[] ident = {"Direction","Name","Address","Date","Text"};
        for (int i = 0; i < ident.length; i++) {
            TableColumn ColFormato = this.getColumn(ident[i]);
            DefaultTableCellRenderer ColFormatoRenderer = new DefaultTableCellRenderer();
            ColFormatoRenderer.setHorizontalAlignment(JLabel.LEFT);
            ColFormato.setCellRenderer(ColFormatoRenderer);
            ColFormato.setResizable(true);
        }
	}
	
    public void resizeAndRepaintMe() {
        this.resizeAndRepaint();
    }

	public ShortMessagesTableModel getShortMessagesTableModel() {
		return shortMessagesTableModel;
	}

	public void setShortMessagesTableModel(ShortMessagesTableModel shortMessagesTableModel) {
		this.shortMessagesTableModel = shortMessagesTableModel;
	}
}
