package com.example.a5crowns;

import java.util.Collections;
import java.util.Vector;

public class Assembler {

	/** a list of the books & runs removed from the hand */
	private Vector<Vector<Card>> m_assemblies;

	/** the cards remaining after assemblies are removed */
	private Vector<Card> m_remainingCards;

	/**
	 * Basic contructor
	 */
	public Assembler() {
		m_assemblies = new Vector<Vector<Card>>();
		m_remainingCards = new Vector<Card>();
	}

	/** Constructor with given cards */
	public Assembler(Vector<Card> inRemainingCards) {
		m_remainingCards = new Vector(inRemainingCards);
		m_assemblies = new Vector<Vector<Card>>();
	}

	/**
	 * Constructor with given variables
	 * @param inAssemblies the assemblies to set
	 * @param inRemainingCards the remaining cards to set
	 */
	public Assembler(Vector<Vector<Card>> inAssemblies, Vector<Card> inRemainingCards) {
		m_assemblies = new Vector<Vector<Card>>(inAssemblies);
		m_remainingCards = new Vector<Card>(inRemainingCards);
	}

	/**
	 * Copy constructor
	 * @param inAssembler the object to copy values from
	 */
	public Assembler(Assembler inAssembler) {
		m_assemblies = new Vector<Vector<Card>>(inAssembler.m_assemblies);
		m_remainingCards = new Vector<Card>(inAssembler.m_remainingCards);
	}

	/**
	 * Returns all possible books & runs that can be made with the given hand
	 * @param inHand the cards to remove assemblies from
	 * @return a list of all possible assemblies
	 */
	static public Vector<Vector<Card>> getAllAssemblies(final Vector<Card> inHand) {
		// total list of books
		Vector<Vector<Card>> allAssemblies = new Vector<Vector<Card>>();

		// get books and runs
		Vector<Vector<Card>> books = getAllBooks(inHand);
		Vector<Vector<Card>> runs = getAllRuns(inHand);

		// check if the entire are a book or a run
		if (isBook(inHand) || isRun(inHand)) {
			// move cards from remaining to assembled
			allAssemblies.add(new Vector<Card>(inHand));
		}

		// concatenate lists
		allAssemblies.addAll(books);
		allAssemblies.addAll(runs);

		// return list
		return allAssemblies;


//		Vector<Vector<Card>> allAssemblies = new Vector<Vector<Card>>();
//
//		// try to make an assembly starting with each card
//		for (int startIdx = 0; startIdx < inHand.size(); startIdx++) {
//			Vector<Card> hand = new Vector<Card>(inHand);
//			Vector<Card> possibleBook = new Vector<Card>();
//			Vector<Card> possibleRun = new Vector<Card>();
//
//			// start possible assemblies with start card
//			possibleBook.add(hand.get(startIdx));
//			possibleRun.add(hand.get(startIdx));
//
//			// remove card from hand to avoid double counting
//			hand.remove(startIdx);
//
//			// store face and suit for convenience
//			int face = possibleBook.get(0).getFace();
//			char suit = possibleBook.get(0).getSuit();
//
//			// see if other cards can be used for books
//			Vector<Card> handFaceSort = sortHandByFace(hand);
//			for (int cmpIdx = 0; cmpIdx < handFaceSort.size(); cmpIdx++ ) {
//				// check for possible book
//				if (handFaceSort.get(cmpIdx).isJoker()
//						|| handFaceSort.get(cmpIdx).isWildcard()
//						|| face == handFaceSort.get(cmpIdx).getFace()) {
//					possibleBook.add(handFaceSort.get(cmpIdx));
//
//					// add valid book to return value
//					if (isBook(possibleBook)) {
//						allAssemblies.add(possibleBook);
//
//						// create copy of self to avoid overwriting existing assemblies
//						possibleBook = new Vector<Card>(possibleBook);
//					}
//				}
//			}
//
//			// see if following cards can be used for runs
//			Vector<Card> handSuitSort = Assembler.sortHandBySuit(hand);
//			for (int cmpIdx = startIdx + 1; cmpIdx < handSuitSort.size(); cmpIdx++) {
//				// check for possible run
//				if (handSuitSort.get(cmpIdx).isJoker()
//						|| handSuitSort.get(cmpIdx).isWildcard()
//						|| (suit == handSuitSort.get(cmpIdx).getSuit()
//						&& (face < handSuitSort.get(cmpIdx).getFace() + handSuitSort.size()
//						|| face > handSuitSort.get(cmpIdx).getFace() - handSuitSort.size()))) {
//
//					// add cards with matching suit in face range to possible run
//					possibleRun.add(handSuitSort.get(cmpIdx));
//
//					// add valid books to return value
//					if (isRun(possibleRun)) {
//						allAssemblies.add(possibleRun);
//
//						// create copy of self to avoid overwriting existing assemblies
//						possibleRun = new Vector<Card>(possibleRun);
//					}
//				}
//			}
//		}
//
//		// remove duplicates from list (caused by sorting which interferes with its outer incremental loop)
//		for (int search = 0; search < allAssemblies.size() - 1; search++) {
//			for (int i = search + 1; i < allAssemblies.size(); i++) {
//				if (Assembler.sortHandBySuit(allAssemblies.get(search)) == Assembler.sortHandBySuit(allAssemblies.get(i))) {
//					allAssemblies.remove(i);
//					i--;
//				}
//			}
//		}
//
//		// return list of all possible assemblies
//		return allAssemblies;
	}

