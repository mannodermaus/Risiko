package risk.commons.valueobjects;

import java.io.Serializable;

import java.util.Vector;
import java.util.ArrayList;

import risk.commons.SpielerState;

/**
 * Jedem Teilnehmer an der Partie wird ein Objekt der Klasse Spieler
 * bereitgestellt. Es enth�lt Angaben zum Spieler, wie Name und Farbe,
 * h�lt aber auch seine aktuellen Karten und die L�nder in seinem
 * Besitz bereit.
 * @author Marcel
 */
public class Spieler implements Serializable {
	private static final long serialVersionUID = 7549938622502852871L;
	
	/** Wenn true, ist der Spieler Game Master und hat einige weitere Privilegien */
	private boolean gameMaster;							
	/** Aktueller Status des Clients, dem dieser Spieler zugeh�rt */
	private String state = SpielerState.NOT_LOGGED_IN;
	/** Name */
	private String name;						
	/** Farbe der Spielfiguren f�r GUI (Identifikation) */
	private int farbe;							
	/** Farbenname in Textform */
	private String farbtext;					
	/** Liste seiner aktuellen Karten */
	private ArrayList<Karte> handkarten;		
	/** Liste der "eigenen" L�nder */
	private ArrayList<Land> besitz;				
	/** Siegbedingung */
	private Mission mission;					
	/** true, wenn der Spieler in seinem Zug ein Land besetzt hat (wird nach dem Zug zur�ck gesetzt) */
	private boolean siegreich = false;			
	
	/** Konstruktor
	 * @param name Name */
	public Spieler(String name) {
		this.name = name;
		this.besitz = new ArrayList<Land>();
		this.handkarten = new ArrayList<Karte>();
	}

	/** Setzen der int-Farbe und der Textrepr�sentation der Farbe f�r diesen Spieler */
	public void setFarben(@SuppressWarnings("rawtypes")Vector farbe) {
		this.farbe = (Integer) farbe.elementAt(0);
		this.farbtext = (String) farbe.elementAt(1);
	}

	/** Gib' den Namen des Spielers zur�ck */
	public String getName() { return name; }

	/** Lege die Mission des Spielers fest */
	public void setMission(Mission m) { this.mission = m; }
	/** Gib' die Mission des Spielers aus */
	public Mission getMission() { return mission; }

	/** Gib' die Spielerfarbe dieses Spielers aus */
	public int getFarbe() { return this.farbe; }

	/** Gib' die Textrepr�sentation der Farbe dieses Spielers aus */
	public String getFarbtext() { return farbtext; }

	/** Gib alle L�nder im Besitz des Spielers in einer Liste aus */
	public ArrayList<Land> getBesitz() { return besitz; }
	/** Setze den Besitz des Spielers auf die �bergebene Liste */
	public void setBesitz(ArrayList<Land> besitz) {	this.besitz = besitz; }

	/** Gib' alle Handkarten des Spielers in einer Liste aus */
	public ArrayList<Karte> getKarten() { return handkarten; }
	/** Entferne eine Karte aus der Hand des Spielers und gib' zur�ck, ob es geklappt hat */
	public boolean removeKarte(Karte k) {
		if (handkarten.contains(k)) {
			handkarten.remove(k);
			return true;
		}
		return false;
	}
	/** F�ge der Hand des Spielers eine neue Karte hinzu */
	public void addKarte(Karte k) { handkarten.add(k); }

	/** Gib' aus, ob der Spieler in diesem Zug schon erfolgreich ein Land eingenommen hat oder nicht */
	public boolean getSiegreich() { return siegreich; }
	/** Setze den Siegreich-Status des Spielers auf den �bergebenen boolean */
	public void setSiegreich(boolean b) { siegreich = b; }

	/** Gib' an, ob dieser Spieler der Spielleiter ist oder nicht */
	public boolean isGameMaster() {	return gameMaster; }
	/** Setze den Spielleiter-Status dieses Spielers auf den �bergebenen boolean */
	public void setGameMaster(boolean b) { gameMaster = b; }

	/** Gib' den aktuellen STATE des Spielers aus
	 * @see risk.commons.SpielerState */
	public String getState() { return state; }
	/** Setze den STATE des Spielers auf den �bergebenen State
	 * @see risk.commons.SpielerState */
	public void setState(String neu) { state = neu; }

	@Override
	/** String-Darstellung f�r println */
	public String toString() { return name; }
	
	@Override
	/** Gleichheitspr�fung zwischen zwei Spieler-Objekten. Zwei Spieler-Objekte sind genau dann gleich, wenn sie die gleiche Farbe oder den gleichen Namen haben */
	public boolean equals(Object obj) {
		if (obj instanceof Spieler) {
			Spieler objS = (Spieler) obj;
			return (this.farbe == objS.getFarbe()) || (this.name.equals(objS.getName()));
		} else return false;
	}

	/** Diese Methode kann dazu benutzt werden, die Eigenschaften des ersten �bergebenen Spielers auf den zweiten zu �bertragen.
	 * @param von	Spieler, von dem �bertragen wird
	 * @param nach	Spieler, auf den �bertragen wird */
	public static void copy(Spieler von, Spieler nach) {
		nach.setGameMaster(von.isGameMaster());
		nach.setState(von.getState());
		nach.setBesitz(von.getBesitz());
	}
}
