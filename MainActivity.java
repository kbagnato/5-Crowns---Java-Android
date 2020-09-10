package com.example.a5crowns;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.NoSuchElementException;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	// the game we're playing
	Game game;

	// game states
	boolean userIsSelectingPile = false;
	boolean userIsSelectingCard = false;
	boolean lastMove = false;

	/**
	 * Display activity, print title, set onClick listeners.
	 * @param savedInstanceState idk it's mandatory
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// display the activity
		setContentView(R.layout.activity_main);

		// print title to console
		final TextView console = findViewById(R.id.tv_console);
		console.setMovementMethod(new ScrollingMovementMethod());
		print(console, getString(R.string.title));

		// grab buttons
		final Button newGameBtn = findViewById(R.id.b_newGame);
		final Button serializeBtn = findViewById(R.id.b_serialize);
		final Button moveBtn = findViewById(R.id.b_move);
		final Button helpBtn = findViewById(R.id.b_help);
		final Button goOutBtn = findViewById(R.id.b_goOut);
		final Button nextRoundBtn = findViewById(R.id.b_nextRound);
//		final Button drawBtn = findViewById(R.id.b_draw);
//		final Button dropBtn = findViewById(R.id.b_drop);


		// grab draw and discard piles
		final ImageButton drawPileBtn = findViewById(R.id.ib_draw);
		final ImageButton discardPileBtn = findViewById(R.id.ib_discard);

		// add onClick listeners to all buttons, Draw Pile, and Discard Pile
		newGameBtn.setOnClickListener(this);
		serializeBtn.setOnClickListener(this);
		goOutBtn.setOnClickListener(this);
		nextRoundBtn.setOnClickListener(this);
		moveBtn.setOnClickListener(this);
		helpBtn.setOnClickListener(this);
		discardPileBtn.setOnClickListener(this);
		drawPileBtn.setOnClickListener(this);

		// disable Move, Help, Go Out, Next Round buttons before game starts
		moveBtn.setEnabled(false);
//		drawBtn.setEnabled(false);
//		dropBtn.setEnabled(false);
		helpBtn.setEnabled(false);
		goOutBtn.setEnabled(false);
		nextRoundBtn.setEnabled(false);
	}

	/**
	 * Handle all button and Card clicks.
	 * @param v the View clicked
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			// handle button input
			case R.id.b_newGame:
				newGame();
				break;
			case R.id.b_serialize:
				serialize();
				break;
			case R.id.b_move:
				move();
				break;
			case R.id.b_goOut:
				userGoOut();
				break;
			case R.id.b_nextRound:
				nextRound();
				break;
			case R.id.b_help:
				help();
				break;
			case R.id.ib_draw:
				drawPileClicked();
				break;
			case R.id.ib_discard:
				discardPileClicked();
				break;
			case R.id.ib_humanCard0:
				humanCardClicked(0);
				break;
			case R.id.ib_humanCard1:
				humanCardClicked(1);
				break;
			case R.id.ib_humanCard2:
				humanCardClicked(2);
				break;
			case R.id.ib_humanCard3:
				humanCardClicked(3);
				break;
			case R.id.ib_humanCard4:
				humanCardClicked(4);
				break;
			case R.id.ib_humanCard5:
				humanCardClicked(5);
				break;
			case R.id.ib_humanCard6:
				humanCardClicked(6);
				break;
			case R.id.ib_humanCard7:
				humanCardClicked(7);
				break;
			case R.id.ib_humanCard8:
				humanCardClicked(8);
				break;
			case R.id.ib_humanCard9:
				humanCardClicked(9);
				break;
			case R.id.ib_humanCard10:
				humanCardClicked(10);
				break;
			case R.id.ib_humanCard11:
				humanCardClicked(11);
				break;
			case R.id.ib_humanCard12:
				humanCardClicked(12);
				break;
			default:
				// print view ID
				print((TextView) findViewById(R.id.tv_console), "view: " + v.getId());
		}
	}

	/**
	 * when draw pile is clicked
	 */
	private void drawPileClicked() {
		if (game != null) {
			// print full pile to console
			// TODO change this to long press
//			print((TextView) findViewById(R.id.tv_console), "Draw Pile: " + game.getRound().getDrawPile().toString());

			// if human's turn, draw card from selected pile
			if (userIsSelectingPile) {
				userSelectPile(game.getRound().getDrawPile());
			}
		}
	}

	/**
	 * when discard pile is clicked
	 */
	private void discardPileClicked() {
		if (game != null) {
			// print full pile to console
			// TODO change this to long press
//				print((TextView) findViewById(R.id.tv_console), "Discard Pile: " + game.getRound().getDiscardPile().toString());

			// if human's turn, draw card from selected pile
			if (userIsSelectingPile) {
				userSelectPile(game.getRound().getDiscardPile());
			}
		}
	}

	/**
	 * when human card is clicked, and userIsSelectingCard,
	 * have the user drop the selected card
	 */
	private void humanCardClicked(int index) {
		if (userIsSelectingCard) {
			// have user drop the selected card
			Card card = game.getHuman().getHand().elementAt(index);
			userSelectCard(card);
		}
	}

	/**
	 * Human draws a Card from the given pile and sets flags for user to drop a Card
	 * @param pile
	 */
	private void userSelectPile(Vector<Card> pile) {
		// todo make sure user does not select empty pile
		game.getHuman().drawCard(pile);
		// update order of human's hand to the computer's best order
		game.getHuman().assemble();

		updateBoard();
		userIsSelectingPile = false;

		// begin user card drop after user pile selection
		print((TextView) findViewById(R.id.tv_console), "Select a Card to drop.");
		userIsSelectingCard = true;
	}

	/**
	 * Human will drop user's selected Card and end turn
	 * @param card the Card to drop
	 */
	private void userSelectCard(Card card) {
		// drop the given card
		game.getHuman().discardCard(card, game.getRound().getDiscardPile());
		userIsSelectingCard = false;

		// user can attempt to go out after dropping card
		final Button goOutBtn = findViewById(R.id.b_goOut);
		goOutBtn.setEnabled(true);

		// end of Human's movement
		game.getRound().nextPlayer();
		findViewById(R.id.b_move).setEnabled(true);
		updateBoard();

		// start new round after last move of current round
		if (lastMove) {
			// last move has been made
//			goOut(0);
			findViewById(R.id.b_nextRound).setEnabled(true);
			//findViewById(R.id.b_move).setEnabled(false);
			lastMove = false;
		}
	}

	/**
	 * Create a new Game and start CoinTossActivity.
	 */
	private void newGame() {
		// call coin toss
		Intent intent = new Intent(this, CoinTossActivity.class);
		// todo should requestCodes be hardcoded?
		startActivityForResult(intent, 1);
		// continues in onActivityResult() request code 1

		findViewById(R.id.b_help).setEnabled(false);
	}

	/**
	 * Start SerializeActivity.
	 */
	private void serialize() {
		// create intent and start serialize activity
		Intent intent = new Intent(getApplicationContext(), SerializeActivity.class);
		if (game != null) {
			intent.putExtra("currentGameStr", game.getRound().toString());
		}
		else {
			intent.putExtra("currentGameStr", "Start a game to load stats.");
		}

		// continues in onActivityResult()
		startActivityForResult(intent, 2);
	}

	/**
	 * Print a helpful message to the console.
	 */
	private void help() {
		// help selecting which pile to draw
		if (userIsSelectingPile) {
			print((TextView) findViewById(R.id.tv_console), game.getHuman().offerHelpDraw(game.getRound().getDrawPile(), game.getRound().getDiscardPile()));
			return;
		}

		// help selecting which card to draw
		if (userIsSelectingCard) {
			print((TextView) findViewById(R.id.tv_console), game.getHuman().offerHelpDiscard());
			return;
		}

		// tell user if they should go out
		print((TextView) findViewById(R.id.tv_console), "You should " + (game.getHuman().canGoOut() ? "" : "not ")+ "go out.");
	}

	/**
	 * make a player pick a card, drop a card, and assemble
	 * @return
	 */
	private void move() {
		final Button moveBtn = findViewById(R.id.b_move);
		final Button goOutBtn = findViewById(R.id.b_goOut);
		final Button helpBtn = findViewById(R.id.b_help);

		// disable move button while player takes their turn
		moveBtn.setEnabled(false);
		helpBtn.setEnabled(false);
		goOutBtn.setEnabled(false);
		updateBoard();

		/*
		 if (humanNext)
			disable all but piles
			- ask user to pile draw from
			disable all but hand + userSelectedPile
			- ask user which card to drop
			set userSelectedPile and userSelectedCard in game.getHuman()
		 game.play()
		 */
		if (game.getRound().getNextPlayerIndex() == 0) {
			// Human's turn
			print(findViewById(R.id.tv_console), "Select a Pile to draw from.");

			// set flag to enable pile onClick listeners
			userIsSelectingPile = true;
			helpBtn.setEnabled(true);
		}
		else {
			// Computer's turn
			boolean out = game.playComputer();
			print(findViewById(R.id.tv_console), "CPU: " + game.getComputer().getLastMoveDesc());

			if (!lastMove) {
				// check if computer went out
				if (out) {
					goOut(1);
				}
			}

			// end of computer's turn
			game.getRound().nextPlayer();
			moveBtn.setEnabled(true);
		}

		// TODO this does't make sense here
		if (lastMove) {
			findViewById(R.id.b_nextRound).setEnabled(true);
		}

		// update UI
		updateBoard();
	}

	/**
	 * See if user can go out
	 */
	private void userGoOut() {

		// only the user can call this function, it's always's their turn

		// user attempts to drop all cards
		if (game.getHuman().canGoOut()) {
			game.getHuman().goOut();
			goOut(0);
		}
		else {
			print(findViewById(R.id.tv_console), "Sorry, you cannot go out.");
		}

		// disable help and go out after user tries to go out
		findViewById(R.id.b_goOut).setEnabled(false);
		findViewById(R.id.b_help).setEnabled(false);
	}

	private void goOut(int winnerIdx) {
		// print round winner
		if (winnerIdx == 0) {
			print(findViewById(R.id.tv_console), "\nCongrats, you went out! You won the round.");
			print(findViewById(R.id.tv_console), "Your assemblies: " + game.getHuman().getAssemblies().toString());
		}
		else {
			print(findViewById(R.id.tv_console), "\nThe computer went out. You lost the round");
			print(findViewById(R.id.tv_console), "Computer's assemblies: " + game.getComputer().getAssemblies().toString());
		}

		// offer one last move
		print(findViewById(R.id.tv_console), "One last move is offered.");

		// change the player
		game.nextPlayer();
		lastMove = true;
		move();

		// TODO move this code elsewhere
//		// print loser's hands and assemblies
//		if (winnerIdx == 0) {
//			print(findViewById(R.id.tv_console), "Computer's assemblies: " + game.getComputer().getAssemblies().toString());
//			print(findViewById(R.id.tv_console), "Computer's remaining cards: " + game.getComputer().getRemainingCards());
//			print(findViewById(R.id.tv_console), "The computer earned " + game.getComputer().getPointsInHand() + " points.");
//			game.getComputer().addToScore(game.getComputer().getPointsInHand());
//		}
//		else {
//			print(findViewById(R.id.tv_console), "Your assemblies: " + game.getHuman().getAssemblies().toString());
//			print(findViewById(R.id.tv_console), "Your remaining cards: " + game.getHuman().getRemainingCards());
//			print(findViewById(R.id.tv_console), "You earned " + game.getHuman().getPointsInHand() + " points.");
//			game.getHuman().addToScore(game.getHuman().getPointsInHand());
//		}
	}

	/**
	 * Start the next round.
	 * todo score the players, print assemblies to the screen
	 * todo this should process the end of game
	 */
	private void nextRound() {
		if (game != null) {
			game.nextRound();
			if (game.getRound().getRoundNum() < 13) {
				print(findViewById(R.id.tv_console), "\nStarting new round...");
			}
			else {
				print(findViewById(R.id.tv_console), "This is the last round!");
				findViewById(R.id.b_nextRound).setEnabled(false);
			}
			findViewById(R.id.b_move).setEnabled(true);
			lastMove = false;
			updateBoard();
		}
	}

	/**
	 * Process data from finished intents
	 * @param requestCode the identifying intent code
	 * @param resultCode the status of the intent
	 * @param data the resulting data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// coinToss intent
		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {

				print(findViewById(R.id.tv_console), "\nStarting new game...");
				game = new Game();

				// set nextPlayerIndex based on the user's prediction from CoinTossActivity
				boolean result = data.getBooleanExtra("result", true);
				final Button helpBtn = findViewById(R.id.b_help);
				final TextView console = findViewById(R.id.tv_console);

				if (result) {
					// human goes first
					print(console, "You won the coin toss!");
					game.getRound().setNextPlayerIndex(0);
					helpBtn.setEnabled(true);
				} else {
					// computer goes first
					print(console,"You lost the coin toss.");
					game.getRound().setNextPlayerIndex(1);
					helpBtn.setEnabled(false);
				}

				// enable Move Help and Go Out buttons
				final Button moveBtn = findViewById(R.id.b_move);
				moveBtn.setEnabled(true);

			}
			if (resultCode == Activity.RESULT_CANCELED) {
				print((TextView) findViewById(R.id.tv_console), "The coin toss was canceled.");
			}
		}

		// serialize intent
		if (requestCode == 2) {
			// print serial case returned
			if (resultCode == Activity.RESULT_OK) {
				// get game string from serialization
				String gameStr = data.getStringExtra("loadedGameStr");
				if (gameStr == null) {
					print(findViewById(R.id.tv_console), "String is null");
				}
				else {
					// print file title
					String name = data.getStringExtra("loadedGameName");
					print(findViewById(R.id.tv_console), "\nLoading " + name);

					// load returned game
					game = SerializeActivity.stringToGame(gameStr);

					updateButtons();
					updateBoard();
				}
			}

			// print when serialization does not return
			if (resultCode == Activity.RESULT_CANCELED) {
				print(findViewById(R.id.tv_console), "Serialization was cancelled.");
			}

		}

		updateBoard();
		scrollCardsLeft();
	}

	public void updateButtons() {
		if (game != null) {
			// player can always move
			findViewById(R.id.b_move).setEnabled(true);

			// human's turn
			if (game.getRound().getNextPlayerIndex() == 0) {
				findViewById(R.id.b_help).setEnabled(true);

			}
		}
	}

	/**
	 * Update the UI
	 */
	public void updateBoard() {
		if (game != null) {

			// update round number
			final TextView roundNumber = findViewById(R.id.tv_roundNumber);
			final int roundNum = game.getRound().getRoundNum();
			roundNumber.setText("Round: " + roundNum);

			// update next player
			final TextView nextPlayer = findViewById(R.id.tv_nextPlayer);
			String nextPlayerStr;
			if (game == null) {
				nextPlayerStr = "TBD";
			}
			else {
				nextPlayerStr = ((game.getRound().getNextPlayerIndex() == 0) ? "Human" : "Computer");
			}
			nextPlayer.setText("Next Player: " + nextPlayerStr);

			// update Draw Pile image
			final ImageButton drawPileBtn = findViewById(R.id.ib_draw);
			if (game.getRound().getDrawPile().size() != 0) {
				updateImageButton(drawPileBtn, game.getRound().getDrawPile().firstElement());
			} else {
				// pass blank card when pile is empty
				updateImageButton(drawPileBtn, new Card('0', '0'));
			}

			// update Discard Pile image
			final ImageButton discardPileBtn = findViewById(R.id.ib_discard);
			if (game.getRound().getDiscardPile().size() != 0) {
				updateImageButton(discardPileBtn, game.getRound().getDiscardPile().firstElement());
			} else {
				// pass blank card when pile is empty
				updateImageButton(discardPileBtn, new Card('0', '0'));
			}

			// update Human's hand
			final LinearLayout humanLayout = findViewById(R.id.ll_human);
			final Vector<Card> humanHand = game.getHuman().getHand();
			updateHand(humanLayout, humanHand);

			// update Computer's hand
			final LinearLayout computerLayout = findViewById(R.id.ll_computer);
			final Vector<Card> computerHand = game.getComputer().getHand();
			updateHand(computerLayout, computerHand);

			// update Player's scores
			TextView humanScore = findViewById(R.id.tv_humanScore);
			int hScore= game.getHuman().getScore();
			humanScore.setText(Integer.toString(hScore));
			int cScore= game.getComputer().getScore();
			TextView computerScore = findViewById(R.id.tv_computerScore);
			computerScore.setText(Integer.toString(cScore));
		}
	}

	/**
	 * Update the image on an ImageButton
	 *
	 * @param ib the button to update
	 * @param c the card to set as the image
	 */
	private void updateImageButton(ImageButton ib, Card c) {
		// todo jokers 2-3 show as joker 1
		int resID = getResources().getIdentifier("card_" + c.toString().toLowerCase(), "drawable", getPackageName());

		if (resID != 0) {
			ib.setImageResource(resID);
		} else {
			// display empty card
			ib.setImageResource(R.drawable.card_back4);
		}
	}

	/**
	 * Update the cards in a players players hand
	 *
	 * @param playerLayout the player to update
	 * @param hand         the cards to show
	 */
	private void updateHand(LinearLayout playerLayout, Vector<Card> hand) {
		// human cards have static ImageButtons with dedicated onClick listeners
		if (playerLayout.getId() == R.id.ll_human) {

			// update each ib_humanCard[index] with the appropriate image resource
			// todo remove hardcoded 13 - set to max_cards
			for (int i = 0; i < 13; i++) {
				int resID = getResources().getIdentifier("ib_humanCard" + i, "id", getPackageName());
				ImageButton cardIB = findViewById(resID);

				// cards in hand get images, remaining cards are invisible
				if (i < hand.size()) {
					cardIB.setBackgroundColor(Color.TRANSPARENT);
					cardIB.setVisibility(View.VISIBLE);
					cardIB.setOnClickListener(this);
					updateImageButton(cardIB, hand.elementAt(i));
				}
				else {
					cardIB.setVisibility(View.INVISIBLE);
				}
			}
		}
		// computer cards are dynamically updated
		else {
			// clear previous cards in hand
			playerLayout.removeAllViews();

			// dynamically add a new ImageButtons for each card
			for (int i = 0; i < hand.size(); i++) {
				// create new ImageButton with transparent background and card image
				ImageButton newBtn = new ImageButton(getApplicationContext());
				newBtn.setBackgroundColor(Color.TRANSPARENT);

				// add click listener for human
				if (playerLayout.getId() == R.id.ll_human) {
					newBtn.setId(View.generateViewId());
					newBtn.setOnClickListener(this);
				}

				updateImageButton(newBtn, hand.elementAt(i));

				// add new 'card' ImageButton to layout
				playerLayout.addView(newBtn);
			}
		}
	}

	/**
	 * Scroll hands to the left
	 */
	private void scrollCardsLeft() {
		final HorizontalScrollView humanScroll = findViewById(R.id.hsv_human);
		humanScroll.scrollTo(0, 0);
		final HorizontalScrollView computerScroll = findViewById(R.id.hsv_computer);
		computerScroll.scrollTo(0,0);
	}

	/**
	 * Append text to the console and automatically scroll down
	 * @param text the text to append to the console
	 */
	private void print(TextView console, String text) {
		console.append(text + "\n");
		// todo remove hardcoded line count

		//  something like final float linesInConsole = console.getPaint().getFontMetrics().bottom - console.getPaint().getFontMetrics().top;
		if (console.getLineCount() > 11) {
			console.setGravity(Gravity.BOTTOM);
		}
	}

}