package risk.client.ui.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import risk.client.net.ClientEngineNetwork;
import risk.client.ui.gui.RiskClientGUI;
import risk.commons.interfaces.PanelInterface;
import risk.commons.valueobjects.Karte;
import risk.commons.valueobjects.Spieler;

/**
 * Ein Objekt dieser Klasse wird in der East-Seite des Main-Panels angezeigt.
 * In ihm sind Informationen zum aktuellen Spielstand hinterlegt
 * @author Yannik
 */
public class InfoPanel extends DefaultPanel implements PanelInterface {
	private static final long serialVersionUID = 5648424585521793448L;
	
	/** Liste mit Handkarten des Clientspielers */
	private JList<Karte> east_kartenList;
	/** Tabelle mit allen Spielern und deren Farbe */
	private JTable spielerTable;
	/** Tabelle mit allen Spielern und deren Besitzzahlen */
	private JTable standTable;
	
	/** Konstruktor */
	public InfoPanel() {
		// Allg. Infos
		Dimension dim = new Dimension(150,563);
		this.setMinimumSize(dim);
		this.setMaximumSize(dim);
		this.setPreferredSize(dim);
		this.setBorder(new LineBorder(new Color(0x00000)));
		this.setLayout(null);
		
		// Scrollpane für die Kartenliste
		JScrollPane east_kartenScrollPane = new JScrollPane();
		east_kartenScrollPane.setBounds(10, 363, 130, 98);
		this.add(east_kartenScrollPane);
		// Kartenliste
		east_kartenList = new JList<Karte>();
		east_kartenList.setVisibleRowCount(6);
		east_kartenList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		east_kartenScrollPane.setColumnHeaderView(east_kartenList);
		east_kartenList.setFont(new Font("Tahoma", Font.PLAIN, 11));
		// Für den Inhalt der Kartenliste Updatemethode aufrufen
		this.updateKartenliste();
		// Label für die Kartenliste
		JLabel lblDeineHandkarten = new JLabel("Deine Handkarten:");
		lblDeineHandkarten.setBounds(10, 338, 130, 14);
		this.add(lblDeineHandkarten);
		
		// ScrollPane für die Spielerliste
		JScrollPane east_spielerScrollPane = new JScrollPane();
		east_spielerScrollPane.setBounds(10, 29, 130, 123);
		east_spielerScrollPane.setBackground(new Color(255,255,255));
		this.add(east_spielerScrollPane);
		// Spielertable
		spielerTable = new JTable();
		spielerTable.setShowHorizontalLines(false);
		spielerTable.setShowVerticalLines(false);
		spielerTable.setShowGrid(false);
		spielerTable.setRowSelectionAllowed(false);
		spielerTable.setVisible(false);
		DefaultTableModel spielerModel = new DefaultTableModel();
		// Spieler-Array ziehen
		Spieler[] spieler = ClientEngineNetwork.getEngine().getWelt().getSpieler();
		Vector<Vector<String>> spielerRows = new Vector<>(6);
		Vector<Object> spielerColumns = new Vector<Object>(2);
		spielerColumns.add("Name");
		spielerColumns.add("Farbe");
		for (int i = 0; i < spieler.length; i++) {
			// Daten aus dem Spieler-Array ziehen
			if (spieler[i] != null) {
				Vector<String> einSpieler = new Vector<String>();
				einSpieler.add(spieler[i].getName());
				einSpieler.add(spieler[i].getFarbtext());
				spielerRows.add(einSpieler);
			}
		}
		spielerModel.setDataVector(spielerRows,spielerColumns);
		spielerTable.setModel(spielerModel);
	
		for (int i = 0; i < spielerModel.getColumnCount();i++) {
			spielerTable.getColumnModel().getColumn(i).setResizable(false);
		}
		
		spielerTable.setVisible(true);
		east_spielerScrollPane.setViewportView(spielerTable);
	
		// Teilnehmer-Label
		JLabel lblTeilnehmer = new JLabel("Teilnehmer:");
		lblTeilnehmer.setBounds(10, 11, 130, 14);
		add(lblTeilnehmer);
		
		// Spielstand-Label
		JLabel lblDeinSpielstand = new JLabel("Spielstand:");
		lblDeinSpielstand.setBounds(10, 173, 130, 14);
		add(lblDeinSpielstand);
		
		// Separators
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 163, 130, 2);
		add(separator);
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 325, 130, 2);
		add(separator_1);
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, 472, 130, 2);
		add(separator_2);
		
		// Scrollpane für den Spielstand
		JScrollPane east_standScrollPane = new JScrollPane();
		east_standScrollPane.setBounds(10, 191, 130, 123);
		add(east_standScrollPane);
		
		// Spielstand-Tabelle
		standTable = new JTable();
		standTable.setShowVerticalLines(false);
		standTable.setShowHorizontalLines(false);
		standTable.setShowGrid(false);
		standTable.setRowSelectionAllowed(false);
		this.updateInfos();
		
		for (int i = 0; i < spielerModel.getColumnCount();i++) {
			standTable.getColumnModel().getColumn(i).setResizable(false);
		}
		
		east_standScrollPane.setViewportView(standTable);
		
		// Image-Panel für das kleine Splashbild
		JLayeredPane splash;
		try {
			splash = new ImagePanel("images/splash_mini.png");
		} catch (IOException e) {
			splash = new JLayeredPane();
			splash.add(new JTextField("splashmini kann nicht geladen werden"));
		}
		splash.setBounds(10,481,130,80);
		add(splash);
	}

	/** Updatemethode für die Kartenliste. Wird vom MainPanel aufgerufen, wenn der Client-Spieler eine neue Karte erwirbt,
	 * und direkt bei der Initialisierung als leere Liste. */
	public void updateKartenliste() {
		// Handkarten über ClientEngineNetwork ziehen
		DefaultListModel<Karte> model = new DefaultListModel<Karte>();
		Iterator<Karte> iter = RiskClientGUI.getSpieler().getKarten().iterator();
		while (iter.hasNext()) {
			model.addElement(iter.next());
		}
		east_kartenList.setModel(model);
	}
	
	/** Updatemethode für die Eigenschaften des Client-Spielers. Wird dann aufgerufen, wenn bspw. ein Land von ihm eingenommen wurde */
	public void updateInfos() {
		// Vergleich alt/neu nur ausführen, wenn das nicht der erste Zeichnen-Vorgang ist
		// (also die Anzahl Zeilen im Table ungleich 0 ist)
		int[] alteLandCounts = null;
		if (standTable.getModel().getRowCount() != 0) {
			// Array für die alten Land-Werte erstellen
			alteLandCounts = new int[standTable.getModel().getRowCount()];
			
			// Befüllen
			for (int i = 0; i < standTable.getModel().getRowCount(); i++) {
				alteLandCounts[i] =  Integer.parseInt((String) standTable.getModel().getValueAt(i, 1));
			}
		}
		
		// Boolean erstellen, der angibt, ob das Model gewechselt werden muss
		boolean changeNotwendig = false;
		
		// Spieler-Array ziehen
		Spieler[] spieler = ClientEngineNetwork.getEngine().getWelt().getSpieler();
		
		// Index für alle gefundenen Spieler (wird dann für das Iterieren durch alteLandCounts benutzt
		int neuIndex = 0;
		
		// Neues Model und neue Vektoren erstellen
		DefaultTableModel standModel = new DefaultTableModel();
		Vector<Vector<String>> standRows = new Vector<>(6);
		Vector<Object> standColumns = new Vector<Object>(2);
		standColumns.add("Name");
		standColumns.add("Länder");
		
		// Neue Vektoren mit Werten befüllen
		for (int i = 0; i < spieler.length; i++) {
			// Daten aus dem Spieler-Array ziehen
			if (spieler[i] != null) {
				Vector<String> einSpieler = new Vector<String>();
				einSpieler.add(spieler[i].getName());
				einSpieler.add(""+spieler[i].getBesitz().size());
				standRows.add(einSpieler);
				// Ist dieser Wert genau wie der alte? Wenn nein, setz' den Boolean "changeNotwendig" auf true
				// (auch wieder nur dann, wenn die Zeilenanzahl ungleich 0 ist, es also einen Vergleich überhaupt erst geben kann
				if (standTable.getModel().getRowCount() != 0 && 
						spieler[i].getBesitz().size() != alteLandCounts[neuIndex++])
					changeNotwendig = true;
			}
		}
		
		// Nach der Schleife wird nur dann das Model gewechselt, wenn es mindestens eine Änderung gab
		// (oder dies der erste Update-Vorgang ist). Sonst nicht
		if (changeNotwendig || standTable.getModel().getRowCount() == 0) {
			standTable.setModel(standModel);
			standModel.setDataVector(standRows,standColumns);
		}
	}
}
