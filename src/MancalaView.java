import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Mancala Project
 * @author William Brett, Jeffrey Huynh, Jeong Ook Moon
 * @version 1.0
 */

/**
 * View
 */
public class MancalaView extends JPanel implements ChangeListener {
	
	private MancalaModel model;
	private BoardStyle style;
	private Shape[] pits;
	private JButton mainMenu;
	private JButton undo;
	
	/**
	 * Intializes a View object.
	 * @param model the model
	 * @param style strategy pattern, can take any BoardStyle
	 */
	public MancalaView(MancalaModel model, BoardStyle style) {
		this.model = model;
		this.style = style;
		this.model.attach(this);
		
		this.repaint();
		this.setVisible(true);
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				for(int i = 0; i < pits.length; i++) {
					if(pits[i].contains(e.getPoint())) {
						model.play(i);
					}
				}
			}
		});
		
		// undo controller
		undo = new JButton("Undo");
		undo.setBackground(Color.WHITE);
		mainMenu = new JButton("Main Menu");
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.undo();
			}
		});
	}
	
	/**
	 * getUndoButton
	 * @return undo
	 */
	public JButton getUndoButton() {
		return undo;
	}
	
	/**
	 * getMainMenu
	 * @return mainMenu
	 */
	public JButton getMainMenu() {
		return mainMenu;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		pits = this.style.drawPits(g2);
		style.drawBoard(g2, this.model);
		
		if(model.gameOver) {
			undo.setVisible(false);
			mainMenu.setVisible(true);
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		this.repaint();
		
	}
	
}