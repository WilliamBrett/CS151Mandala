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
	private ArrayList<ChangeListener> listeners;
	private int pits[], prevpits[];
	boolean p1turn, p2turn, p1win, p2win, tie, gameStart, gameOver, error;
	int clickedPit,clickedPitStones, lastTraverseI, oppositeStones, p1UndoNum, p2UndoNum;
	String errorMsg;

	/**
	 * Constructor that initializes all the instance variables.
	 */
	public MancalaModel() {
		listeners = new ArrayList<ChangeListener>();
		prevpits = pits = new int[14]; 
		for(int i = 0; i < pits.length; i++) {
			pits[i] = 0;
		}
		prevpits = pits;
		p1turn = true;
		tie = p1win = p2win = p2turn = gameStart = gameOver = error = false;
		clickedPitStones = clickedPit = lastTraverseI = oppositeStones = 0;
		p1UndoNum = p2UndoNum = 3;
		errorMsg = "";
	}
	
	/**
	 * The actual gameplay. This method moves the board for each player's turn.
	 * @param i the position or pit to move from.
	 */
	public void moveBoard(int i) 
	{
		// checks if the game is over
		if(p1win || p2win || tie) {
			return;
		}
		
		// gives error message when mancalas are clicked instead of pits
		if(i == 6 || i == 13) {
			error = true;
			errorMsg = "Please choose pits instead of mancala";
			changeState();
			getErrorMessage();
			return;
		}
		
		// gives error message when no stones are in the pit
		if(pits[i] == 0) {
			error = true;
			errorMsg = "It's empty pit, please choose another pit with stones";
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
		if (p1turn && i >= 0 && i <= 5) 
		{   
			gameStart = true;
			// empty the clicked pit
			pits[i] = 0;
			
			// collect all stones in clicked pits
			int collectedStones = pits[i];
			// traverseI will traverse next indexes(pits) from the clicked pit.
			int traverseI = i + 1;
			
			
			
			//resetting player B's undo number
			p2UndoNum = 3;
			
			// distribute collected Stones to next pits(indexes) from the clicked pit
			for (int x = 0; x < collectedStones; x++) 
			{
				// set traverseI to be 0 when it hits player B's mancala
				if (traverseI == 13) 
					traverseI = 0;
				pits[traverseI] = pits[traverseI] + 1;
				traverseI++;
			}
			
			// save current traverseI at lastTraverseI for undo
			lastTraverseI = traverseI = traverseI - 1;
			
			// playerA takes opposite side stones when traverseI lands on pit with 1 stone
			if (traverseI >= 0 && traverseI < 6 && pits[traverseI] == 1) 
			{
				// saving opposite side pit's stones
				oppositeStones = pits[12-traverseI];
				pits[6] = pits[6] + pits[12 - traverseI];
				pits[12 - traverseI] = 0;
			}
			
			// collect stones and each player's pits
			p1Stones = collectP1Stones();
			p2Stones = collectP2Stones();
			
			// game ends when either side player's pits are empty
			if (p1Stones == 0) 
				gameEnded(true, p2Stones);
			if (p2Stones == 0) 
				gameEnded(false, p1Stones);
			
			// free turn case, if the traverseI ends at player A's manacala
			if (traverseI == 6) 
			{
				p1turn = true;
				p2turn = false;
			} 
			// if not free turn case then it becomes B's turn
			else 
			{
				p1turn = false;
				p2turn = true;
			}
			// update views
			changeState();
		} 
		else 
		{
			if (p2turn && i >= 6 && i <= 12) 
			{
				p1UndoNum = 3;
				int traverseI = i + 1;
				int collectedStones = pits[i];
				pits[i] = 0;
				int clickedPit = 0;
				for (int x = 0; x < collectedStones; x++) {
					if (traverseI == 13) {
						pits[traverseI] = pits[traverseI] + 1;
						traverseI = 0;
						continue;
					}
					if (traverseI == 6) {
						traverseI = 7;
					}
					pits[traverseI] = pits[traverseI] + 1;
					traverseI++;
				}
				traverseI = traverseI - 1;
				if(traverseI == -1) {
					clickedPit = 13;
					lastTraverseI = clickedPit;
				} else {
				lastTraverseI = traverseI;
				}
				//if the last pit stones equal to 1, take that and the opposite pit's stone and add to secondPlayer mancala.
				if (traverseI > 6 && traverseI < 13 && pits[traverseI] == 1) {
					oppositeStones = pits[12-traverseI];
					pits[13] = pits[13] +  pits[12 - traverseI];
					pits[12 - traverseI] = 0;
				}

				// determine if there are any stones left on firstPlayer side.
				for (int n = 0; n < 6; n++) {
					p1Stones += pits[n];
				}

				// determine if there are any stones left on secondPlayer side.
				for (int n = 7; n < 13; n++) {
					p2Stones += pits[n];
				}

				// if firstPlayer side has no stones, collect all of second
				// player stones and give to second player then determine whos
				// the winner.
				if (p1Stones == 0) {
					gameEnded(true, p2Stones);
					return;
				}

				// if secondPlayer side has no stones, collect all of second
				// player stones and give to first player then determine whos
				// the winner.
				if (p2Stones == 0) {
					gameEnded(false, p1Stones);
					return;
				}

				// if the last pit is the secondPlayer's mancala, it will still
				// be firstPlayer's turn. else, change turns.
				if (clickedPit == 13) {
					p1turn = false;
					p2turn = true;
				} else {
					p1turn = true;
					p2turn = false;
				}
				// change listeners after.
				changeState();
			}
		}
	}
	public int collectP1Stones()
	{
		int result = 0;
		for (int i = 0; i < 6; i++) {
			result = result + pits[i];
		}
		return result;
	}
	public int collectP2Stones()
	{
		int result = 0;
		for (int i = 7; i < 13; i++) {
			result = result + pits[i];
		}
		return result;
	}

	/**
	 * Undo function of the game. 
	 */
	public void undo() {
		// On the  
		if(!gameStart) {
			error = true;
			errorMsg = "Game just started. Please make move first.";
			changeState();
			getErrorMessage();
			return;
		}
		
		//if p1UndoNum/p2UndoNum are 0, print out error msg and return.
		if(p1UndoNum == 0 || p2UndoNum == 0) {
			error = true;
			errorMsg = "Used up all undos for this turn.";
			changeState();
			getErrorMessage();
			return;
		}
		
		//if one of the pits does not equal 0, that means that there are consecutive undos.
		if((pits[clickedPit] != 0 && p1turn && lastTraverseI == 6) || (pits[clickedPit] != 0 && p2turn && lastTraverseI == 13)) {
			error = true;
			errorMsg = "Can't have consecutive undos.";
			changeState();
			getErrorMessage();
			return;
		}
		
		error = false;
		//this is undo for firstPlayer if the last stone ends up in firstPlayer's mancala and it's firstPlayer's turn again;
		if(p1turn && lastTraverseI == 6 && p1UndoNum > 0) {
			System.out.println(clickedPit + " " + lastTraverseI);
			pits[clickedPit] = clickedPitStones;
			int traverseI = lastTraverseI;
			//go backwards and decrement each pit's stones by 1.
			for(int i = clickedPitStones; i > 0; i--) {
				if(traverseI == -1) { //if it equals -1, this means it hits the last pit on first player side, change it to 12.
					traverseI = 12;
				}
				pits[traverseI] = pits[traverseI] - 1;
				traverseI--;
			}
			p1UndoNum--; //decrement undo by 1.
			changeState();
			return;
		}
		
		//same as last statement except for secondPlayer.
		if(p2turn && lastTraverseI == 13 && p2UndoNum > 0) {
			pits[clickedPit] = clickedPitStones;
			int traverseI = lastTraverseI;
			for(int i = clickedPitStones; i > 0; i--) {
				if(traverseI == 6) {
					traverseI = 5;
				}
				pits[traverseI] = pits[traverseI] - 1;
				traverseI--;
			}
			p2UndoNum--;
			changeState();
			return;
		}

		// this is the undo part for the first player. After he takes a turn,
		// firstPlayer will be false and this will make firstPlayer true again.
		if (!p1turn && p1UndoNum > 0) {
			pits[clickedPit] = clickedPitStones;//set clickedPitStones to original amount of stones;
			int traverseI = lastTraverseI; //traverseI set to destined pit for traversing back.
			if (pits[traverseI] == 1) { // case where the last pit had one stone after a turn 
				System.out.println(pits[6]);
				System.out.println(clickedPitStones);
				System.out.println(traverseI);
				pits[12-traverseI] = oppositeStones;
				pits[6] = pits[6] - oppositeStones;
				for(int i = clickedPitStones; i > 0; i--) {
					if(traverseI == -1) {
						traverseI = 12;
					}
					pits[traverseI] = pits[traverseI] - 1;
					traverseI--;
				}
			} else { //traverses the mancala board and decrements each pit by 1 stone until it reaches the pit before the last pit.
				for (int i = clickedPitStones; i > 0; i--) {
					if (traverseI == -1) {
						traverseI = 12;
					}
					pits[traverseI] = pits[traverseI] - 1;
					traverseI--;
				}
			}
			p1UndoNum--; //decrement amount of undos.
			p1turn = true; //set p1turn back to true.
			changeState();
		}
		
		//undo function for player two.
		if(!p2turn && p2UndoNum > 0) {
			pits[clickedPit] = clickedPitStones;//set clickedPitStones to original amount of stones;
			int traverseI = lastTraverseI; //traverseI set to destined pit for traversing back.
			if (pits[traverseI] == 1) { // case where the last pit had one stone after a turn 
				pits[12-traverseI] = oppositeStones;
				pits[13] = pits[13] - oppositeStones;
				for(int i = clickedPitStones; i > 0; i--) {
					if(traverseI == -1) {
						traverseI = 13;
						pits[traverseI] = pits[traverseI] - 1;
						continue;
					}
					if(traverseI == 6) {
						traverseI = 5;
					}
					pits[traverseI] = pits[traverseI] - 1;
					traverseI--;
				}
			} else { //traverses the mancala board and decrements each pit by 1 stone until it reaches the pit before the last pit.
				for (int i = clickedPitStones; i > 0; i--) {
					System.out.println(clickedPitStones + " " + traverseI);
					if (traverseI == -1) {
						traverseI = 13;
					}
					if(traverseI == 6) {
						traverseI = 5;
					}
					pits[traverseI] = pits[traverseI] - 1;
					traverseI--;
				}
			}
			p2UndoNum--; //decrement amount of undos.
			p2turn = true; //set p1turn back to true.
			changeState();
		}
	}
	
	
	/**
	 * Gets the data from the array of pits.
	 * @return an array of pits.
	 */
	public int[] getData() {
		return this.pits.clone();
	}
	
	/**
	 * Gets the player's turn. Player 1's turn returns true. Player 2's turn returns false
	 * @return boolean
	 */
	public boolean getTurn(){
		return p1turn;
	}
	
	/**
	 * Attaches changelisteners to the model.
	 * @param l the listener to add.
	 */
	public void attach(ChangeListener l) {
		listeners.add(l);
	}
	
	/**
	 * Clears the board and sets to 0. Used when there is a winner.
	 */
	public void clearBoard() {
		for(int i = 0; i < 6; i++) {
			pits[i]= 0;
			pits[i+7] = 0;
		}
		changeState();
	}
	
	/**
	 * Used for deciding how many stones should be in each pit in the beginning of the game.
	 * @param num Number of stones each pit should start off with.
	 */
	public void setStones(int num) {
		for(int i = 0; i < pits.length; i++) {
			if(i == 6 || i == 13) {
				continue;
			}
			pits[i] = num;
		}
		changeState();
	}
	
	/**
	 * Changes the state of the listeners.
	 */
	public void changeState() {
		for(ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}
	
	/**
	 * determine the winner
	 * @param side boolean to determine which side has 0 pits. true for firstplayer false for secondplayer
	 */
	public void gameEnded(boolean side, int stones) {
		if(side) { //this means that first player has no more pits.
			pits[13]= pits[13] + stones; //set second mancala stones to current stones and how many stones were left on second player side.
			if (pits[6] > pits[13]) {
				System.out.println("First player won");
				p1win = true;
			} else if(pits[13] > pits[6]){
				System.out.println("Second player won");
				p2win = true;
			} else {
				System.out.println("tie game!");
				tie = true;
			}
		} else {
			pits[6] = pits[6] + stones;
			if (pits[6] > pits[13]) {
				System.out.println("First player won");
				p1win = true;
			} else if(pits[13] > pits[6]){
				System.out.println("Second player won");
				p2win = true;
			} else {
				System.out.println("tie game!");
				tie = true;
			}
		}
		gameOver = true;
		clearBoard();
	}
	
	/**
	 * Gets the error message whenever a game logic error occurs.
	 * @return the error
	 */
	public String getErrorMessage() {
		return errorMsg;
	}
}