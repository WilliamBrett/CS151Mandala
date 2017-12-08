
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * CS151 Project
 * @author Kenny Huynh, Vincent Hang, Christopher Nguyen
 * @copyright 2017
 * @version 1.0
 */

/**
 * Represents the main method to run the program.
 */
public class MainTester {
		static public JFrame styleSelectFrame;
		static public MancalaModel model;
		
	/**
	 * Main method to run the program.
	 * @param args Arguments inputted.
	 */
	public static void main(String[] args) {
		model = new MancalaModel();
		
		//here is our opening style select frame
		styleSelectFrame = new JFrame();
		styleSelectFrame.setSize(400, 100);
		//this sets the overall frame to list top down
		JTextArea openingText = new JTextArea("Welcome to Mancala. Please choose your style.");
		JButton circleView =  new JButton("Circle");
		JButton squareView = new JButton("Square");
		
		//thePanel is put into styleSelectFrame so that we can have our two buttons next to each other neatly
		JPanel thePanel = new JPanel();
		//the buttons creates the mancala board
		circleView.addActionListener(chooseView(new CircleStyle()));
		squareView.addActionListener(chooseView(new SquareStyle()));
		thePanel.setLayout(new GridLayout(1,2));
		thePanel.add(circleView);
		thePanel.add(squareView);
		
		//the opening text will be at the top and the two buttons will be at the bottom
		styleSelectFrame.setLayout(new BoxLayout(styleSelectFrame.getContentPane(),BoxLayout.Y_AXIS));
		styleSelectFrame.add(openingText);
		styleSelectFrame.add(thePanel);
		styleSelectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		styleSelectFrame.setVisible(true);
		
	}
	
	/**
	 * Method to determine which style the user wants.
	 * @param strat the style or strategy to be used
	 * @return an actionlistener that draws the board with the given strategy.
	 */
	public static ActionListener chooseView(BoardStyle strat) {
		//its like making an anon class but inside 
		//after you press the button, it makes the mancala board here along with model and view
		
		return new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//getting rid of the previous frame here, no longer need to see style selection
				styleSelectFrame.setVisible(false); 
				
				//making the entire frame
				JFrame board = new JFrame();
				final MancalaModel myModel = new MancalaModel();
				
				//here we are making our view, the main menu button will turn off the board and turn on the style select
				MancalaView view = new MancalaView(myModel, strat);
				view.getMainMenu().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						board.setVisible(false);
						styleSelectFrame.setVisible(true);
					}
				});
				view.getMainMenu().setVisible(false);
				
				//undo button, attach to board frame
				JPanel inputUndo = new JPanel();
				inputUndo.setLayout(new FlowLayout());
				inputUndo.setSize(600, 40);
				view.getUndoButton().setVisible(false);
				
				//prompts user for number of starting stones, attach to board frame
				JTextArea prompt = new JTextArea("Please enter number of stones: 3 or 4.");
				prompt.setEditable(false);
				JTextField input = new JTextField("", 10);
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
							} else {
								throw new Exception();
							}
						} catch (Exception error) {
							prompt.setText("Error. Either not a number or number is not 3 or 4.");
						}
					}
				});
				
				//is flow layout, goes left to right
				inputUndo.add(prompt);
				inputUndo.add(input);
				inputUndo.add(view.getUndoButton());
				inputUndo.add(view.getMainMenu());
				//board is already in borderlayout
				board.add(inputUndo, BorderLayout.NORTH);
				board.add(view, BorderLayout.CENTER);

				board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				board.setSize(1050, 600);
				board.setVisible(true);
			}
		};
	}
}