import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.*;

public class MancalaModel {

	private ArrayList<ChangeListener> theListeners;
<<<<<<< Updated upstream
	private int currentData[];
	private int oldData[];
=======
	private int theData[]; //index 0 starts at bottom left, non score slot and goes right
	
>>>>>>> Stashed changes
	
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
}
