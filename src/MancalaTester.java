
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Mancala Project
 * @author William Brett, Jeffrey Huynh, Jeong Ook Moon
 * @version 1.0
 */

/**
 * Tester
 */
public class MancalaTester {
		static public JFrame mainFrame;
		static public MancalaModel model;
		
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) 
	{
		model = new MancalaModel();
		mainFrame = new JFrame("Mancala");
		mainFrame.setSize(1200, 100);
		JButton circleView =  new JButton("Circle");
		circleView.setBackground(Color.WHITE);
		JButton squareView = new JButton("Square");
		squareView.setBackground(Color.WHITE);
				
		JPanel thePanel = new JPanel();
		circleView.addActionListener(chooseView(new CircleStrategy()));
		squareView.addActionListener(chooseView(new SquareStrategy()));
		thePanel.setLayout(new GridLayout(1,2));
		thePanel.add(circleView);
		thePanel.add(squareView);
				
		mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(),BoxLayout.Y_AXIS));
		mainFrame.add(thePanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		
	}
	
	/**
	 * 
	 * @param 
	 * @return 
	 */
	public static ActionListener chooseView(Strategy strat) {
			
		return new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//getting rid of the previous frame here, no longer need to see style selection
				mainFrame.setVisible(false); 
				
				//making the entire frame
				JFrame board = new JFrame("Mancala");
				final MancalaModel myModel = new MancalaModel();
				
				//here we are making our view, the main menu button will turn off the board and turn on the style select
				MancalaView view = new MancalaView(myModel, strat);
				view.getMainMenu().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						board.setVisible(false);
						mainFrame.setVisible(true);
					}
				});
				view.getMainMenu().setVisible(true);
				
				//undo button, attach to board frame
				JPanel inputUndo = new JPanel();
				inputUndo.setLayout(new GridLayout(1,2));
				inputUndo.setSize(600, 10);
				view.getUndoButton().setVisible(false);
				view.setVisible(true);
				//prompts user for number of starting stones, attach to board frame
				JButton prompt = new JButton("Choose # of stones: 3 or 4");
				prompt.setEnabled(false);
				prompt.setBackground(Color.WHITE);
				JTextField input = new JTextField("", 1);
				input.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String text = input.getText().trim();
						try {
							int stones = Integer.parseInt(text);
							if (stones == 3 || stones == 4) {
								myModel.setStones(stones);
								prompt.setVisible(false);
								input.setVisible(false);
								view.getUndoButton().setVisible(true);
								view.setVisible(true);
							} else {
								throw new Exception();
							}
						} catch (Exception error) {
							prompt.setText("Error. input 3 or 4");
						}
					}
				});
				JPanel undoPanel = new JPanel();
				undoPanel.setLayout(new FlowLayout());
				
				//is flow layout, goes left to right
				inputUndo.add(prompt);
				inputUndo.add(input);
				undoPanel.add(view.getUndoButton());
				undoPanel.add(view.getMainMenu());
				//board is already in borderlayout
				board.add(inputUndo, BorderLayout.NORTH);
				board.add(view, BorderLayout.CENTER);
				board.add(undoPanel, BorderLayout.SOUTH);

				board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				board.setSize(1055, 700);
				board.setVisible(true);
			}
		};
	}
}