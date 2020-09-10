package com.example.a5crowns;

import java.util.Vector;

/*******************************************************
 Runs one round of 5 Crowns game
 @author Kevin Bagnato
 @since 10/27/2019
  ******************************************************** */

public class Round {
    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************
    /** The number of players in game */
    public static final int TOTAL_PLAYERS = 2;

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************
    /** Number of round which determines size of player's hands */
    private int roundNum;

    /** Array of Players in the round */
    private Player[] playerArray;

    /** Index in playerArray indicating next Player to play */
    private int nextPlayerIndex;

    /** CardDealer for this round */
    private CardDealer cardDealer;

    /** The draw pile for this round */
    private Vector<Card> drawPile;

    /** The discard pile for this round */
    private Vector<Card> discardPile;

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************

    /**
     * For each function, one-line description of the function
     *
     * @param inRoundNum Describe the parameter, starting with its data type
     * @return What the function returns - don't include if void. Also list special cases, such as what is returned if error.
     */
    public Round(int inRoundNum, Human inHuman, Computer inComputer) {
        // set local copy of round number
        roundNum = inRoundNum;

        // create TOTAL_PLAYERS players
        playerArray = new Player[TOTAL_PLAYERS];
        playerArray[0] = inHuman;
        playerArray[1] = inComputer;

        // update wild cards
        cardDealer = new CardDealer();
        cardDealer.updateWildcards(roundNum + 2);

        // deal cards to players
        for (int count = 0; count < roundNum + 2; count++) {
            for (int playerIndex = 0; playerIndex < playerArray.length; playerIndex++) {
                playerArray[playerIndex].addCard(cardDealer.dealCard());
            }
        }

        // deal rest of cards to DrawPile
        drawPile = new Vector<Card>(Deck.DECK_SIZE * 2);
        Card nextCard = cardDealer.dealCard();
        while (nextCard != null) {
            drawPile.add(nextCard);
            nextCard = cardDealer.dealCard();
        }

        // move top card from DrawPile to DiscardPile
        discardPile = new Vector<Card>(Deck.DECK_SIZE * 2);
        discardPile.add(drawPile.get(0));
        drawPile.remove(0);
    }


    public Round(int inRoundNum, Human inHuman, Computer inComputer, int inNextPlayerIdx, Vector<Card> inDraw, Vector<Card> inDiscard) {
        roundNum = inRoundNum;

        playerArray = new Player[TOTAL_PLAYERS];
        playerArray[0] = inHuman;
        playerArray[1] = inComputer;

        nextPlayerIndex = inNextPlayerIdx;

        drawPile = new Vector<>(inDraw);
        discardPile = new Vector<>(inDiscard);
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
     * Return the round number
     * @return the round number
     */
    final public int getRoundNum() {
        return roundNum;
    }

    /**
     * Return the draw pile
     * @return the draw pile
     */
    final  public Vector<Card> getDrawPile() {
        return drawPile;
    }

    public void setDrawPile(Vector<Card> inDrawPile) {
        drawPile = inDrawPile;
    }

    /**
     * Return the discard pile
     * @return the discard pile
     */
    final public Vector<Card> getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(Vector<Card> inDiscardPile) {
        discardPile = inDiscardPile;
    }

    /**
     * Return the next player index
     * @return the next player index
     */
    final public int getNextPlayerIndex() {
        return nextPlayerIndex;
    }

    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************

    /**
     * set the next player index
     * @param nextPlayerIndex the next player index
     */
    public void setNextPlayerIndex(int nextPlayerIndex) {
        this.nextPlayerIndex = nextPlayerIndex;
    }

    /**
     * Increment the next player index
     */
    public void nextPlayer() {
        nextPlayerIndex++;
        if (nextPlayerIndex == TOTAL_PLAYERS) {
            nextPlayerIndex = 0;
        }
    }

    /**
     * Step the computer through its turn
     * @return true if the player went out, else false
     */
    public boolean moveComputer() {

        // draw card from DrawPile or DiscardPile
        playerArray[1].drawCard(drawPile, discardPile);

        // discard a card
        playerArray[1].discardCard(discardPile);

        // assemble cards
        playerArray[1].assemble();

        return playerArray[1].canGoOut();
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
     * Print the round as a string
     * @return the round as a string
     */
    public String toString() {
        // add round number
        String result = "Round: " + roundNum + "\n";

        // add human hand and score
        result += "Computer:\n\tScore: " + playerArray[1].getScore() + "\n\t"
                + "Hand: " + playerArray[1].toString() + "\n";

        // add computer hand and score
        result += "Human:\n\tScore: " + playerArray[0].getScore() + "\n\t"
                + "Hand: " + playerArray[0].toString() + "\n";

        // add draw pile
        result += "Draw Pile: ";
        for (Card card : drawPile) {
            result += card.toString() + " ";
        }
        result += "\n";

        // add discard pile
        result += "Discard Pile: ";
        for (Card card : discardPile) {
            result += card.toString() + " ";
        }
        result += "\n";

        result += "Next Player: ";
        if (nextPlayerIndex == 0) {
            result += "Human\n";
        } else {
            result += "Computer\n";
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
