package risk.commons.valueobjects;

import java.io.Serializable;

/**
 * Diese abstrakte Klasse ist die Oberklasse f�r alle Arten von Missionen.
 * Von ihr abgeleitete Klassen m�ssen eine eigene Siegbedingungs-
 * methode implementieren.
 * Die Missions-Objekte werden bei Spielstart automatisch erzeugt.
 * @author Marcel
 * @version 1
 */
public abstract class Mission implements Serializable {
	private static final long serialVersionUID = 217168460534931739L;
	
	/** Der Spieler, der die Mission hat */
	protected Spieler s;			
	/** Ja, was wohl... (Missionstext) */
	protected String missionstext;

	/** Konstante, die diese Mission als LandMission deklariert */
	public static final int LAND = 11;
	/** Konstante, die diese Mission als KontinentMission deklariert */
	public static final int KONTINENT = 12;
	/** Konstante, die diese Mission als BefreiungsMission deklariert */
	public static final int BEFREIUNG = 13;

	/** Siegpr�fungsmethode. Diese wird von den drei Unterklassen passend �berschrieben */
	public boolean pruefeSieg() {return false;}
	/** Setze den Missiontext */
	public void setText(String s) {missionstext = s;}
	/** Setze den Spieler mit dieser Mission */
	public void setSpieler(Spieler s) {this.s = s;}
	/** Gebe den Spieler mit dieser Mission zur�ck */
	public Spieler getSpieler() {return s;}
	/** String-Darstellung f�r println */
	public String toString() {return missionstext;}
}