	/**
	 * Return all books from the given hand
	 * @param inHand the hand to check for books
	 * @return the list of all possible books
	 */
	static public Vector<Vector<Card>> getAllBooks(final Vector<Card> inHand) {
		// create a list of all possible assemblies from given hand
		Vector<Vector<Card>> allBooks = new Vector<Vector<Card>>();

		// try to make an assembly starting with each card
		for (int startIdx = 0; startIdx < inHand.size(); startIdx++) {
			Vector<Card> hand = new Vector<Card>(inHand);
			Vector<Card> possibleBook = new Vector<Card>();

			// start possible assemblies with start card
			possibleBook.add(hand.get(startIdx));

			// remove card from hand to avoid double counting
			hand.remove(startIdx);

			// store face and suit for convenience
			int face = possibleBook.get(0).getFace();

			// see if other cards can be used for books
			Vector<Card> handFaceSort = sortHandByFace(hand);
			for (int cmpIdx = 0; cmpIdx < handFaceSort.size(); cmpIdx++ ) {
				// check for possible book
				if (handFaceSort.get(cmpIdx).isJoker()
						|| handFaceSort.get(cmpIdx).isWildcard()
						|| face == handFaceSort.get(cmpIdx).getFace()) {
					possibleBook.add(handFaceSort.get(cmpIdx));

					// add valid book to return value
					if (isBook(possibleBook)) {
						allBooks.add(possibleBook);

						// create copy of self to avoid overwriting existing assemblies
						possibleBook = new Vector<Card>(possibleBook);
					}
				}
			}
		}

		// remove duplicates from list (caused by sorting which interferes with its outer incremental loop)
		for (int search = 0; search < allBooks.size() - 1; search++) {
			for (int i = search + 1; i < allBooks.size(); i++) {
				if (Assembler.sortHandBySuit(allBooks.get(search)) == Assembler.sortHandBySuit(allBooks.get(i))) {
					allBooks.remove(i);
					i--;
				}
			}
		}

		// return list of all possible assemblies
		return allBooks;
	}

