package risk.client.ui.gui.panels;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import risk.client.net.ClientEngineNetwork;
import risk.client.ui.gui.RiskClientGUI;
import risk.client.ui.gui.comp.ErrorDialog;
import risk.commons.ChangeConst;
import risk.commons.SpielerState;
import risk.commons.interfaces.PanelInterface;
import risk.commons.valueobjects.Result;
import risk.commons.valueobjects.Spieler;

/**
 * Panel zum Login vor Beginn der Partie
 * @author Yannik
 */
public class LoginPanel extends DefaultPanel implements PanelInterface, Observer {
	private static final long serialVersionUID = -6708652027192220681L;
	
	/** Textfeld für die Eingabe des Namens */
	private JTextField textField;				
	/** Tabelle mit allen eingeloggten Spielern */
	private JTable table;						
	/** JLabel mit Angabe der erfolgten Registrierung */
	private JLabel lblDuBistRegged;				
	
	/** Button zum Beitreten einer Partie */
	private JButton btnDemSpielBeitreten;		
	/** Button zum Zurücktreten von einer Partie */
	private JButton btnZurcktreten;				
	/** Button zum Starten einer Partie (wenn der Spieler der Spielleiter ist) */
	private JButton btnPartieStarten;			
	
	/** Eingegebener Name */
	private String enteredName;					
	
	/** Login-Stand (true: eingeloggt, false: nicht eingeloggt) */
	private static boolean loginState;			

