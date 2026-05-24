package risk.server;

import risk.server.net.ServerEngineNetwork;

import javax.swing.*;

/**
 * Die ausführende Klasse auf Serverseite. Diese Klasse beinhaltet die main()-Methode,
 * über die ein Server gestartet werden kann und dann weitere Abläufe einleitet.
 * @author Marcel
 */
public class Server {
	/** main(). Als Parameter kann ein anderer Host angegeben werden, dies ist aber optional (Standard: localhost) */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				// Neue Serverimplementierung starten!
				new ServerEngineNetwork();
			} catch (Exception e) {
				e.printStackTrace();
			}
        });
	}
}
