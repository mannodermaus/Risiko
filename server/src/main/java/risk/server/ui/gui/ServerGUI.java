package risk.server.ui.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import risk.commons.RiskUtils;
import risk.commons.valueobjects.Spieler;
import risk.server.net.ServerEngineNetwork;

/**
 * Diese Klasse definiert ein kleines Fensterchen, das als graphische Repräsentation des Servers
 * in der RISK-Anwendung steht. Es besteht aus einer Tabelle mit den angemeldeten Spielern
 * und einem Knopf zum Abbrechen des Servers.
 * @author Marcel
 */
public class ServerGUI extends JFrame {
	private static final long serialVersionUID = -2664254163893543646L;
	
	/** Inhalts-Panel */
	private JPanel contentPane;
	/** Textfeld mit Status des Servers */
	private JTextField txtStatus;
	/** Button zum Herunterfahren des Servers */
	private JButton btnSpielAbbrechen;
	/** ScrollPane mit der Tabelle */
	private JScrollPane scrollPane;
	/** Tabelle */
	private JTable table;
	/** Anbindung an die Server-Implementierung */
	private ServerEngineNetwork server;

	/** Erzeugt die GUI und setzt wichtige Eigenschaften. */
	public ServerGUI(final ServerEngineNetwork server) throws IOException {
		// Allg. Eigenschaften und Attribute setzen
		this.server = server;
		setResizable(false);
		setTitle("RISKSERVER");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 400, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Icon setzen
		RiskUtils.setIconImage(this, "images/riskicon_server.png");
		
		// Statustextfeld
		txtStatus = new JTextField(server.getStatus());
		txtStatus.setEnabled(false);
		txtStatus.setEditable(false);
		txtStatus.setText("Status");
		contentPane.add(txtStatus, BorderLayout.NORTH);
		txtStatus.setColumns(10);
		
		// Buttons und ihre Listener
		btnSpielAbbrechen = new JButton("Spiel abbrechen");
		btnSpielAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				server.disconnectGame();
			}
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				server.disconnectGame();
				super.windowClosing(e);
			}
		});
		contentPane.add(btnSpielAbbrechen, BorderLayout.SOUTH);
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		// Tabelle mit allen Spielern
		table = new JTable();
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
		scrollPane.setViewportView(table);
		updateGUI();
		
		setVisible(true);
	}
	
	/** Update-Methode, welche die Tabelle aktualisiert */
	public void updateGUI() {
		// Status-TXT
		txtStatus.setText(server.getStatus());
		
		// Table
		table.setVisible(false);
		DefaultTableModel model = new DefaultTableModel(0,3);
				
		// Spieler-Array ziehen
		Spieler[] spieler = server.getWelt().getSpieler();
		Vector<Vector<String>> rows = new Vector<>(6);
		Vector<Object> columns = new Vector<>();
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
}
