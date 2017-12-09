import java.util.ArrayList;
import javax.swing.event.*;

/**
 * Mancala Project
 * @author William Brett, Jeffrey Huynh, Jeong Ook Moon
 * @version 1.0
 */

/**
 * MancalaModel holds all necessary data and data structure to run Mancala game
 */
public class MancalaModel 
{
	private int pits[], prevpits[];
	private ArrayList<ChangeListener> listeners;
	public int clickedPit,clickedPitStones, lastTraverseI, oppositeStones, p1UndoNum, p2UndoNum;
	public String errorMsg;
	public boolean p1turn, p2turn, p1win, p2win, tie, gameStart, gameOver, error, case2;
	

	/**
	 * initializes all variables
	 */
	public MancalaModel() 
	{
		listeners = new ArrayList<ChangeListener>();
		prevpits = pits = new int[14];
		errorMsg = "";
		p1UndoNum = p2UndoNum = 3;
		clickedPitStones = clickedPit = lastTraverseI = oppositeStones = 0;
		case2 = tie = p1win = p2win = p2turn = gameStart = gameOver = error = false;
		for(int i = 0; i < pits.length; i++) 
		{
			pits[i] = 0;
		}
		prevpits = pits;
		p1turn = true;
	}
	
	/**
	 * whenever pit is clicked this method moves stones and makes game forward according to each situation
	 * @param i = index of clicked pit
	 */
	public void play(int i) 
	{
		// checks if the game is over
		if(p1win || p2win || tie) 
			return;
		
		// gives error message when mancalas are clicked instead of pits
		if(i == 6 || i == 13) 
		{
			error = true;
			errorMsg = "Error: You clicked mancala! Choose pit with stones";
			changeState();
			getErrorMessage();
			return;
		}
		
		// gives error message when no stones are in the pit
		if(pits[i] == 0) 
		{
			error = true;
			errorMsg = "Error: You clicked empty pit! Choose pit with stones";
			changeState();
			getErrorMessage();
			return;
		}
		error = false;
		
		// for undo
		clickedPit = i; // last clicked pit index;
		clickedPitStones = pits[i]; // # of stones in the pit.
		// 
		int p1Stones = 0; 
		int p2Stones = 0;
		// for player A turn
		if (p1turn && i <= 5 && i >= 0) 
		{   
			gameStart = true;
			
			//resetting player B's undo number
			p2UndoNum = 3;
			// collect all stones in clicked pits
			int collectedStones = pits[i];
			// empty the clicked pit
			pits[i] = 0; 
			// traverseI will traverse next indexes(pits) from the clicked pit.
			int traverseI = i + 1;
			// distribute collected Stones to next pits(indexes) from the clicked pit
			for (int x = 0; x < collectedStones; x++) 
			{
				// set traverseI to be 0 when it hits player B's mancala
				if (traverseI == 13) 
					traverseI = 0;
				pits[traverseI] = pits[traverseI] + 1;
				traverseI++;
			}
			// lastTraverseI is for undo
			lastTraverseI = traverseI = traverseI - 1;
			
			// Case #1, when the last traversing stone ends up A's Mancala, free turn
			if (traverseI == 6) 
			{
				error = true;
				errorMsg = "Free Turn!";
				getErrorMessage();
				p1turn = true;
				p2turn = false;
			} 
			else 
			{
				p1turn = false;
				p2turn = true;
			}
			
			// Case #2, 1 distributed pit lands on A side and opposite side has stones 
			// both 1 pit and the stones on opposite side goes to A's mancala
			if (traverseI >= 0 && traverseI < 6 && pits[traverseI] == 1 && pits[12-traverseI] != 0) 
			{
				// saving opposite side pit's stones
				prevpits = pits;
				oppositeStones = pits[12-traverseI];
				pits[6] = pits[6] + pits[12 - traverseI] + pits[traverseI];
				pits[12 - traverseI] = 0;
				pits[traverseI] = 0;
				case2 = true;
			}

			// collect all stones
			for (int k = 0; k < 6; k++) 
				p1Stones += pits[k];

			for (int k = 7; k < 13; k++) {
				p2Stones += pits[k];
			}

			// check if game is ended
			if (p1Stones == 0) {
				gameEnded(true, p2Stones);
			}
			if (p2Stones == 0) {
				gameEnded(false, p1Stones);
			}
			
			// update
			changeState();
		} 
		else 
		{
			if (p2turn && i <= 12 && i >= 6) 
			{
				p1UndoNum = 3;
				int traverseI = i + 1;
				int collectedStones = pits[i];
				pits[i] = 0;
				int clickedPit = 0;
				for (int k = 0; k < collectedStones; k++) 
				{
					if (traverseI == 13) 
					{
						pits[traverseI] = pits[traverseI] + 1;
						traverseI = 0;
						continue;
					}
					if (traverseI == 6)
						traverseI = 7;
					pits[traverseI] = pits[traverseI] + 1;
					traverseI++;
				}
				traverseI = traverseI - 1;
				if(traverseI == -1) 
				{
					clickedPit = 13;
					lastTraverseI = clickedPit;
				} 
				else 
					lastTraverseI = traverseI;
				
				// case #1
				if (clickedPit == 13) 
				{
					error = true;
					errorMsg = "Free Turn!";
					getErrorMessage();
					p1turn = false;
					p2turn = true;
				} 
				else 
				{
					p1turn = true;
					p2turn = false;
				}
				
				// case #2
				if (traverseI > 6 && traverseI < 13 && pits[traverseI] == 1 && pits[12-traverseI] != 0) 
				{
					oppositeStones = pits[12-traverseI];
					pits[13] = pits[13] +  pits[12 - traverseI] + pits[traverseI];
					pits[12 - traverseI] = 0;
					pits[traverseI] = 0;
					case2 = true;
				}

				// collect stones
				for (int k = 0; k < 6; k++) 
				{
					p1Stones += pits[k];
				}
				for (int k = 7; k < 13; k++) 
				{
					p2Stones += pits[k];
				}
				
				// check if game ended
				if (p1Stones == 0) 
				{
					gameEnded(true, p2Stones);
					return;
				}
				if (p2Stones == 0) {
					gameEnded(false, p1Stones);
					return;
				}

				// update
				changeState();
			}
		}
	}
	
