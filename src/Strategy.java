
import java.awt.Graphics2D;
import java.awt.Shape;
/**
 * Mancala Project
 * @author William Brett, Jeffrey Huynh, Jeong Ook Moon
 * @version 1.0
 */

/**
 * Interface for strategy pattern. 
 */
public interface Strategy {
	
	public Shape[] drawPits(Graphics2D g2);
	
	public void drawBoard(Graphics2D g2, MancalaModel model);
}