	/**
	 * Return all runs from the given hand
	 * @param inHand the hand to check for runs
	 * @return the list of all possible runs
	 */
	static public Vector<Vector<Card>> getAllRuns(final Vector<Card> inHand) {
		// create a list of all possible assemblies from given hand
		Vector<Vector<Card>> allRuns = new Vector<Vector<Card>>();

		// TODO this is different from my CPP code, likely does not work 100%
		// 	there is no next_permutation in java, and it's horribly inefficient,
		//  so check this out https://leetcode.com/problems/next-permutation/solution/


		// try to make an assembly starting with each card
		for (int startIdx = 0; startIdx < inHand.size(); startIdx++) {
			Vector<Card> hand = new Vector<Card>(inHand);
			Vector<Card> possibleRun = new Vector<Card>();

			// start possible assemblies with start card
			possibleRun.add(hand.get(startIdx));

			// remove card from hand to avoid double counting
			hand.remove(startIdx);

			// store face and suit for convenience
			int face = possibleRun.get(0).getFace();
			char suit = possibleRun.get(0).getSuit();

			// see if following cards can be used for runs
			Vector<Card> handSuitSort = Assembler.sortHandBySuit(hand);
			for (int cmpIdx = startIdx + 1; cmpIdx < handSuitSort.size(); cmpIdx++) {
				// check for possible run
				if (handSuitSort.get(cmpIdx).isJoker()
						|| handSuitSort.get(cmpIdx).isWildcard()
						|| (suit == handSuitSort.get(cmpIdx).getSuit()
						&& (face < handSuitSort.get(cmpIdx).getFace() + handSuitSort.size()
						|| face > handSuitSort.get(cmpIdx).getFace() - handSuitSort.size()))) {

					// add cards with matching suit in face range to possible run
					possibleRun.add(handSuitSort.get(cmpIdx));

					// add valid books to return value
					if (isRun(possibleRun)) {
						allRuns.add(possibleRun);

						// create copy of self to avoid overwriting existing runs
						possibleRun = new Vector<Card>(possibleRun);
					}
				}
			}
		}

		// remove duplicates from list (caused by sorting which interferes with its outer incremental loop)
		for (int search = 0; search < allRuns.size() - 1; search++) {
			for (int i = search + 1; i < allRuns.size(); i++) {
				if (Assembler.sortHandBySuit(allRuns.get(search)) == Assembler.sortHandBySuit(allRuns.get(i))) {
					allRuns.remove(i);
					i--;
				}
			}
		}

		// return list of all possible runs
		return allRuns;
	}

	/**
	 * Determine the cheapest assembly of a given assembly
	 *
	 * for each assembly in allAssemblies
	 * 	build list of children
	 * 	if no children, return self (leaf node)
	 * 	else, build list of children where each child has a unique assembly removed
	 * 	find and return the cheapepst child with recurive calls
	 *
	 * @param inAssembly the object to find the best assemblies from
	 * @param cheapestChild the Assembly with the lowest score of remaining cards
	 * @return a new Assembler with the most assemblies and cheapest remaining cards
	 */
	static public Assembler getCheapestAssembly(Assembler inAssembly, Assembler cheapestChild) {
		// make a list of all possible assemblies from remaining cards
		Vector<Vector<Card>> allAssemblies = getAllAssemblies(inAssembly.getRemainingCards());

		// check if assemblies can be made from hand
		if (allAssemblies.size() == 0 || inAssembly.m_remainingCards.size() == 0) {
			// no possible assemblies remaining
			return inAssembly;
		}
		else {
			// create children nodes
			Vector<Assembler> children = new Vector<Assembler>();

			// populate each child with one assembly removed
			for (Vector<Card> assembly : allAssemblies) {
				Assembler child = new Assembler(inAssembly);
				child.removeAssembly(assembly);
				children.add(child);
			}

			// find the cheapest node of the children
			for (Assembler child : children) {
				if (cheapestChild.isEmpty() ||
						child.getCheapestAssembly(child, cheapestChild).getScore() < cheapestChild.getScore()) {
					cheapestChild = child.getCheapestAssembly(child, cheapestChild);
				}
			}

			return cheapestChild;
		}
	}