	/**
	 * getData
	 * @return pits
	 */
	public int[] getData() 
	{
		return this.pits.clone();
	}
	
	/**
	 * getTurn
	 * @return boolean p1turn
	 */
	public boolean getTurn()
	{
		return p1turn;
	}
	
	/**
	 * Undo 
	 */
	public void undo() 
	{
		// error #1, undo before move
		if(!gameStart) 
		{
			error = true;
			errorMsg = "Error: Undo is Impossible Before the Move";
			changeState();
			getErrorMessage();
			return;
		}
		// error #2, when # of undo is 0
		if(p1UndoNum == 0 || p2UndoNum == 0) 
		{
			error = true;
			errorMsg = "Your Undo # = 0";
			changeState();
			getErrorMessage();
			return;
		}
		
		// error #3, no serial undos
		if((pits[clickedPit] != 0 && p1turn && lastTraverseI == 6) || (pits[clickedPit] != 0 && p2turn && lastTraverseI == 13)) 
		{
			error = true;
			errorMsg = "Error: Serial Undo is Not Possible";
			changeState();
			getErrorMessage();
			return;
		}
		error = false;
		
		// undo for case #1
		if(p1turn && p1UndoNum > 0 && lastTraverseI == 6) 
		{
			pits[clickedPit] = clickedPitStones;
			int traverseI = lastTraverseI;
			
			//decrease previous pit stones by 1
			for(int i = clickedPitStones; i > 0; i--) 
			{
				if(traverseI == -1) 
					traverseI = 12;
				pits[traverseI] = pits[traverseI] - 1;
				traverseI--;
			}
			p1UndoNum--;
			changeState();
			return;
		}
		
		// undo for case #1
		if(p2turn && p2UndoNum > 0 && lastTraverseI == 13) 
		{
			pits[clickedPit] = clickedPitStones;
			int traverseI = lastTraverseI;
			for(int i = clickedPitStones; i > 0; i--) 
			{
				if(traverseI == 6)
					traverseI = 5;
				pits[traverseI] = pits[traverseI] - 1;
				traverseI--;
			}
			p2UndoNum--;
			changeState();
			return;
		}

		// Undo for the case where the turn got changed for player A
		if (p1UndoNum > 0 && !p1turn) 
		{
			// reversing everything
			// lastly clicked pit has the stones it had
			pits[clickedPit] = clickedPitStones;
			// traverseI becomes the last Traverse I
			int traverseI = lastTraverseI;
			// undo for case #2
			if (case2 == true) 
			{ 
				pits[12-traverseI] = oppositeStones;
				pits[6] = pits[6] - oppositeStones -1;
				pits[traverseI] = 1;
				for(int i = clickedPitStones; i > 0; i--) {
					if(traverseI == -1) {
						traverseI = 12;
					}
					pits[traverseI] = pits[traverseI] - 1;
					traverseI--;
				}
			} 
			else 
			{
				for (int i = clickedPitStones; i > 0; i--) 
				{
					if (traverseI == -1) 
						traverseI = 12;
					pits[traverseI] = pits[traverseI] - 1;
					traverseI--;
				}
			}
			p1UndoNum--;
			p1turn = true;
			changeState();
		}
		
		// Undo for the case where the turn got changed for player B
		if(!p2turn && p2UndoNum > 0) 
		{
			// reversing everything
			// lastly clicked pit has the stones it had
			pits[clickedPit] = clickedPitStones;
			// traverseI becomes the last Traverse I
			int traverseI = lastTraverseI; 
			if (case2 == true) 
			{  
				// undo for case #2
				pits[12-traverseI] = oppositeStones;
				pits[13] = pits[13] - oppositeStones - 1;
				pits[traverseI] = 1;
				for(int i = clickedPitStones; i > 0; i--) 
				{
					if(traverseI == -1) 
					{
						traverseI = 13;
						pits[traverseI] = pits[traverseI] - 1;
						continue;
					}
					if(traverseI == 6) 
					{
						traverseI = 5;
					}
					pits[traverseI] = pits[traverseI] - 1;
					traverseI--;
				}
			} 
			else 
			{ 
				for (int i = clickedPitStones; i > 0; i--) 
				{
					if(traverseI == -1) 
						traverseI = 13;
					if(traverseI == 6) 
						traverseI = 5;
					pits[traverseI] = pits[traverseI] - 1;
					traverseI--;
				}
			}
			p2UndoNum--;
			p2turn = true;
			changeState();
		}
	}
	
