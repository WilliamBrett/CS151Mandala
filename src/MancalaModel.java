import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.*;

public class MancalaModel {

	private ArrayList<ChangeListener> theListeners;

	private int currentData[];
	private int oldData[];

	
	/*
	 * this is the model class
	 */
	//make sure when MancalaModel is created, the array is of size 14
	MancalaModel(int[] theData)
	{
		currentData = theData;
		oldData = currentData;
		theListeners = new ArrayList<ChangeListener>();
	}
	
	public int[] getCurrentData()
	{
		return currentData;
	}
	
	public int[] getOldData()
	{
		return oldData;
	}
	public void setCurrentData(int r_data[])
	{
		currentData = r_data;
	}
	public void setOldData(int r_data[])
	{
		oldData = r_data;
	}
	
	public void attach(ChangeListener e)
	{
		theListeners.add(e);
	}
	
	public void update()
	{
		for(ChangeListener e: theListeners)
		{
			e.stateChanged(new ChangeEvent(this));
		}
	}
	//checks if a pit is empty
	public boolean isEmpty(int index)
	{
		if(currentData[index]==0)
		{
			return true;
		}
		else
			return false;
	}
	// checks if the game is ended
	public boolean gameEnd()
	{
		// if either bottom pits or top pits are empty, the game is ended
		if(getBottomStones() == 0 || getTopStones() == 0)
			return true;
		// if not, then game is not ended yet
		else
			return false;
	}
	// collects all the bottom stones
	public int getBottomStones()
	{
		int result = 0;
		for(int i=0; i<6; i++)
		{
			result = currentData[i] + result;
		}
		return result;
	}
	// collects all the top stones
	public int getTopStones()
	{
		int result = 0;
		for(int i=7; i<13; i++)
		{
			result = currentData[i] + result;
		}
		return result;
	}
	// puts all leftover stones into correct player's pit
	public void afterGameEnd()
	{
		// collect leftover stones in the bottom row
		int player1Stones = getBottomStones();
		// if there is leftover, put them into player1's pit
		if(player1Stones != 0)
		{
			currentData[6] = player1Stones;
			for(int i=0; i<6; i++)
			{
				currentData[i] = 0;
			}
		}
		// collect leftover stones in the top row
		int player2Stones = getTopStones();
		// if there is leftover, put them into player2's pit
		if(player2Stones != 0)
		{
			currentData[13] = player2Stones;
			for(int i=7; i<13; i++)
			{
				currentData[i] = 0;
			}
		}
	}
	// saves currentData into oldData
	public void saveCurrentData()
	{
		oldData = currentData;
	}
	// restores oldData into currentData
	public void undo()
	{
		currentData = oldData;
	}
	//
}