	/**
	 * Determine the best order for the cards
	 * @param inHand the given hand to sort
	 * @return the hand with assemblies in order followed by reamining cards
	 */
	static public Vector<Card> getCheapestHand(final Vector<Card> inHand) {
		// get the best assembly from the given hand
		Assembler bestAssembly = Assembler.getCheapestAssembly(new Assembler(inHand), new Assembler());

		// value to return
		Vector<Card> result = new Vector<Card>();

		// add all assemblies to hand
		for (Vector<Card> assembly : bestAssembly.getAssemblies()) {
			// add all cards in assembly
			for (Card card : assembly) {
				result.add(card);
			}
		}

		// add all remaining cards to hand
		for (Card card : bestAssembly.getRemainingCards()) {
			result.add(card);
		}

		// return all cards in bestAssembly
		return result;
	}

	/**
	 * Determine if the assembly is empty
	 * @return true if there are no cards
	 */
	private boolean isEmpty() {
		return (m_assemblies.size() == 0 && m_remainingCards.size() == 0);
	}

	/**
	 * Move an assembly from the remaining cards to the list of assemblies
	 * @param assembly the cards to move
	 */
	private void removeAssembly(Vector<Card> assembly) {
		// add assembly to list
		m_assemblies.add(assembly);

		// remove assembly from hand
		for (int i = 0; i < assembly.size(); i++) {
			m_remainingCards.remove(assembly.get(i));
		}
	}

	/**
	 * Return the assembly as a string
	 */
	public String toString() {
		String result = "(";

		result += "Assemblies: " + m_assemblies + ", ";
		result += "Remaining cards: " + m_remainingCards + ", ";
		result += "Score: " + getScore();

		return result + ")";
	}

	/**
	 * Get the list of assemblies
	 * @return the list of assemblies
	 */
	public Vector<Vector<Card>> getAssemblies() {
		return m_assemblies;
	}

	/**
	 * Set the assemblies
	 * @param assemblies the assemblies to set
	 */
	public void setAssemblies(Vector<Vector<Card>> assemblies) {
		this.m_assemblies = assemblies;
	}

	/**
	 * Get the remaining cards
	 * @return the remaining cards
	 */
	public Vector<Card> getRemainingCards() {
		return m_remainingCards;
	}

	/**
	 * Set the remaining cards
	 * @param remainingCards the cards to set
	 */
	public void setRemainingCards(Vector<Card> remainingCards) {
		this.m_remainingCards = remainingCards;
	}

	/**
	 * Get the score of the remaining cards
	 * @return the sum value of the remaining cards
	 */
	public int getScore() {
		//reset score
		int score = 0;

		// add points to score
		for (Card card : m_remainingCards) {
			score += card.getPoints();
		}

		return score;
	}

