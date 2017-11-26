import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
		JButton b = new JButton(); //USE FLOWLAYOUT FOR THE STONES
		
		
		JPanel theBoard = new JPanel(); //the board is the mancala board
		theBoard.setLayout(new BorderLayout());
		theBoard.setSize(800, 800);
		
		for(int i = 0; i < theModel.getCurrentData().length; i++) //should be i<14
		{
			if(i == 6) //this is a score pit
			{
				
			}
		}
	}
	
	
	
	
	
}
