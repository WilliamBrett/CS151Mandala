

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
 * CS151 Project
 * @author Kenny Huynh. Vincent Hang, Christopher Nguyen
 * @copyright 2017
 * @version 1.0
 */

/**
 * Represents the GUI portion or context of the Strategy pattern. This is where the model will interact with the controller and update the view.
 */
public class MancalaView extends JPanel implements ChangeListener {
	
	private MancalaModel model;
	private BoardStyle style;
	private Shape[] pits;
	private JButton mainMenu;
	private JButton undo;
	
	/**
	 * Intializes a View object.
	 * @param model the model to be used
	 * @param style the strategy/style to be used for this view.
	 */
	public MancalaView(MancalaModel model, BoardStyle style) {
		this.model = model;
		this.style = style;
		this.model.attach(this);
		pits = style.drawPits();
		this.setVisible(true);
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				for(int i = 0; i < pits.length; i++) { //check to see if the point is clicked within the pit.
					if(pits[i].contains(e.getPoint())) {
						model.moveBoard(i);
					}
				}
			}
		});
		
		//put undo/mainMenu in View class so that it updates automatically due to the state change method.
		undo = new JButton("Undo");
		mainMenu = new JButton("Main Menu");
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.undo();
			}
		});
	}
	
	/**
	 * Gets the undo JButton
	 * @return the undo JButton
	 */
	public JButton getUndoButton() {
		return undo;
	}
	
	/**
	 * Gets the main menu JButton
	 * @return the main menu JButton
	 */
	public JButton getMainMenu() {
		return mainMenu;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		pits = this.style.drawPits();
		style.drawBoard(g2, this.model);
		
		//if game is over, make undo disappear and mainMenu appear.
		if(model.gameIsOver) {
			undo.setVisible(false);
			mainMenu.setVisible(true);
		}
	}
	
	@Override
	//this will update the view whenever the board's state is changed.
	public void stateChanged(ChangeEvent arg0) {
		this.repaint();
		
	}
	
}
/*
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MancalaView extends JFrame implements ChangeListener 
{
	private MancalaModel theModel;
	
	//this is the view AND controller portions
	
	//the construcor will also run through the style selection and number of starting stones
	//implement later
	MancalaView(MancalaModel r_model)
	{
		theModel = r_model;
		
		this.setLayout(new FlowLayout());
		this.setSize(1000, 1000); //this is the size of our overall frame
		createBoard();
	}
	public MancalaModel getModel()
	{
		return theModel;
	}
	public void setModel(MancalaModel r_model)
	{
		theModel = r_model;
	}
	@Override
	public void stateChanged(ChangeEvent c) 
	{
			
	}

	public void createBoard()
	{
		//ADD BUTTONS IN THE FOR LOOP BELOW 
		 //USE FLOWLAYOUT FOR THE STONES
		
		JPanel theBoard = new JPanel(); //the board is the mancala board
		theBoard.setLayout(new BorderLayout());
		theBoard.setSize(800, 800);
		
		JPanel bottomPits = new JPanel();  //this panel is for holding the bottomPits, put into theBoard
		bottomPits.setLayout(new FlowLayout());
		
		JPanel topPits = new JPanel(); //this panel is for holding the topPits, put into theBoard
		topPits.setLayout(new FlowLayout());
		
		JPanel rightPit = new JPanel(); //this panel is for holding player1's stones
		rightPit.setLayout(new FlowLayout());
		
		JPanel leftPit = new JPanel(); //this panel is for holding player2's stones
		leftPit.setLayout(new FlowLayout());
		
		for(int i = 0; i < theModel.getCurrentData().length; i++) //should be i<14
		{
			if(i < 6) //this is a score pit
			{
				JButton b = new JButton();//this is the button to be added
				//LATER MUST ADD FUNCTIONALITY TO THE BUTTON. USE ACTION LISTENER
				//THESE BOTTOM PITS MUST MAKE STONE TRAVEL RIGHT
				
				JPanel stoneDisplay = new JPanel();  //this displays the stones inside the pits
				stoneDisplay.setLayout(new FlowLayout());
				b.add(stoneDisplay); //the button now has correctly displayed stones inside
				
				bottomPits.add(b); //the bottom row now has a correctly done pit.
			}
			if(i > 6 && i < 13) //greater than 6 because gotta skip player 1 score pit and less than 13 because 13 is player 2's score pit
			{
				JButton b = new JButton();//this is the button to be added
				//LATER MUST ADD FUNCTIONALITY TO THE BUTTON. USE ACTION LISTENER
				//THESE TOP PITS MUST MAKE STONES TRAVEL LEFT
				
				JPanel stoneDisplay = new JPanel();  //this displays the stones inside the pits
				stoneDisplay.setLayout(new FlowLayout());
				b.add(stoneDisplay); //the button now has correctly displayed stones inside
				
				topPits.add(b); //the bottom row now has a correctly done pit.
			}
			
		} //end of making the bottom and top pits
		
		//making number display here
		JPanel middleDisplay = new JPanel(); //this is used to display the pit labels for both sides
		GridLayout middleDisplayLayout = new GridLayout(0,6); //the layout, will display the pitnumbers in grid format
		middleDisplayLayout.setHgap(5); //makes sure there is space between numbers
		middleDisplay.setLayout(middleDisplayLayout);
		
		for(int i = 7; i < 13; i++) //this loop is putting in the numbers
		{
			String number = "" + i;
			JTextArea num = new JTextArea(number);
			num.setSize(5, 5);
			middleDisplay.add(num);
		}
		for(int i = 0; i < 6; i++) //this loop is putting in the numbers
		{
			String number = "" + i;
			JTextArea num = new JTextArea(number);
			num.setSize(5, 5);
			middleDisplay.add(num);
		}
		
		//end of  number display making
		
		theBoard.add(bottomPits, BorderLayout.SOUTH);
		theBoard.add(topPits, BorderLayout.NORTH);
		theBoard.add(rightPit, BorderLayout.EAST);
		theBoard.add(leftPit, BorderLayout.WEST);
		theBoard.add(middleDisplay, BorderLayout.CENTER);
		
		this.add(theBoard);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	
	
	
}
*/