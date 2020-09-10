package com.example.a5crowns;

import android.util.Log;
import android.widget.TextView;

import com.example.a5crowns.Card;

import java.util.Collections;
import java.util.Vector;

/*******************************************************
 Base class of Human and Computer
 @author Kevin Bagnato
 @since 10/27/2019
  ******************************************************** */

public class Player {
	// *********************************************************
	// **************** Configuration Variables ****************
	// *********************************************************

	// *********************************************************
	// ******************** Class Constants ********************
	// *********************************************************

	// *********************************************************
	// ******************** Class Variables ********************
	// *********************************************************
	/**
	 * Current score of player
	 */
	protected int score;

	/**
	 * Current hand of player
	 */
	protected Vector<Card> hand;

	/**
	 * Description of the player's last move
	 */
	String lastMoveDesc = "";
	String drawReason = "";
	String discardReason = "";

//	/**
//	 * Description of the last assemblies
//	 */
//	String lastRun = "";
//	String lastBook = "";
//
//	public String getLastRun() {
//		return lastRun;
//	}
//
//	public String getLastBook() {
//		return lastBook;
//	}

	// *********************************************************
	// ******************** GUI Components *********************
	// *********************************************************

	// *********************************************************
	// ******************** Constructor ************************
	// *********************************************************

	/**
	 * For each function, one-line description of the function
	 */
	public Player() {
		// set default score to 0
		score = 0;

		// allocate to hand
		hand = new Vector<Card>(3, 3);

		lastMoveDesc = "Player was just created.";
		drawReason = new String();
		discardReason = new String();
	}

	/**
	 * For each function, one-line description of the function
	 */
	public Player(int s, Vector<Card> h) {
		// set default score to 0
		score = s;

		// allocate to hand
		hand = h;

		lastMoveDesc = "Player was just created with " + s + " and " + h;
		drawReason = new String();
		discardReason = new String();
	}