	/**
	 * sets how many stones in each pit
	 * @param number of stones in each pit
	 */
	public void setStones(int num) 
	{
		for(int i = 0; i < pits.length; i++) 
		{
			if(i == 6 || i == 13) 
				continue;
			pits[i] = num;
		}
		changeState();
	}
		
	/**
	 * gameEnded moves left over stones in the pits to the player's Mancala
	 * @param player side and number of left stones in the player's side
	 */
	public void gameEnded(boolean side, int stones) 
	{
		if(side) 
		{ 
			// move all leftover stones to mancala
			pits[13]= pits[13] + stones; 
			if (pits[13] < pits[6]) 
				p1win = true; 
			else if(pits[13] > pits[6])
				p2win = true;
			else 
				tie = true;
		} 
		else 
		{
			pits[6] = pits[6] + stones;
			if (pits[13] < pits[6]) 
				p1win = true; 
			else if(pits[13] > pits[6])
				p2win = true;
			else 
				tie = true;
		}
		gameOver = true;
		emptyBoard();
	}
	/**
	 * Empty the board
	 */
	public void emptyBoard() 
	{
		for(int i = 0; i < 6; i++) 
		{
			pits[i]= 0;
			pits[i+7] = 0;
		}
		changeState();
	}
	
	/**
	 * get error message
	 * @return erroMsg
	 */
	public String getErrorMessage() {
		return errorMsg;
	}
	
	/**
	 * Attaches changelisteners to the model.
	 * @param l ChangeListener
	 */
	public void attach(ChangeListener l) {
		listeners.add(l);
	}
	
	/**
	 * Changes the state of the listeners.
	 */
	public void changeState() {
		for(ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}
}