	/**
	 * Determine if the given hand is a valid run
	 * @param hand the cards to check. must be sorted by face
	 * @return true if the hand is a run, false otherwise
	 */
	static public boolean isRun(Vector<Card> hand) {
		// run must be 3 or more cards long
		if (hand.size() < 3) {
			return false;
		}

		// sort by suit, face
		// count/remove all jokers/wildcards
		// if run is about to break, try to use joker/wildcard if it's not possible

		hand = sortHandBySuit(hand);
		int totalWilds = 0;
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i).isJoker() || hand.get(i).isWildcard()) {
				totalWilds++;
				hand.remove(i);
				i--;
			}
		}

		// cards must have matching suit
		int suitToMatch = hand.get(0).getSuit();

		// track current face to determine if next card follows
		int currentFace = hand.get(0).getFace();

		// check cards by stepping through pairs in the hand
		for (int leftCard = 0; leftCard < hand.size() - 1; leftCard++) {
			int rightCard = leftCard + 1;

//			// no need to compare jokers/wildcards
//			if (hand.get(leftCard).isJoker() || hand.get(leftCard).isWildcard()
//					|| hand.get(rightCard).isJoker() || hand.get(rightCard).isWildcard()) {
//				currentFace++;
//				continue;
//			}

			// cards must match suit
			if (hand.get(rightCard).getSuit() != suitToMatch) {
				if (totalWilds > 0) {
					totalWilds--;
					leftCard--;
				}
				else {
					return false;
				}
			}

			// check if rightCard is one face higher than leftCard
			if (hand.get(rightCard).getFace() != currentFace + 1) {
				if (totalWilds > 0) {
					totalWilds--;
					leftCard--;
				}
				else {
					return false;
				}
			}

			// go to next face
			currentFace++;
		}

		// no cards failed test
		return true;
	}

	/**
	 * Determine if the given hand is a valid book
	 * @param hand the cards to check
	 * @return true if the hand is a book, false otherwise
	 */
	static public boolean isBook(final Vector<Card> hand) {
		// books must be 3 Cards or longer
		if (hand.size() < 3) {
			return false;
		}

		// set the face to match
		int face = hand.firstElement().getFace();

		// check if first card is joker/wildcard
		for (int i = 0; i < hand.size() - 1; i++) {
			if (hand.get(i).isJoker() || hand.get(i).isWildcard()) {
				face = hand.get(i + 1).getFace();
			} else {
				break;
			}
		}

		// check if all Cards have the same face
		for (int i = 1; i < hand.size(); i++) {
			if (hand.get(i).getFace() != face) {
				// jokers and wildcards continue books
				if (hand.get(i).isJoker() || hand.get(i).isWildcard()) {
					continue;
				} else {
					return false;
				}
			}
		}

		// book has not broken
		return true;
	}

	public static Vector<Card> sortHandByFace(final Vector<Card> inHand) {
		Vector<Card> hand = new Vector<>(inHand);

		// bubble sort the hand by face (ascending)
		for (int i = 0; i < hand.size() - 1; i++) {
			for (int j = 0; j < hand.size() - i - 1; j++) {
				// swap Cards if current Card is greater than next Card
				// or current Card is Joker
				if (hand.get(j).getFace() > hand.get(j + 1).getFace()
						|| hand.get(j).isJoker()) {
					Collections.swap(hand, j, j + 1);
				}
			}
		}

		// bubble sort jokers by suit
		for (int i = 0; i < hand.size() - 1; i++) {
			for (int j = 0; j < hand.size() - i - 1; j++) {
				// if cards are both jokers, sort by suit ascending
				if (hand.get(j).isJoker() && hand.get(j + 1).isJoker() && hand.get(j).getSuit() > hand.get(j + 1).getSuit()) {
					Collections.swap(hand, j, j + 1);
				}
			}
		}

		return hand;
	}

	/** *********************************************************************
	 Function Name: sortHandBySuit
	 Purpose: Sort the Computer's hand by suit then face
	 Parameters:
	 final Vector<Card> inHand the hand to sort
	 Return Value:
	 Vector<Card> the sorted hand
	 Assistance Received: https://www.geeksforgeeks.org/bubble-sort/
	 ********************************************************************* */
	public static Vector<Card> sortHandBySuit(final Vector<Card> inHand) {
		Vector<Card> hand = new Vector<>(inHand);

		// bubble sort the hand by suit (ascending)
		for (int i = 0; i < hand.size() - 1; i++) {
			for (int j = 0; j < hand.size() - i - 1; j++) {
				// swap Cards if
				//	current Card is Joker
				//	OR current Card suit is greater than next Card suit
				//	OR suits are same and current Card face is greater than next Card face
				if (hand.get(j).isJoker()) {
					Collections.swap(hand, j, j + 1);
				} else if (hand.get(j).getSuit() > hand.get(j + 1).getSuit()) {
					Collections.swap(hand, j, j + 1);
				} else if (hand.get(j).getSuit() == hand.get(j + 1).getSuit()
						&& hand.get(j).getFace() > hand.get(j + 1).getFace()) {
					Collections.swap(hand, j, j + 1);
				}
			}
		}

		// bubble sort jokers by suit
		for (int i = 0; i < hand.size() - 1; i++) {
			for (int j = 0; j < hand.size() - i - 1; j++) {
				// if cards are both jokers, sort by suit ascending
				if (hand.get(j).isJoker() && hand.get(j + 1).isJoker() && hand.get(j).getSuit() > hand.get(j + 1).getSuit()) {
					Collections.swap(hand, j, j + 1);
				}
			}
		}

		return hand;
	}

}


