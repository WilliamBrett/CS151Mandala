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
		JPanel middleDisplay = new JPanel(); //this is used to display the pit labels for both sides
		GridLayout middleDisplayLayout = new GridLayout(); //the layout, will display the pitnumbers in grid format
		middleDisplayLayout.setHgap(50); //makes sure there is space between numbers
		middleDisplayLayout.setColumns(6); //makes sure there is 6 columns max
		middleDisplay.setLayout(middleDisplayLayout);
		
		for(int i = 0; i < 6; i++) //this loop is putting in the numbers
		{
			middleDisplay.add(new JTextField(i));
		}
		for(int i = 7; i < 13; i++) //this loop is putting in the numbers
		{
			middleDisplay.add(new JTextField(i));
		}
		
		theBoard.add(bottomPits);
		theBoard.add(topPits);
		theBoard.add(rightPit);
		theBoard.add(leftPit);
		
		this.add(theBoard);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	
	
	
}
