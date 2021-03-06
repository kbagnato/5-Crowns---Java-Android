package com.example.a5crowns;

import java.util.Vector;

/*******************************************************
  Represents Human player
  @author Kevin Bagnato
  @since 10/27/2019
******************************************************** */

public class Human extends Player
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

	// *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************


	/**
	 * The Human constructor
	 */
	public Human() {
		super();
	}


	public Human(int inScore, Vector<Card> inHand) {
		super(inScore, inHand);
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

	// *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************

	/**
	 * Determine which card picked up offers the lowest points in hand after assembly
	 * @return a string saying which pile to draw from
	 */
	public String offerHelpDraw(Vector<Card> drawPile, Vector<Card> discardPile) {
		// get advice and reason from base class
		int choice = super.helpDraw(drawPile, discardPile);

		// print reasoning
		return "You should draw from the " + (choice == 0 ? "draw" : "discard") + " pile because " + drawReason;
	}

	public String offerHelpDiscard() {
		// get advice and reason from base class
		Card choice = super.helpDiscard();

		// print reasoning
		return "You should drop the " + choice.toString() + " because " + discardReason;
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

    // *********************************************************
    // ******************** Debugging Methods ******************
    // *********************************************************

};

    // *********************************************************
    // ******************** Trash Methods **********************
    // *********************************************************
