
import java.awt.Graphics2D;
import java.awt.Shape;
/**
 * CS151 Project
 * @author Kenny Huynh, Vincent Hang, Christopher Nguyen
 * @copyright 2017
 * @version 1.0
 */

/**
 * Interface to draw the pits using the strategy pattern. 
 */
public interface BoardStyle {
	
	public Shape[] drawPits(Graphics2D g2);
	
	public void drawBoard(Graphics2D g2, MancalaModel model);
}