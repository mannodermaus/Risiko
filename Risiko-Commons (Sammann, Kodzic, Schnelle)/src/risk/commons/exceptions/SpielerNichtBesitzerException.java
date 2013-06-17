package risk.commons.exceptions;

import risk.commons.valueobjects.Land;
import risk.commons.valueobjects.Spieler;

/**
 * Eine Instanz dieser Klasse wird dann geworfen, wenn ein Spieler auf ein
 * Land in der Weltkarte klickt (bspw. um es zu verst�rken), das ihm aber
 * gar nicht geh�rt.
 * @author Marcel
 */
public class SpielerNichtBesitzerException extends Exception {
	private static final long serialVersionUID = 1L;

	public SpielerNichtBesitzerException(Spieler name, Land l) { super(l + " geh�rt " + name + " und nicht dir!"); }
}
