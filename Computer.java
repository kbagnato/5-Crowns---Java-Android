package com.example.a5crowns;

import java.util.Vector;

/*******************************************************
 Represents Human player
 @author Kevin Bagnato
 @since 10/27/2019
  ******************************************************** */

public class Computer extends Player
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
     * The Computer constructor
     */
    public Computer() {
        super();
        //lastMoveDesc = "";
    }

    public Computer(int inScore, Vector<Card> inHand) {
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

    /**
     * Return the last move description
     * @return the last move description
     */
    @Override
    final public String getLastMoveDesc() {
        return lastMoveDesc;
    }

    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************
    /**
     * Move a Card from the user-selected Deck to the Player's hand
     * @param drawPile drawPile the first deck to choose from
     * @param discardPile the second deck to choose from
     */
    @Override
    public void drawCard(Vector<Card> drawPile, Vector<Card> discardPile) {
        // get advice and reason from base class
        int choice = super.helpDraw(drawPile, discardPile);

        // choose from advised pile
        if (choice == 0) {
            lastMoveDesc = "Computer is drawing from the draw pile because " + drawReason + " It's a " + drawPile.firstElement().toString();
            super.drawCard(drawPile);
        }
        else {
            lastMoveDesc = "Computer is drawing " + discardPile.get(0).toString() + " from the discard pile because " + drawReason;
            super.drawCard(discardPile);
        }
    }

    /**
     * Move a user-selected Card from the Human's hand to the given pile
     * @param discardPile the pile to move the Card to
     */
    @Override
    public void discardCard(Vector<Card> discardPile) {
        // get advice and reason from base class
        Card choice = super.helpDiscard();

        // print reasoning
        lastMoveDesc += " \nComputer is dropping the " + choice.toString() + " because " + discardReason;

        // choose from advised pile
        super.discardCard(choice, discardPile);
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
