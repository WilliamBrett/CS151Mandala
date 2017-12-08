
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
public class MancalaModel {
	private ArrayList<ChangeListener> listeners;
	private Pit[] pits;
	boolean p1turn, p2turn, p1win, p2win, tie;
	int lastPit,lastPitStones, destinationPit, oppOnesPitStones, p1UndoNum, p2UndoNum;
	boolean gameStart, gameOver, error;
	String errorMsg;

	/**
	 * Constructor that initializes all the instance variables.
	 */
	public MancalaModel() {
		listeners = new ArrayList<ChangeListener>();
		pits = new Pit[14]; 
		for(int i = 0; i < pits.length; i++) {
			pits[i] = new Pit(0);
		}
		p1turn = true;
		tie = p1win = p2win = p2turn = gameStart = gameOver = error = false;
		lastPitStones = lastPit = destinationPit = oppOnesPitStones = 0;
		p1UndoNum = p2UndoNum = 3;
		errorMsg = "";
	}
	
	/**
	 * The actual gameplay. This method moves the board for each player's turn.
	 * @param i the position or pit to move from.
	 */
	public void moveBoard(int i) {
		//If one is true, winner already decided.
		if(p1win || p2win || tie) {
			return;
		}
		
		//pits[6] and pits[13] are mancalas. Should not be pressable.
		if(i == 6 || i == 13) {
			error = true;
			errorMsg = "Can't choose mancalas as a pit.";
			changeState();
			getErrorMessage();
			return;
		}
		
		//If there are no more stones in the chosen pit.
		if(pits[i].getStones() == 0) {
			error = true;
			errorMsg = "No stones, please choose another pit.";
			changeState();
			getErrorMessage();
			return;
		}
		
		error = false;
		// used for the undo function.
		lastPit = i; //the pit that the user clicked on.
		lastPitStones = pits[i].getStones(); //the number of stones in that pit.
		
		
			int firstPlayerPitStones = 0; //both used to determine when game is over.
			int secondPlayerPitStones = 0;
			if (p1turn && i >= 0 && i <= 5) { //makes sure that the pit is on firstplayer side.
				
				gameStart = true;
				int stonesInCurrentPit = pits[i].getStones();
				pits[i].setStones(0); //set chosen pit stones to 0.
				int mover = i + 1; //this is the value that is used to traverse through the board.
				//using the current pit's stones, move the stones to each pit following it
				for (int x = 0; x < stonesInCurrentPit; x++) {
					if (mover == 13) { //when mover is equal to the index of 2nd player mancala, set mover to first player pit.
						mover = 0;
					}
					pits[mover].setStones(pits[mover].getStones() + 1);
					mover++;
				}
				mover = mover - 1; //decrement back 
				destinationPit = mover; // keeps track of the pit that the last
										// stone was placed for undo function.
				// if last pit is on first player side and only has one stone.
				if (mover >= 0 && mover < 6 && pits[mover].getStones() == 1) {
					oppOnesPitStones = pits[12-mover].getStones();
					pits[6].setStones(pits[6].getStones() + pits[12 - mover].getStones());
					pits[12 - mover].setStones(0);
				}

				// determine if there are any stones left on firstPlayer side.
				for (int n = 0; n < 6; n++) {
					firstPlayerPitStones += pits[n].getStones();
				}

				// determine if there are any stones left on secondPlayer side.
				for (int n = 7; n < 13; n++) {
					secondPlayerPitStones += pits[n].getStones();
				}

				// if firstPlayer side has no stones, collect all of second
				// player stones and give to second player then determine whos
				// the winner.
				if (firstPlayerPitStones == 0) {
					determineWinner(true, secondPlayerPitStones);
				}

				// if secondPlayer side has no stones, collect all of second
				// player stones and give to first player then determine whos
				// the winner.
				if (secondPlayerPitStones == 0) {
					determineWinner(false, firstPlayerPitStones);
				}
				// if the last pit is the secondPlayer's mancala, it will still
				// be firstPlayer's turn. else, change turns.
				if (mover == 6) {
					p1turn = true;
					p2turn = false;
				} else {
					p1turn = false;
					p2turn = true;
				}
				// change listeners after.
				changeState();
			} else {
				if (p2turn && i >= 6 && i <= 12) {
					
					int mover = i + 1;
					int stonesInCurrentPit = pits[i].getStones();
					pits[i].setStones(0);
					int lastPit = 0;
					for (int x = 0; x < stonesInCurrentPit; x++) {
						if (mover == 13) {
							pits[mover].setStones(pits[mover].getStones() + 1);
							mover = 0;
							continue;
						}
						if (mover == 6) {
							mover = 7;
						}
						pits[mover].setStones(pits[mover].getStones() + 1);
						mover++;
					}
					mover = mover - 1;
					if(mover == -1) {
						lastPit = 13;
						destinationPit = lastPit;
					} else {
					destinationPit = mover;
					}
					//if the last pit stones equal to 1, take that and the opposite pit's stone and add to secondPlayer mancala.
					if (mover > 6 && mover < 13 && pits[mover].getStones() == 1) {
						oppOnesPitStones = pits[12-mover].getStones();
						pits[13].setStones(pits[13].getStones() +  pits[12 - mover].getStones());
						pits[12 - mover].setStones(0);
					}

					// determine if there are any stones left on firstPlayer side.
					for (int n = 0; n < 6; n++) {
						firstPlayerPitStones += pits[n].getStones();
					}

					// determine if there are any stones left on secondPlayer side.
					for (int n = 7; n < 13; n++) {
						secondPlayerPitStones += pits[n].getStones();
					}

					// if firstPlayer side has no stones, collect all of second
					// player stones and give to second player then determine whos
					// the winner.
					if (firstPlayerPitStones == 0) {
						determineWinner(true, secondPlayerPitStones);
						return;
					}

					// if secondPlayer side has no stones, collect all of second
					// player stones and give to first player then determine whos
					// the winner.
					if (secondPlayerPitStones == 0) {
						determineWinner(false, firstPlayerPitStones);
						return;
					}

					// if the last pit is the secondPlayer's mancala, it will still
					// be firstPlayer's turn. else, change turns.
					if (lastPit == 13) {
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

	/**
	 * Undo function of the game. 
	 */
	public void undo() {
		//if the game just started and first player tries to undo without making a first turn. 
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
		if((pits[lastPit].getStones() != 0 && p1turn && destinationPit == 6) || (pits[lastPit].getStones() != 0 && p2turn && destinationPit == 13)) {
			error = true;
			errorMsg = "Can't have consecutive undos.";
			changeState();
			getErrorMessage();
			return;
		}
		
		error = false;
		//this is undo for firstPlayer if the last stone ends up in firstPlayer's mancala and it's firstPlayer's turn again;
		if(p1turn && destinationPit == 6 && p1UndoNum > 0) {
			System.out.println(lastPit + " " + destinationPit);
			pits[lastPit].setStones(lastPitStones);
			int mover = destinationPit;
			//go backwards and decrement each pit's stones by 1.
			for(int i = lastPitStones; i > 0; i--) {
				if(mover == -1) { //if it equals -1, this means it hits the last pit on first player side, change it to 12.
					mover = 12;
				}
				pits[mover].setStones(pits[mover].getStones() - 1);
				mover--;
			}
			p1UndoNum--; //decrement undo by 1.
			changeState();
			return;
		}
		
		//same as last statement except for secondPlayer.
		if(p2turn && destinationPit == 13 && p2UndoNum > 0) {
			pits[lastPit].setStones(lastPitStones);
			int mover = destinationPit;
			for(int i = lastPitStones; i > 0; i--) {
				if(mover == 6) {
					mover = 5;
				}
				pits[mover].setStones(pits[mover].getStones() - 1);
				mover--;
			}
			p2UndoNum--;
			changeState();
			return;
		}

		// this is the undo part for the first player. After he takes a turn,
		// firstPlayer will be false and this will make firstPlayer true again.
		if (!p1turn && p1UndoNum > 0) {
			pits[lastPit].setStones(lastPitStones);//set lastPitStones to original amount of stones;
			int mover = destinationPit; //mover set to destined pit for traversing back.
			if (pits[mover].getStones() == 1) { // case where the last pit had one stone after a turn 
				System.out.println(pits[6].getStones());
				System.out.println(lastPitStones);
				System.out.println(mover);
				pits[12-mover].setStones(oppOnesPitStones);
				pits[6].setStones(pits[6].getStones() - oppOnesPitStones);
				for(int i = lastPitStones; i > 0; i--) {
					if(mover == -1) {
						mover = 12;
					}
					pits[mover].setStones(pits[mover].getStones() - 1);
					mover--;
				}
			} else { //traverses the mancala board and decrements each pit by 1 stone until it reaches the pit before the last pit.
				for (int i = lastPitStones; i > 0; i--) {
					if (mover == -1) {
						mover = 12;
					}
					pits[mover].setStones(pits[mover].getStones() - 1);
					mover--;
				}
			}
			p1UndoNum--; //decrement amount of undos.
			p1turn = true; //set p1turn back to true.
			changeState();
		}
		
		//undo function for player two.
		if(!p2turn && p2UndoNum > 0) {
			pits[lastPit].setStones(lastPitStones);//set lastPitStones to original amount of stones;
			int mover = destinationPit; //mover set to destined pit for traversing back.
			if (pits[mover].getStones() == 1) { // case where the last pit had one stone after a turn 
				pits[12-mover].setStones(oppOnesPitStones);
				pits[13].setStones(pits[13].getStones() - oppOnesPitStones);
				for(int i = lastPitStones; i > 0; i--) {
					if(mover == -1) {
						mover = 13;
						pits[mover].setStones(pits[mover].getStones() - 1);
						continue;
					}
					if(mover == 6) {
						mover = 5;
					}
					pits[mover].setStones(pits[mover].getStones() - 1);
					mover--;
				}
			} else { //traverses the mancala board and decrements each pit by 1 stone until it reaches the pit before the last pit.
				for (int i = lastPitStones; i > 0; i--) {
					System.out.println(lastPitStones + " " + mover);
					if (mover == -1) {
						mover = 13;
					}
					if(mover == 6) {
						mover = 5;
					}
					pits[mover].setStones(pits[mover].getStones() - 1);
					mover--;
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
	public Pit[] getData() {
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
			pits[i].setStones(0);
			pits[i+7].setStones(0);
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
			pits[i].setStones(num);
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
	public void determineWinner(boolean side, int stones) {
		if(side) { //this means that first player has no more pits.
			pits[13].setStones(pits[13].getStones() + stones); //set second mancala stones to current stones and how many stones were left on second player side.
			if (pits[6].getStones() > pits[13].getStones()) {
				System.out.println("First player won");
				p1win = true;
			} else if(pits[13].getStones() > pits[6].getStones()){
				System.out.println("Second player won");
				p2win = true;
			} else {
				System.out.println("tie game!");
				tie = true;
			}
		} else {
			pits[6].setStones(pits[6].getStones() + stones);
			if (pits[6].getStones() > pits[13].getStones()) {
				System.out.println("First player won");
				p1win = true;
			} else if(pits[13].getStones() > pits[6].getStones()){
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