	public Player (final Player inPlayer) {
		score = inPlayer.score;
		hand = new Vector(inPlayer.hand);
		lastMoveDesc = inPlayer.lastMoveDesc;
		drawReason = inPlayer.drawReason;
		discardReason = inPlayer.discardReason;
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
	 * Return the player's score
	 *
	 * @return the player's score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Return the player's hand
	 *
	 * @return the player's hand
	 */
	public Vector<Card> getHand() {
		return hand;
	}

	// *********************************************************
	// ******************** Mutators ***************************
	// *********************************************************
	public void addCard(Card newCard) {
		hand.add(newCard);
//		Collections.sort(hand);

		// move jokers to end with bubble sort
		for (int i = 0; i < hand.size() - 1; i++) {
			for (int j = 0; j < hand.size() - 1 - i; j++) {
				if (hand.get(j).isJoker()) {
					Card temp = hand.get(j);
					hand.set(j, hand.get(j + 1));
					hand.set(j + 1, temp);
				}
			}
		}

		// sort jokers by suit
		for (int i = 0; i < hand.size() - 1; i++) {
			for (int j = 0; j < hand.size() - 1 - i; j++) {
				if (hand.get(j).isJoker() || hand.get(j + 1).isJoker()
						|| hand.get(j).getSuit() > hand.get(j + 1).getSuit()) {
					Card temp = hand.get(j);
					hand.set(j, hand.get(j + 1));
					hand.set(j + 1, temp);
				}
			}
		}
	}

	/**
	 * Add points to the players score
	 *
	 * @param newScore the points to add
	 */
	public void addToScore(int newScore) {
		score += newScore;
	}

	public void assemble() {
		hand = helpAssemble();
	}

	/** *********************************************************************
	 Function Name: drawCard
	 Purpose: Move the first Card from a given pile to the Player's hand
	 Parameters:
	 Vector<Card> drawPile the pile to choose from
	 Return Value: None
	 Assistance Received: Dr. Kumar's Sunday lecture
	 ********************************************************************* */
	void drawCard(Vector<Card> drawPile) {
		// add the first Card in the draw pile
		addCard(drawPile.firstElement());
		drawPile.remove(drawPile.firstElement());
	}

	/** *********************************************************************
	 Function Name: drawCard
	 Purpose: Move the first Card from a given pile to the Player's hand
	 Parameters:
	 Vector<Card> drawPile the first pile to choose from
	 Vector<Card> discardPile the second pile to choose from
	 Return Value: None
	 Assistance Received: Dr. Kumar's Sunday lecture
	 ********************************************************************* */
	void drawCard(Vector<Card> drawPile, Vector<Card> discardPile) {
		// add the first Card in the draw pile
		addCard(drawPile.firstElement());
		drawPile.remove(drawPile.lastElement());
		lastMoveDesc = "default drawCard()";
	}

	/** *********************************************************************
	 Function Name: discardCard
	 Purpose: Move the first Card from the Player's hand to the given pile
	 Parameters:
	 Vector<Card> discardPile pile to move the Card to
	 Return Value: None
	 Assistance Received: Dr. Kumar's Sunday lecture
	 ********************************************************************* */
	void discardCard(Vector<Card> discardPile) {
		// remove the first Card in hand
		discardPile.add(0, hand.firstElement());
		hand.remove(hand.firstElement());
		lastMoveDesc = "default discardCard()";
	}

	/** *********************************************************************
	 Function Name: discardCard
	 Purpose: Move the given Card from the Player's hand to the given pile
	 Parameters:
	 Card card the Card to drop
	 Vector<Card> discardPile pile to move the Card to
	 Return Value: None
	 Assistance Received: None
	 ********************************************************************* */
	void discardCard(Card card, Vector<Card> discardPile) {
		// discard the user-selected card
		discardPile.add(0, card);

		// find Card to remove
		for (int i = 0; i < hand.size(); i++) {
			// remove matching card from hand
			if (hand.get(i).getFace() == card.getFace() && hand.get(i).getSuit() == card.getSuit()) {
				hand.remove(i);
				return;
			}
		}
	}

	/** *********************************************************************
	 Function Name: getPointsInHand
	 Purpose: Return the points in the Player's hand
	 Parameters: None
	 Return Value: The Player's points in hand
	 Assistance Received: Dr. Kumar's Sunday lecture
	 ********************************************************************* */
	int getPointsInHand() {
		int sum = 0;
		for (Card card : hand) {
			sum += card.getPoints();
		}
		return sum;
	}

	/** *********************************************************************
	 Function Name: canGoOut
	 Purpose: Determine if all Cards can be assembled
	 Parameters: None
	 Return Value: true if the Player has no remaining Cards after assembly
	 Assistance Received: None
	 ********************************************************************* */
	boolean canGoOut() {
		Assembler assembler = new Assembler();
		assembler.setRemainingCards(hand);

		Vector<Card> remainingCards = Assembler.getCheapestAssembly(assembler, new Assembler()).getRemainingCards();

		return (remainingCards.size() == 0);
	}

	/** *********************************************************************
	 Function Name: goOut
	 Purpose: Remove and print all assemblies from hand
	 Parameters: None
	 Return Value: None
	 Assistance Received: None
	 ********************************************************************* */
	void goOut() {
		// get best assembly
		Assembler bestAssembly = Assembler.getCheapestAssembly(new Assembler(hand), new Assembler());

//		// add assemblies to string
//		for (Vector<Card> assemblies : bestAssembly.getAssemblies()) {
//			for (Card card : assemblies) {
//				if (card != null) {
//					lastAssembly += card.toString() + " ";
//
//				}
//				lastAssembly  += "\n";
//			}
//		}

		// set hand to only remaining cards
		hand.clear();
		for (Card card : bestAssembly.getRemainingCards()) {
			hand.add(card);
		}
	}

	// old code
	{
//	/** *********************************************************************
//	 Function Name: removeAssemblies
//	 Purpose: Remove and return all assemblies from given hand
//	 Parameters:
//	 Vector<Card> handPtr a pointer to the hand to remove assemblies from
//	 Return Value:
//	 Vector<Vector<Card>> a Vector of all assemblies (Vectors) removed
//	 Assistance Received: None
//	 ********************************************************************* */
//	static private Vector<Card> removeAssemblies(Vector<Card> hand) {
//		Vector<Vector<Card>> allAssemblies = Assembler.getAllAssemblies(hand);
//
//		for (Vector<Card> assembly : allAssemblies) {
//			for (Card card : assembly) {
//				hand.remove(card);
//			}
//		}
//
//		return hand;
//	}
//
//	/** *********************************************************************
//	 Function Name: removeAllBooks
//	 Purpose: Remove all valid books from hand
//	 Parameters:
//	 Vector<Card> a reference to the hand the Cards to remove books from
//	 Return Value:
//	 Vector<Vector<Card>> all of the books removed from the hand
//	 Assistance Received: None
//	 ********************************************************************* */
//	static private Vector<Card> removeAllBooks(Vector<Card> hand) {
//		// get all books in hand
//		Vector<Vector<Card>> allBooks = getAllBooks(hand);
//
//		// loop through all books in hand
//		for (Vector<Card> book : allBooks) {
//			// loop through all cards in book
//			for (Card card : book) {
//
//				// remove card from hand
//				hand.remove(card);
//			}
//		}
//
//		// return hand with books removed
//		return hand;
//	}
//
//	/** *********************************************************************
//	 Function Name: removeAllBooks
//	 Purpose: Remove all valid books from hand
//	 Parameters:
//	 Vector<Card> a reference to the hand the Cards to remove books from
//	 Return Value:
//	 Vector<Vector<Card>> all of the books removed from the hand
//	 Assistance Received: None
//	 ********************************************************************* */
//	static private Vector<Vector<Card>> getAllBooks(final Vector<Card> hand) {
//		Vector<Card> handCopy = new Vector<Card>(hand);
//
//		// remove and store all books from hand and store them in booksRemoved
//		Vector<Vector<Card>> booksRemoved = new Vector<Vector<Card>>();
//		while (true) {
//
//			// look for next book
//			Vector<Card> tempBook = getFirstBook(handCopy);
//
//			// check if book exists
//			if (tempBook.size() > 0) {
//				booksRemoved.add(tempBook);
//				handCopy = removeFirstBook(handCopy);
//			} else {
//				// no books left in hand
//				break;
//			}
//		}
//
//		return booksRemoved;
//	}
//
//	/** *********************************************************************
//	 Function Name: removeFirstBook
//	 Purpose: Remove the first valid books from hand
//	 Parameters:
//	 Vector<Card> hand the Cards to remove a book from
//	 Return Value:
//	 Vector<Card> the removed book or an empty Vector
//	 Assistance Received: None
//	 ********************************************************************* */
//	static private Vector<Card> removeFirstBook(Vector<Card> hand) {
//		Vector<Card> book = getFirstBook(hand);
//
//		for (Card card : book) {
//			hand.remove(card);
//		}
//
//		return hand;
//	}
//
//	/** *********************************************************************
//	 Function Name: removeFirstBook
//	 Purpose: Remove the first valid books from hand
//	 Parameters:
//	 Vector<Card> hand the Cards to remove a book from
//	 Return Value:
//	 Vector<Card> the removed book or an empty Vector
//	 Assistance Received: None
//	 ********************************************************************* */
//	static private Vector<Card> getFirstBook(final Vector<Card> hand) {
//		// books must be 3 Cards or longer
//		if (hand.size() < 3) {
//			return new Vector<Card>();
//		}
//
//		// track book position starting with first 3 Cards
//		int startIdx = 0, endIdx = 2;
//
//		// loop through all Cards until the last 2 (books must be 3 long)
//		while (startIdx < hand.size() - 2) {
//
//			// check if Cards from hand[startIdx] to hand[endIdx] (inclusive) is a valid
//			// book
//			if (endIdx != hand.size() && Assembler.isBook(new Vector(hand.subList(startIdx, endIdx + 1)))) {
//				// if so, continue to the next card
//				endIdx++;
//				continue;
//			} else {
//				// hand[endIdx] cannot be used in book
//				endIdx--;
//
//				// confirm hand[startIdx, endIdx] is a book
//				if (Assembler.isBook(new Vector(hand.subList(startIdx, endIdx + 1)))) {
//					// return the removed book
//					Vector<Card> book = new Vector(hand.subList(startIdx, endIdx + 1));
//					return book;
//				} else {
//					// book cannot exist starting with startIdx
//					startIdx++;
//				}
//				// adjust start/end
//				endIdx = startIdx + 2;
//
//				// avoid out of range errors
//				if (endIdx >= hand.size()) {
//					break;
//				}
//			}
//		}
//
//		// no book found, return empty list
//		return new Vector<Card>();
//	}
//
//	/** *********************************************************************
//	 Function Name: removeAllRuns
//	 Purpose: Remove all valid runs from hand
//	 Parameters:
//	 Vector<Card> a reference to the hand the Cards to remove runs from
//	 Return Value:
//	 Vector<Vector<Card>> all of the runs removed from the hand
//	 Assistance Received: None
//	 ********************************************************************* */
//	static private Vector<Vector<Card>> getAllRuns(final Vector<Card> hand) {
//		Vector<Card> handCopy = new Vector<Card>(hand);
//		// remove and store all books from hand and store them in booksRemoved
//		Vector<Vector<Card>> runsRemoved = new Vector();
//		while (true) {
//			Vector<Card> tempBook = getFirstRun(handCopy);
//
//			// check if book exists
//			if (tempBook.size() > 0) {
//				runsRemoved.add(tempBook);
//				handCopy = removeFirstRun(handCopy);
//			} else {
//				// no books left in hand
//				break;
//			}
//		}
//
//		return runsRemoved;
//	}
//
//	/** *********************************************************************
//	 Function Name: removeAllRuns
//	 Purpose: Remove all valid runs from hand
//	 Parameters:
//	 Vector<Card> a reference to the hand the Cards to remove runs from
//	 Return Value:
//	 Vector<Vector<Card>> all of the runs removed from the hand
//	 Assistance Received: None
//	 ********************************************************************* */
//	static private Vector<Card> removeAllRuns(Vector<Card> hand) {
//		Vector<Vector<Card>> allRuns = getAllRuns(hand);
//
//		for (Vector<Card> run : allRuns) {
//			for (Card card : run) {
//				hand.remove(card);
//			}
//		}
//
//		return hand;
//	}
//
//	/** *********************************************************************
//	 Function Name: removeFirstRun
//	 Purpose: Remove the first valid run from hand
//	 Parameters:
//	 Vector<Card> hand the Cards to remove a run from
//	 Return Value:
//	 Vector<Card> the removed run or an empty Vector
//	 Assistance Received: None
//	 ********************************************************************* */
//	static private Vector<Card> getFirstRun(final Vector<Card> hand) {
//		// runs must be 3 Cards or longer
//		if (hand.size() < 3) {
//			return new Vector<Card>();
//		}
//
//		// track run position starting with first 3 Cards
//		int startIdx = 0, endIdx = 2;
//
//		// loop through all Cards until the last 2 (runs must be 3 long)
//		while (startIdx < hand.size() - 2) {
//
//			// check if the next Card can be used in run (from hand[startIdx] to hand[endIdx] inclusive)
//			if (endIdx != hand.size() && Assembler.isRun(new Vector(hand.subList(startIdx, endIdx + 1)))) {
//				// if so, continue to the next card
//				endIdx++;
//				continue;
//			} else {
//				// hand[endIdx] cannot be used in run
//				endIdx--;
//
//				// confirm hand[startIdx, endIdx] is a run
//				if (Assembler.isRun(new Vector(hand.subList(startIdx, endIdx + 1)))) {
//					// return the removed run
//					Vector<Card> run = new Vector(hand.subList(startIdx, endIdx + 1));
//					return run;
//				} else {
//					// book cannot exist starting with startIdx
//					startIdx++;
//				}
//				// adjust start/end
//				endIdx = startIdx + 2;
//
//				// stop at end of hand
//				if (endIdx >= hand.size()) {
//					break;
//				}
//			}
//		}
//
//		// no run found, return empty list
//		return new Vector<Card>();
//	}
//
//	/***********************************************************************
//	 Function Name: removeFirstRun
//	 Purpose: Remove the first valid run from hand
//	 Parameters:
//	 Vector<Card> hand the Cards to remove a run from
//	 Return Value:
//	 Vector<Card> the removed run or an empty Vector
//	 Assistance Received: None
//	 ********************************************************************* */
//	static private Vector<Card> removeFirstRun(Vector<Card> hand) {
//		Vector<Card> run = getFirstRun(hand);
//
//		for (Card card : run) {
//			hand.remove(card);
//		}
//
//		return hand;
//	}
}

	/** *********************************************************************
	 Function Name: helpDraw
	 Purpose: Advice the user what pile to draw from
	 Parameters:
	 final Vector<Card> drawPile the first pile to choose from
	 final Vector<Card> discardPile the second pile to choose from
	 string reason the reasoning behind the advice
	 Return Value:
	 0 if the draw pile should be chosen
	 1 if the discard pile should be chosen
	 Assistance Received: None
	 ********************************************************************* */
	protected int helpDraw(final Vector<Card> drawPile, final Vector<Card> discardPile) {
		// check if either deck is empty
		if (drawPile.size() == 0) {
			drawReason = "the draw pile is empty.";
			return 0;
		}
		if (discardPile.size() == 0) {
			drawReason = "the discard pile is empty.";
			return 1;
		}

		// look at the top of the discard pile
		Card discardTopCard = discardPile.firstElement();

		// check if discard pile is joker
		if (discardTopCard.isJoker()) {
			drawReason = "it is a Joker.";
			return 1;
		}
		// check if discard pile is wildcard
		else if (discardTopCard.isWildcard()) {
			drawReason = "it is a wildcard.";
			return 1;
		}

		// check if the discard pile can be used in a book
		int faceToMatch = discardTopCard.getFace();
		for (Card card : hand) {
			if (card.getFace() == faceToMatch) {
				// get face as character
				char face;
				switch (faceToMatch) {
					case 10:
						face = 'X';
						break;
					case 11:
						face = 'J';
						break;
					case 12:
						face = 'Q';
						break;
					case 13:
						face = 'K';
						break;
					default:
						face = (char) faceToMatch;
						break;
				}
				drawReason = "to use it in a book of " + face + "s.";
				return 1;
			}
		}

		// check if the discard pile can be used in a run
		for (Card card : hand) {
			// use discard pile if it has a matching suit and is one face value away
			if (card.getSuit() == discardTopCard.getSuit()
					&& (card.getFace() - 1 == discardTopCard.getFace()
					|| card.getFace() + 1 == discardTopCard.getFace())) {
				drawReason = "to use it in a run with " + card.toString() + ".";
				return 1;
			}
		}

		// discard pile cannot help - draw from draw pile
		drawReason = "the " + discardTopCard.toString() + " cannot be used for an assembly.";
		return 0;
	}

	/** *********************************************************************
	 Function Name: helpAssemble
	 Purpose: Advice the Player how to order their Cards
	 Parameters: None
	 Return Value:
	 Vector<Card> the hand in suggested order
	 Assistance Received: None
	 ********************************************************************* */
	private Vector<Card> helpAssemble() {

		// see if all cards but one can be fully assembled
		//	if so, return the assembly/ies with the remaining card at the end
		//	else return cheapestHand

		// iterate through the hand with each card once removed
		for (int i = 0; i < hand.size(); i++) {
			// remove card from copy of hand
			Vector<Card> handWithoutCard = new Vector<Card>(hand);
			handWithoutCard.remove(i);

			// get best assembly
			Assembler assembly = Assembler.getCheapestAssembly(new Assembler(handWithoutCard), new Assembler());

			// check if all cards are assembled
			if (assembly.getRemainingCards().size() == 0) {
				// return cards in order with removed card at the end
				Vector<Card> result = assembly.getAssemblies().firstElement();
				result.add(hand.get(i));
				return result;
			}
		}
		
		return Assembler.getCheapestHand(hand);
	}

	/** *********************************************************************
	 Function Name: helpDiscard
	 Purpose: Advice the user what Card to drop
	 Parameters:
	 string reason the reasong behind the advice
	 Return Value:
	 Card the Card to drop
	 Assistance Received: None
	 ********************************************************************* */
	Card helpDiscard() {
		// build assemblies on copy deck
		// check what is not used (copy is modified)
		// if only one, drop it

		// see if one card can be dropped and the others assembled
		for (int cardDropped = 0; cardDropped < hand.size(); cardDropped++) {
			Vector<Card> handWithoutOne = new Vector<Card>(hand);
			handWithoutOne.remove(cardDropped);

			// check if all cards are assembled
			if (Assembler.getCheapestAssembly(new Assembler(handWithoutOne), new Assembler()).getRemainingCards().size() == 0) {
				discardReason = "all other cards can be assembled.";
				return hand.get(cardDropped);
			}
		}

		// get best assembly (books and runs, remaining cards)
		Assembler bestAssembly = Assembler.getCheapestAssembly(new Assembler(hand), new Assembler());
		Vector<Card> handCopy = bestAssembly.getRemainingCards();
		Vector<Vector<Card>> assemblies = bestAssembly.getAssemblies();

		// remove one card, check if best assembly remaining == 0
		if (bestAssembly.getRemainingCards().size() == 1) {
			return bestAssembly.getRemainingCards().firstElement();
		}

		// remove double cards from hand
		//handCopy = removeDoubles(handCopy);

		// remove jokers/wildcards
		for (int i = 0; i < handCopy.size(); i++) {
			if (handCopy.get(i).isJoker() || handCopy.get(i).isWildcard()) {
				handCopy.remove(i);
			}
		}

		// remove first Card not used in assembly
		if (handCopy.size() > 0) {

			// don't drop a card if it has the potential to be used for a book or run
			for (int i = 0; i < handCopy.size() - 1; i++) {
				int face = handCopy.get(i).getFace();
				char suit = handCopy.get(i).getSuit();

				// loop through all the following cards in the hand
				boolean useful = false;
				for (int j = 0; j < handCopy.size(); j++) {

					// skip current card
					if (j == i) {
						continue;
					}
					// see if Card can be used for book
					if (handCopy.get(j).getFace() == face) {
						useful = true;
					}

					// see if Card can be used for run within range
					if (handCopy.get(j).getSuit() == suit
							&& (handCopy.get(j).getFace() - hand.size() + 2 <= face
							|| handCopy.get(j).getFace() + hand.size() - 2 >= face)) {
						useful = true;
					}
				}

				// determine if card is useful after checking following cards
				if (!useful) {
					discardReason = "it is not close to an assembly.";
					return handCopy.get(i);
				}
			}

			// drop the Card with the highest value
			int highestIdx = 0;
			int highestValue = handCopy.get(highestIdx).getPoints();
			for (int i = 0; i < handCopy.size(); i++) {
				// store highest index (besides jokers/wildcards)
				if (!(handCopy.get(i).isJoker() || handCopy.get(i).isWildcard()) && handCopy.get(i).getPoints() > highestValue) {
					highestIdx = i;
					highestValue = handCopy.get(highestIdx).getPoints();
				}
			}
			discardReason = "it has the highest value between the unused cards.";
			return handCopy.get(highestIdx);
		}
		else {
			// all cards are in assemblies - drop a card from the biggest one

			// find the largest assembly
			int largestSize = 0;
			int assemblyIdx = 0;
			for (int i = 0; i < assemblies.size(); i++) {
				if (assemblies.get(i).size() > largestSize) {
					assemblyIdx = i;
				}
			}

			// get the first Card in the largest assembly
			if (assemblies.size() > 0) {
				if (assemblies.get(0).size() > 0) {
					Card card = assemblies.get(assemblyIdx).firstElement();
					discardReason = "it is from the largest assembly.";
					return card;

				}
			}

			discardReason = "something went wrong";
			return new Card('0', 0);
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
	 * Return the Player's hand as a string
	 */
	public String toString() {
		String result = "";

		if (hand == null) {
			return "null";
		}

		for (Card card : hand) {
			result += card.toString() + " ";
		}

		return result;
	}

	public String getLastMoveDesc() {
		return lastMoveDesc;
	}

	public Vector<Vector<Card>> getAssemblies() {
		return Assembler.getCheapestAssembly(new Assembler(hand), new Assembler()).getAssemblies();
	}

	/** return cards remaining after assembly */
	public Vector<Card> getRemainingCards() {
		return Assembler.getCheapestAssembly(new Assembler(hand), new Assembler()).getRemainingCards();
	}

	// *********************************************************
	// ******************** Debugging Methods ******************
	// *********************************************************
};

// *********************************************************
// ******************** Trash Methods **********************
// *********************************************************
