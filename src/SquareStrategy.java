
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * Mancala Project
 * @author William Brett, Jeffrey Huynh, Jeong Ook Moon
 * @version 1.0
 */

/**
 * Represents a style with square pits. A concrete strategy.
 */
public class SquareStrategy implements Strategy {

	Shape[] pits;
	int[] modelPits;
	boolean turn;
	final String mancala = "Christmas & Square Theme";

	@Override
	public Shape[] drawPits(Graphics2D g2) {
		pits = new Shape[14];
		int x = 200;
		int topY = 100;
		int botY = 250;
		for(int i = 0; i < 6; i++) {
			pits[i] = new Rectangle2D.Double(x, botY+20, 75, 75); //this draws the bottom pits. <-- changeme!
			pits[12-i] = new Rectangle2D.Double(x, topY, 75, 75); //this draws the top pits. <-- changeme!
			x += 100;
		}
 
		pits[6] = new Rectangle2D.Double(850, topY, 125, 250); //firstPlayer mancala <-- changeme!
		pits[13] = new Rectangle2D.Double(50, topY, 125, 250); //secondPlayer mancala <-- changeme!

		return pits;
	}

	@Override
	public void drawBoard(Graphics2D g2, MancalaModel model) {
		modelPits = model.getData();
		turn = model.getTurn();
		int stones;

		String status = "";
		int x = 220;
		int y = 150;
		g2.setColor(Color.DARK_GRAY);
		g2.setFont(new Font("Courier New", Font.BOLD, 23));
		g2.drawString(mancala, 340, 30);
		
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Courier New", Font.BOLD, 20));
		g2.drawString("Undo#(" + model.p2UndoNum +")", 75, 380);
		g2.drawString("Undo#(" + model.p1UndoNum +")",875, 380);

		g2.drawString("B", 110, 95);
		g2.drawString("A", 910, 95);

		//used for labeling each pit. B6 B5 B4 ... <--changeme!
		for(int i = 1; i < 7; i++) {
			g2.setColor(new Color(100,0,0));
			g2.drawString("B" + (7-i), x+9, 95);
			g2.setColor(new Color(0,0,100));
			g2.drawString("A" + (i), x+9, 380);
			g2.setColor(Color.BLACK);
			x += 100;
		}

		//drawing the stones in each pit
		for(int i = 0; i < pits.length; i++) {
			stones = modelPits[i];
			g2.setColor(Color.WHITE);
			g2.fill(pits[i]);
			g2.setColor(Color.gray);
			g2.draw(pits[i]);

			if(i!= 6 && i!= 13){//here we fill in the graphics representing stones in all pits except 6 and 13
				for(int j = 0; j < stones; j++) {
					Shape s = new Rectangle2D.Double((float) pits[i].getBounds2D().getMinX() + (Math.random() * 50) + 10,
							(float) pits[i].getBounds2D().getMinY() + (Math.random() * 50) + 10, 15, 15);
					g2.setColor(new Color((int)(Math.random()*254),(int)(Math.random()*254),(int)(Math.random()*254)));
					g2.fill(s);
					g2.setColor(Color.DARK_GRAY);
					g2.draw(s);
				}
			}
			if(i==6){ //6 and 13 represent the player's mancalas, having different size limitations than regular pits
				for(int j = 0; j < modelPits[i]; j++) {
					g2.setColor(new Color((int)(0),(int)(Math.random()*50+100),(int)(0)));//r
					Shape s = new Rectangle2D.Double((float) pits[i].getBounds2D().getMinX() + (Math.random() * 100) + 10,
							(float) pits[i].getBounds2D().getMinY() + (Math.random() * 200) + 10, 15, 15) ;
					g2.fill(s);
					g2.setColor(Color.DARK_GRAY);
					g2.draw(s);
				}
			}
			if(i==13){ //6 and 13 represent the player's mancalas, having different size limitations than regular pits
				for(int j = 0; j < modelPits[i]; j++) {
					g2.setColor(new Color((int)(Math.random()*50+100),(int)(0),(int)(0)));//r
					Shape s = new Rectangle2D.Double((float) pits[i].getBounds2D().getMinX() + (Math.random() * 100) + 10,
							(float) pits[i].getBounds2D().getMinY() + (Math.random() * 200) + 10, 15, 15); 
					g2.fill(s);
					g2.setColor(Color.DARK_GRAY);
					g2.draw(s);
				}
			}
			//if(i==6 | i==13){ //6 and 13 represent the player's mancalas, having different size limitations than regular pits
			//	for(int j = 0; j < modelPits[i]; j++) {
			//		g2.setColor(new Color((int)(Math.random()*254),(int)(Math.random()*254),(int)(Math.random()*254)));//r
			//		g2.fill(new Rectangle2D.Double((float) pits[i].getBounds2D().getMinX() + (Math.random() * 100) + 10,
			//				(float) pits[i].getBounds2D().getMinY() + (Math.random() * 200) + 10, 15, 15));
			//	}
			//}
			
			g2.setColor(Color.gray); //prepares the font, ensuring strings drawn are consistant in style, size and color
			g2.setFont(new Font("Courier New", Font.BOLD, 15));
			g2.drawString(Integer.toString(modelPits[i]), (float)pits[i].getBounds2D().getCenterX(), (float)pits[i].getBounds2D().getMaxY());
			//g2.drawString("<-- Player B", 425, 60);
			//g2.drawString("--> Player A", 425, 400);

			//Projects a score sheet and announcement of who wins upon win condition 
			if (model.gameOver) {

				if(model.p1win) {
					g2.setColor(new Color(0,0,150));
					g2.drawString("Player A wins!", 425, 415);
				} else if(model.p2win) {
					g2.setColor(new Color(150,0,0));
					g2.drawString("Player B wins!", 425, 415);
				} else {
					g2.setColor(Color.DARK_GRAY);
					g2.drawString("Tied game!", 425, 415);
				}
				g2.setColor(Color.DARK_GRAY);
				g2.drawString("Player A score: " + modelPits[6], 425, 440);
				g2.drawString("Player B score: " + modelPits[13], 425, 460);
				g2.setFont(new Font("Courier New", Font.BOLD, 25));

			}

			//displays who's turn it currently is
			if (!model.gameOver) {
				if (turn) {
					g2.setColor(new Color(0,0,150));
					status = "Player A's Turn";
					g2.drawString(status, 425, 400);//800, 20
					g2.setColor(Color.BLACK);
				} else {
					g2.setColor(new Color(150,0,0));
					status = "Player B's Turn";
					g2.drawString(status, 425, 60);//800, 20
					g2.setColor(Color.BLACK);
				}
			}

			if(model.error) {
				g2.drawString("ERROR: " + model.getErrorMessage(), 70,440);
			}
		}
	}

}

//A:dark blue  setColor(new Color(0,0,150)), DARKDARKBLUE new Color(0,0,100)
//B:dark red   setColor(new Color(150,0,0)), DARKDARKRED  new Color(100,0,0)