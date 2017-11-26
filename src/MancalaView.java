import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MancalaView extends JFrame implements ChangeListener 
{
	private MancalaModel theModel;
	
	//this is the view AND controller portions
	
	MancalaView(MancalaModel r_model)
	{
		theModel = r_model;
		
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
		
		for(int i = 0; i < theModel.getCurrentData().length; i++)
		{
			
		}
	}
	
	
	
	
	
}
