package com.example.a5crowns;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.a5crowns.Deck;

import java.io.Serializable;

/*******************************************************
  Card class to hold one card
  @author Kevin Bagnato
  @since 10/27/2019
******************************************************** */

public class Card implements Comparable<Card>, Parcelable//, Serializable
{
    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************
	/** the default suit */
	public static final char DEFAULT_SUIT = 'X';
	
	/** the default face */
	public static final int DEFAULT_FACE = 0;
	
    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /** suit char representing the suit of the card: S/C/D/H/T/1/2/3 */
	private char suit;
	
	/** face int representing the face of the card Deck.MIN_FACE - Deck.MAX_FACE */
	private int face;
	
	/** represents if the card is a wildcard in the current round */
	private boolean wildcard;
	
    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************
   /** 
	Card constructor
    @param inSuit inSuit the given suit for the card
    @param inFace the given face for the card
    */
	public Card (char inSuit, int inFace) {
		if (!setSuit(inSuit)) {
			System.out.println("Card class reports incorrect suit: " + inSuit);
			System.out.println("Setting suit to: " + DEFAULT_SUIT);
		}
		
		if (!setFace(inFace)) {
			System.out.println("Card class reports incorrect face: " + inFace);
			System.out.println("Setting face to: " + DEFAULT_FACE);
		}
		
		// not a wildcard by default
		wildcard = false;
	}

	/**
	 Card constructor
	 @param inSuit inSuit the given suit for the card
	 @param inFace the given face for the card
	 */
	public Card (char inSuit, int inFace, boolean inWild) {
		this(inSuit, inFace);

		// not a wildcard by default
		wildcard = inWild;
	}

	public Card (Card card) {
		suit = card.suit;
		face = card.face;
		wildcard = card.wildcard;
	}

	Card (Parcel in) {
		face = in.readInt();
		suit = in.readString().charAt(0);
		if (in.readInt() == 1) {
			wildcard = true;
		}
		else {
			wildcard = false;
		}
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

	public static final Creator<Card> CREATOR = new Creator<Card>() {
		@Override
		public Card createFromParcel(Parcel in) {
			return new Card(in);
		}

		@Override
		public Card[] newArray(int size) {
			return new Card[size];
		}
	};

	/**
	 * Return the suit
	 * @return the suit
	 */
	final public char getSuit() {
		return suit;
	}

	/**
	 * Return the face
	 * @return the face
	 */
	final public int getFace() {
		return face;
	}
	
	/**
	 *  Return the value of the card
	 * @return face if the face is 3-13 
		50 if the Card is a Joker 
		20 if the Card is a wildcard
	 */
	final public int getPoints() {
		if (wildcard) {
			return 20;
		}
		
		if (isJoker()) {			
			return 50;
		}
		
		return face;
	}
	
	/**
	 * Compare a given Card to this
	 * @param right the Card to compare to
	 * @return 0 if the Cards have matching face and suit
	 *	-1 if this Card has a lower Face than the right Card
	 * 		or if the faces match and this Card has a lower suit
	 * 	1 if this Card has a greater face than the right Card
	 * 		or if the faces match and this Card has a lower face than the right Card
	 */
	public int compareTo(Card right) {
		if (face == right.face && suit == right.suit) {
			return 0;
		}
		
		if (face < right.face) {
			return -1;
		}
		if (face > right.face) {
			return 1;
		}
		
		// faces are the same at this point
		// TODO do we want to sort by suit in the order specified in Deck.SUITS?
		if (face < right.face) {
			return -1;
		}
		if (face > right.face) {
			return 1;
		}
		return 0;
	}

	/**
	 * Return wildcard status
	 * @return true if wildcard, else false
	 */
	final public boolean isWildcard() {
		return wildcard;
	}
	
	/**
	 * Determine if the Card is a Joker
	 * @return true if the Card is a Joker
	 * 	false if the Card is not a Joker
	 */
	final public boolean isJoker() {
		return (face == 11 && (suit == '1' || suit == '2' || suit == '3'));
	}
	
    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************
   /** 
   Update the suit.
   @param inSuit the new suit value
   @return Returns true if inSuit is valid, false if not
   */
	public boolean setSuit(char inSuit) {
		inSuit = Character.toUpperCase(inSuit);
		boolean status = false;
		
		switch (inSuit) {
		case 'S':
		case 'H':
		case 'C':
		case 'D':
		case 'T':
		case '1':
		case '2':
		case '3':
			suit = inSuit;
			status = true;
			break;
		default:
			suit = DEFAULT_SUIT;
			status = false;
			break;
		}
		
		return status;
	}
	
   /** 
   Update the face.
   @param inFace the new face value
   @return Returns true if inFace is valid, false if not
   */
	public boolean setFace(int inFace) {
		boolean status = false;
		
		if (inFace >= Deck.MIN_FACE && inFace <= Deck.MAX_FACE) {
			face = inFace;
			status = true;
		}
		else {
			face = DEFAULT_FACE;
			status = false;
		}
		
		return status;		
	}
	
   /** 
	Update the wildcard for the current round
	@param inFace the wildcard
   */
	public void updateWildcard(int inFace) {
		if (face == inFace) {
			wildcard = true;			
		}
		else {
			wildcard = false;
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
	 * Return the card as a string
	 * @return the card as a string
	 */
	final public String toString() {
		switch (face) {
		case 10:
			return "X" + Character.toString(suit);
		case 11:
			return "J" + Character.toString(suit);
		case 12:
			return "Q" + Character.toString(suit);
		case 13:
			return "K" + Character.toString(suit);
		default:
			return face + Character.toString(suit);				
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// pass face
		dest.writeInt(face);

		// pass suit
		dest.writeString(String.valueOf(suit));

		// pass wildcard as int
		if (wildcard) {
			dest.writeInt(1);
		}
		else {
			dest.writeInt(0);
		}
	}

	// *********************************************************
    // ******************** Debugging Methods ******************
    // *********************************************************

    
};

    // *********************************************************
    // ******************** Trash Methods **********************
    // *********************************************************
