import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * CS151 Project
 * @author Kenny Huynh, Vincent Hang, Christopher Nguyen
 * @copyright 2017
 * @version 1.0
 */

/**
 * Represents a style with circle pits, a concrete strategy of the strategy
 * pattern.
 */
public class CircleStyle implements BoardStyle {
	public Shape[] pits;
	int[] modelPits;
	boolean turn;
	final String mancala = "MANCALA"; //used for drawing out MANCALA next to each mancala.
	@Override
	// this method creates the pits/mancalas and stores it in a shape array.
	public Shape[] drawPits(Graphics2D g2) {
		pits = new Shape[14];
		int x = 200;
		int topY = 100;
		int botY = 250;
		
		//this draws the bottom pits
		for (int i = 0; i < 6; i++) {
		
			Ellipse2D thePit = new Ellipse2D.Double(x, botY, 100, 100);
			pits[i] = thePit;
			pits[12- i] = new Ellipse2D.Double(x, topY, 100, 100); // this draws the top pits.

			 // this draws the bottom pits.
			x += 100;												
		}
		
		
		pits[6] = new Ellipse2D.Double(800, topY, 150, 250); // firstPlayer mancala
		pits[13] = new Ellipse2D.Double(50, topY, 150, 250); // secondPlayer  mancala

		return pits;
	}

	@Override

	// this will iterate the shape array and draw the shapes. Using a number in
	// the center of the pits for now.
	public void drawBoard(Graphics2D g2, MancalaModel model) {
		modelPits = model.getData();
		turn = model.getTurn();
		int stones;
		String status = "";
		int x = 240;
		int y = 150;
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g2.drawString("Undos: " + model.p2UndoNum, 100, 80);
		g2.drawString("Undos: " + model.p1UndoNum, 850, 80);
		//this for loop is used for drawing MANCALA next to the mancala pits.
		for(int i = 0; i < mancala.length(); i++) {
			g2.drawString(mancala.substring(i, i+1), 20, y);
			g2.drawString(mancala.substring(i,i+1), 970, y);
			y += 20;
		}
		g2.drawString("B", 20, y + 30);
		g2.drawString("A", 970, y + 30);
		
		//used for labeling each pit. B6 B5 B4 ...
		for(int i = 1; i < 7; i++) {
			g2.drawString("B" + (7-i), x, 85);
			g2.drawString("A" + (i), x, 375);
			x += 100;
		}
		
		//this draws each pit and the stones inside respective pits
		for (int i = 0; i < pits.length; i++) {
			//draws pits
			g2.setColor(Color.WHITE);
			g2.fill(pits[i]);
			g2.draw(pits[i]);
			
			//draws stones in each pit
			stones = modelPits[i];
			//this is bottom pits
			if(i <6)
			{
				for (int j = 0; j < stones; j++) {
					//this sets the stones to a random RBG color value that is grey ish
					g2.setColor(new Color((int) (Math.random()* 254) ,(int) (Math.random()* 254), (int) (Math.random()* 254)));
					//set to fill instead of draw so it draws the circles already filled with a color.
					g2.fill(new Ellipse2D.Double( (float) pits[i].getBounds2D().getMinX() + (Math.random() * 50) + 20,
							(float) pits[i].getBounds2D().getMinY() + (Math.random() * 50) + 10, 20, 20));
				}
			}
			//this is mancala A
			else if(i ==6)
			{
				for (int j = 0; j < stones; j++) {
					//this sets the stones to a random RBG color value
					int colorValue = (int) (Math.random() * 60 + 105);
					g2.setColor(new Color(colorValue, colorValue, colorValue));
					//set to fill instead of draw so it draws the circles already filled with a color.
					g2.fill(new Ellipse2D.Double( (float) pits[i].getBounds2D().getMinX() + (Math.random() * 50) + 20,
							(float) pits[i].getBounds2D().getMinY() + (Math.random() * 50) + 10, 20, 20));
				}
			}
			//this is mancala B
			else if(i < 13){
				for (int j = 0; j < stones; j++) {
					//this sets the stones to a random RBG color value
					g2.setColor(new Color((int) (Math.random()* 254),(int) (Math.random()* 254), (int) (Math.random()* 254)));
					//set to fill instead of draw so it draws the circles already filled with a color.
					g2.fill(new Ellipse2D.Double( (float) pits[i].getBounds2D().getMinX() + (Math.random() * 50) + 20,
							(float) pits[i].getBounds2D().getMinY() + (Math.random() * 50) + 10, 20, 20));
				}
			}
			else if(i == 13){
				for (int j = 0; j < stones; j++) {
					//this sets the stones to a random RBG color value
					g2.setColor(new Color((int) (Math.random()* 20),(int) (Math.random()* 20), (int) (Math.random()* 20)));
					//set to fill instead of draw so it draws the circles already filled with a color.
					g2.fill(new Ellipse2D.Double( (float) pits[i].getBounds2D().getMinX() + (Math.random() * 50) + 20,
							(float) pits[i].getBounds2D().getMinY() + (Math.random() * 50) + 10, 20, 20));
				}
			}
			
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g2.drawString(Integer.toString(modelPits[i]), (float) pits[i].getBounds2D().getCenterX(),
					(float) pits[i].getBounds2D().getCenterY());
			//g2.drawString("<-- Player B", 425, 60);
			//g2.drawString("--> Player A", 425, 400);
			
			//this is used to show the winner after the game is over. 
			if (model.gameOver) {
				g2.drawString("Game Over!", 450, 420);
				g2.drawString("Player A score: " + modelPits[6], 425, 440);
				g2.drawString("Player B score: " + modelPits[13], 425, 460);
				g2.setFont(new Font("TimesRoman", Font.BOLD, 25));
				if(model.p1win) {
					g2.drawString("Player A wins!", 425, 480);
				} else if(model.p2win) {
					g2.drawString("Player B wins!", 425, 480);
				} else {
					g2.drawString("Tied game!", 425, 480);
				}
			}
			
			//only display turns when the game isn't over yet.
			if (!model.gameOver) {
				if (turn) {
					status = "[Player A's Turn]";
					g2.setColor(Color.RED);
					g2.setFont(new Font("TimesRoman", Font.BOLD, 30));
					g2.drawString(status, 780, 400);
				} else {
					status = "[Player B's Turn]";
					g2.setColor(Color.GREEN);
					g2.setFont(new Font("TimesRoman", Font.BOLD, 30));
					g2.drawString(status, 50, 400);
				}
			}
			if(model.error) {
				g2.drawString("ERROR: " + model.getErrorMessage(), 350,450);
			}
		}
	}
}