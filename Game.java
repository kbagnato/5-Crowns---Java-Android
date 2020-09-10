package com.example.a5crowns;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.a5crowns.Round;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Vector;

/*******************************************************
  One line description of what the class does
  @author Your name here
  @since Date when you created the class
******************************************************** */

public class Game implements Parcelable //Serializable
{
    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************
	
	/** the current round number */
	private int roundNum;

	/** the current round */
	private Round round;

	/** the human player */
	private Human human;

	/** the computer player */
	private Computer computer;


	public static final Creator<Game> CREATOR = new Creator<Game>() {
		@Override
		public Game createFromParcel(Parcel in) {
			return new Game(in);
		}

		@Override
		public Game[] newArray(int size) {
			return new Game[size];
		}
	};



	public void nextPlayer() {
		round.nextPlayer();
	}

	
    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************

	/**
	 * The Game constructor
	 */
	public Game() {
		// create 2 players
		human = new Human();
		computer = new Computer();
		
		roundNum = 1;

		// to test main activity
		round = new Round(roundNum, human, computer);
	}

	/**
	 * The Game constructor
	 */
	public Game(Human hum, Computer cpu, Round inRound) {
		// create 2 players
		human = new Human(hum.getScore(), hum.getHand());
		computer = new Computer(cpu.getScore(), cpu.getHand());

		round = inRound;
		roundNum = inRound.getRoundNum();
	}

    // *********************************************************
    // ******************** Paint - View ***********************
    // *********************************************************

    // *********************************************************
    // ******************** actionPerformed - Controller *******
    // *********************************************************

    // *********************************************************
    // ******************** Selectors **************************
    // *********************************************************

	/**
	 * Return the round
	 * @return the round
	 */
	final public Round getRound() {
		return round;
	}

	public Human getHuman() {
		return human;
	}

	public Player getComputer() {
		return computer;
	}

    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************

	/**
	 * rip of above to avoid loop in round.play()
	 * @return
	 */
	public boolean playComputer() {
		return round.moveComputer();
	}


	/**
	 * start a new round
	 * @return false if the game has ended
	 */
	public boolean nextRound() {
		if (roundNum == 13) {
			return false;
		}
		else {
			roundNum += 1;
			human.hand.clear();
			computer.hand.clear();
			round = new Round(roundNum, human, computer);
			return true;
		}
	}

    // *********************************************************
    // ******************** Code Generation ********************
    // *********************************************************

    // *********************************************************
    // ******************** Code Explanation *******************
    // *********************************************************

    // *********************************************************
    // ******************** Utility Methods ********************
    // *********************************************************

    // *********************************************************
    // ******************** Printing Methods *******************
    // *********************************************************
	final public String toString() {
		return round.toString();
	}

	// *********************************************************
	// ******************* Parcelable Part *********************
	// *********************************************************
	public Game(Parcel in) {
		// get human
		int humanScore = in.readInt();
 		Card humanArray[] = in.createTypedArray(Card.CREATOR);
// 		Card humanArray[] = (Card[]) in.readArray(Card.class.getClassLoader());
		Vector<Card> humanHand = new Vector<>();
		for (Card card : humanArray) {
			humanHand.add(card);
		}
		human = new Human(humanScore, humanHand);

		// get computer
		int computerScore = in.readInt();
		Card computerArray[] = in.createTypedArray(Card.CREATOR);
//		Card computerArray[] = (Card[]) in.readArray(Card.class.getClassLoader());
		Vector<Card> computerHand = new Vector<>();
		for (Card card : computerArray) {
			computerHand.add(card);
		}
		computer = new Computer(computerScore, computerHand);

		// get next player
		int nextPlayerIdx = in.readInt();

		// get round number
		roundNum = in.readInt();

		// get draw/discard piles
		Card drawArray[] = (Card[]) in.readArray(null);
		Vector<Card> drawPile = new Vector<>();
		for (Card card : drawArray) {
			drawPile.add(card);
		}

		Card discardArray[] = (Card[]) in.readArray(null);
		Vector<Card> discardPile = new Vector<>();
		for (Card card : discardArray) {
			discardPile.add(card);
		}

		round = new Round(roundNum, human, computer, nextPlayerIdx, drawPile, discardPile);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		// write human by score then hand
		dest.writeInt(human.getScore());
//		dest.writeTypedArray(Arrays.asList(human.getHand().toArray()), 0);
		dest.writeArray(human.getHand().toArray());

		// write computer
		dest.writeInt(computer.getScore());
		dest.writeArray(computer.getHand().toArray());

		// write next player
		dest.writeInt(round.getNextPlayerIndex());

		// write round
		dest.writeInt(roundNum);
		dest.writeInt(round.getNextPlayerIndex());

		// write draw/discard piles
		dest.writeArray(round.getDrawPile().toArray());
		dest.writeArray(round.getDiscardPile().toArray());
	}

	// *********************************************************
    // ******************** Debugging Methods ******************
    // *********************************************************

};

    // *********************************************************
    // ******************** Trash Methods **********************
    // *********************************************************
