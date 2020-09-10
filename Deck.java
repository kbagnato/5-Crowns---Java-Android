package com.example.a5crowns;

import java.util.Collections;
import java.util.Vector;

/*******************************************************
  Holds one deck of cards of 5 suits and faces 1-13
  @author Kevin Bagnato
  @since 10/27/2019
******************************************************** */

public class Deck
{
    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************
	
	/** The suits of the cards in this deck */
	public static final char SUITS[] = {'s', 'c', 'd', 'h', 't'};
	
	/** The faces of the cards in this deck */
	public static final int MIN_FACE = 3, MAX_FACE = 13;
	
	/** The size of the deck */
	public static final int DECK_SIZE = 58;
	
	
    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /** The Vector that holds all the cards in the deck */
	private Vector <Card>cardVector;
	
	
    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************
	/**
	 * Deck constructor
	 */
	public Deck () {
		
		// allocate space for the vector of cards
		cardVector = new Vector <Card>(DECK_SIZE);
		
		// create a card of each face and suit and add it to the deck
		for (int suitIndex = 0; suitIndex < SUITS.length; suitIndex++) {
			for (int faceIndex = MIN_FACE; faceIndex <= MAX_FACE; faceIndex++) {
				cardVector.add(new Card(SUITS[suitIndex], faceIndex));
			}
		}
		
		// add 3 jokers
		for (int i = 1; i <= 3; i ++) {
			cardVector.add(new Card((char) (i + '0'), 11));
		}

		// shuffle the deck
		Collections.shuffle(cardVector);
		
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
	 * Determine if the Deck has any Cards
	 * @return true if the size of the Deck is 0
	 *	false if the Deck has Cards
	 */
	final public boolean isEmpty() {
		return cardVector.size() == 0;
	}
	
    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************
	
	/**
	 * Remove the top Card from the Deck and return it
	 * @return Card returnCard The Card removedfrom the Deck
	 * 	null if the deck is empty
	 */
	public Card dealCard() {
		// return null if deck is empty
		if (isEmpty()) {	// cardVector.size() == 0
			System.out.println("Failed to deal card from empty deck.");
			return null;
		}
		
		// get a reference to the first card
		Card returnCard = cardVector.get(0);
		
		// remove the card from cardVector
		cardVector.remove(0);
		
		// return reference to first card
		return returnCard;
	}
	
	/**
	 * Update the Cards wildcard
	 * @param face the current wildcard
	 */
	public void updateWildcards(int face) {
		for (Card card: cardVector) {
			card.updateWildcard(face);
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
	/**
	 * Return the Cards in the Deck as a String
	 * @return the Cards in the Deck
	 */
	public String toString() {
		String result = "";
		
		for (int count = 0; count < cardVector.size(); count++) {
			result += cardVector.get(count).toString() + " ";
			if (count != 0 && count % 11 == 0) {
				result += "\n";
			}
		}
		
		return result;
	}
	
    // *********************************************************
    // ******************** Debugging Methods ******************
    // *********************************************************

};

    // *********************************************************
    // ******************** Trash Methods **********************
    // *********************************************************