	/** Konstruktor */
	public LoginPanel() {
		setLayout(null);
        this.setPreferredSize(RiskClientGUI.windowsize);
        this.setOpaque(true);
		// Textfeld zum Eingeben des Namens
		textField = new JTextField();
		textField.setBounds(39, 75, 357, 20);
		add(textField);
		textField.setColumns(10);
		// Button zum Beitreten
		btnDemSpielBeitreten = new JButton("Beitreten");
		btnDemSpielBeitreten.setBounds(39, 106, 152, 23);
		add(btnDemSpielBeitreten);
		// Button zum Zurücktreten
		btnZurcktreten = new JButton("Zur\u00FCcktreten");
		btnZurcktreten.setEnabled(false);
		btnZurcktreten.setBounds(244, 106, 152, 23);
		add(btnZurcktreten);
		// Überschrift-Textarea
		JTextArea txtrSchnDassDu = new JTextArea();
		txtrSchnDassDu.setBorder(null);
		txtrSchnDassDu.setEditable(false);
		txtrSchnDassDu.setFont(new Font("Arial", Font.PLAIN, 11));
		txtrSchnDassDu.setLineWrap(true);
		txtrSchnDassDu.setBackground(null);
		txtrSchnDassDu.setText("Sch\u00F6n, dass du dich entschieden hast, RISK zu spielen! Bitte gib' deinen Namen ein und tritt dem Spiel mit Klick auf \"Beitreten\" bei...");
		txtrSchnDassDu.setBounds(39, 11, 357, 58);
		add(txtrSchnDassDu);
		// "Bereits registrierte Spieler"-Label
		JLabel lblBer = new JLabel("Bereits registrierte Spieler:");
		lblBer.setBounds(10, 146, 430, 20);
		add(lblBer);
		// Scrollpane und Tabelle mit bereits registrierten Spielern
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 176, 430, 143);
		add(scrollPane);
		table = new JTable();
		// Design der Tabelle
		table.setBackground(null);
		table.setBorder(null);
		DefaultTableModel model = new DefaultTableModel(0,3);
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// Breite der Spalten
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setPreferredWidth(70);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		updateTable();
		scrollPane.setViewportView(table);
		// Du bist regged-Label (wird beschrieben, wenn der Spieler registriert ist)
		lblDuBistRegged = new JLabel("");
		lblDuBistRegged.setBounds(10, 325, 430, 20);
		add(lblDuBistRegged);
		// Partiestarten-Button
		btnPartieStarten = new JButton("Partie starten!");
		btnPartieStarten.setBounds(283, 350, 157, 23);
		btnPartieStarten.setVisible(false);
		add(btnPartieStarten);
		// Listener hinzufügen
		addListeners();
		// Observer hinzufügen
		ClientEngineNetwork.getEngine().addObserver(this);
	}
	
	/** Update-Methode für die Tabelle. Wird dann aufgerufen,
	 * wenn ein Spieler beitritt oder verlässt */
	private void updateTable() {
		table.setVisible(false);
		//DefaultTableModel model = (DefaultTableModel) registerdialog_list.getModel();
				DefaultTableModel model = new DefaultTableModel(0,3);
				
				// Spieler-Array ziehen
				Spieler[] spieler = ClientEngineNetwork.getEngine().getWelt().getSpieler();
				Vector<Vector<String>> rows = new Vector<>(6);
				Vector<Object> columns = new Vector<Object>();
				columns.add("#");
				columns.add("Name");
				columns.add("Farbe");
				for (int i = 0; i < spieler.length; i++) {
					// Daten aus dem Spieler-Array ziehen
					if (spieler[i] != null) {
						Vector<String> einSpieler = new Vector<String>();
						int index = i+1;
						einSpieler.add(""+index);
						einSpieler.add(spieler[i].getName() + (spieler[i].isGameMaster() ? " (Spielleiter)" : ""));
						einSpieler.add(spieler[i].getFarbtext());
						rows.add(einSpieler);
					}
				}
				model.setDataVector(rows,columns);
				table.setModel(model);

		table.setVisible(true);
	}
	
	/** Listener hinzufügen für die Buttons und das Textfeld */
	private void addListeners() {
		// Confirm-ActionListener für Beitreten-Button und Name-TextField registrieren.
		ActionListener confirmListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Login durchziehen. Dazu zuerst ein Spieler-Objekt erstellen
				enteredName = textField.getText();
				RiskClientGUI.setSpieler(new Spieler(enteredName));
				if (enteredName.equals("")) {
					new ErrorDialog(RiskClientGUI.getInstance(), "Du hast keinen Namen angegeben!", "Fehler");
					return;
				}
				// und einloggen
				try {
					ClientEngineNetwork.getEngine().login(RiskClientGUI.getSpieler(), 
							ClientEngineNetwork.getEngine());
					setLoginState(true);
					RiskClientGUI.getSpieler().setState(SpielerState.LOGGED_IN);
					// Menüpunkt für "Partie laden" aktivieren, wenn der Spieler der Spielleiter ist
					if (RiskClientGUI.getSpieler().isGameMaster())
						RiskClientGUI.getLoadGameMenuItem().setEnabled(true);
					//System.out.println("GM? " + RiskClientGUI.getSpieler().isGameMaster());
					updateTable();
				} catch (Exception e) {
					new ErrorDialog(RiskClientGUI.getInstance(), e.getMessage(), "Problem beim Einloggen");
				}
			}
		};

		// Login-Button
		btnDemSpielBeitreten.addActionListener(confirmListener);
		
		// Name-Textfeld
		textField.addActionListener(confirmListener);
		
		// Zurücktreten-Button
		btnZurcktreten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// Zurücktreten. Das einfach an den Server weiterleiten
				Spieler sel = ClientEngineNetwork.getEngine().searchSpieler(enteredName);
				// Spielstartbutton verstecken
				btnPartieStarten.setVisible(false);
				// Loginstatus ändern
				setLoginState(false);
				sel.setState(SpielerState.NOT_LOGGED_IN);
				// Menüpunkt für "Partie laden" deaktivieren
				RiskClientGUI.getLoadGameMenuItem().setEnabled(false);
				// Spieler-Eigenschaften "zurückpacken" lassen
				ClientEngineNetwork.getEngine().disposeSpieler(sel);
				RiskClientGUI.getSpieler().setGameMaster(false);
				updateTable();
			}
		});
		
		// Spielstart-Button
		btnPartieStarten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				// Die Partie für alle Clients starten
				RiskClientGUI.getSaveGameMenuItem().setEnabled(true);
				RiskClientGUI.getLoadGameMenuItem().setEnabled(false);
				ClientEngineNetwork.getEngine().startGame();
			}
		});
	}
	
	/** Login-Status setzen. Wird bei Button-Klick ausgelöst und setzt Textfelder,
	 * Texte und Spielleiter-Status abhängig vom Parameter
	 * @param b	true: eingeloggt, false: nicht eingeloggt */
	private void setLoginState(boolean b) {
		loginState = b;
		lblDuBistRegged.setText( b ? "Du hast dich erfolgreich registriert." + (RiskClientGUI.getSpieler().isGameMaster() ?
				" (Du bist der Spielleiter!)" : " (Warten auf Spielleiter...)") : "");
		btnDemSpielBeitreten.setEnabled(!b);
		//btnZurcktreten.setEnabled(b);
		textField.setEnabled(!b);
		boolean gm = (RiskClientGUI.getSpieler().isGameMaster() ? true : false);
		btnPartieStarten.setVisible(gm);
	}

	/** Rückgabe des Loginstatus */
	public static boolean getLoginState() {	return loginState; }
	
	/** Update-Methode vom PanelInterface */
	public void updateYourself() {
		btnPartieStarten.setEnabled((ClientEngineNetwork.getEngine().getLoggedInSpieler() >= 3 ? true : false));
		updateTable();
		// Vor Spielstart:
				if ((RiskClientGUI.getSpieler().getState().equals(SpielerState.WAIT_FOR_TURN) 
						||	RiskClientGUI.getSpieler().getState().contains(SpielerState.ON_TURN))) {
					RiskClientGUI gui = RiskClientGUI.getInstance();
					// Init-Befehl. Wechselt von Login- auf MainPanel.
					gui.setTitle(RiskClientGUI.getSpieler().getName() + " spielt... " + RiskClientGUI.titel);
					gui.changePanel(new MainPanel());
					
					ClientEngineNetwork.getEngine().removeObserver(this);
					
					gui.getCurrentFrame().updateYourself();
				}
	}

	/** Update-Methode vom Observer-Interface */
	public void update(Observable o, Object arg) {
		int mode = (Integer) ((Result)arg).pop();
		if (mode == ChangeConst.SPIELERLOGOUT ||
			mode == ChangeConst.SPIELERLOGIN ||
			mode == ChangeConst.MAPUPDATE)
				updateYourself();
	}
	
}
