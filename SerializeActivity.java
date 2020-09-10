package com.example.a5crowns;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;


public class SerializeActivity extends Activity implements View.OnClickListener {

    /** current game */
    Game currentGame;

//    /** current game stats */
//    String gameStats;

    /** save file filename*/
    String filename;

    /** list of saved games */
    Vector<Game> savedGames;

    /** list of saved files */
    Vector<File> savedFiles;

    /** list of buttons */
    Vector<Button> savedGameBtns;

    /** user selected game*/
    int selectedGameIdx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialize);
        // create and store list of saved games
        savedGames = new Vector();
        savedGameBtns = new Vector<>();
        savedFiles = new Vector<>();

        // TODO delete is not yet implemented
        findViewById(R.id.b_delete).setEnabled(false);

        // get current game from intent
        Intent intent = getIntent();
        currentGame = stringToGame(intent.getStringExtra("currentGameStr"));

        // only enable save if a game was passed
        if (currentGame.getRound().getRoundNum() == 0) {
            findViewById(R.id.b_save).setEnabled(false);
        }

        populateSavedGames();

        // set onClick listeners to all buttons
        findViewById(R.id.b_save).setOnClickListener(this);
        findViewById(R.id.b_back).setOnClickListener(this);
        findViewById(R.id.b_delete).setOnClickListener(this);
        findViewById(R.id.b_load).setOnClickListener(this);
        findViewById(R.id.b_currentGame).setOnClickListener(this);
    }

    /**
     * onClick listener - Map all buttons to their appropriate methods
     * @param v the button clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_save:
                enterFilenamePopup();
                break;
            case R.id.b_back:
                // return to main activity
                finish();
                break;
            case R.id.b_delete:
                delete();
                break;
            case R.id.b_load:
                // TODO
                load();
                break;
            case R.id.b_currentGame:
                selectGame(-1);
                break;
            default:
                // check if view is a button
                if (v instanceof Button) {
                    // get button index in layout
                    LinearLayout gamesScroll = findViewById(R.id.ll_savedGames);
                    selectGame(gamesScroll.indexOfChild(v));
                }
                break;
        }
    }

    /**
     * populate the list of games with files from internal storage
     */
    private void populateSavedGames() {
        // create file and folder at app-specific location
        File file = new File("/data/data/com.example.a5crowns/files");
        File[] list = file.listFiles();

        // clear all buttons and saved games
        LinearLayout gamesScroll = findViewById(R.id.ll_savedGames);
        gamesScroll.removeAllViews();
        savedGames.clear();
        savedFiles.clear();

        // go through all files in directory and populate saved games
        for (int i = 0; i < list.length; i++) {
            // get name from file
            String name = list[i].getName();

            // overwrite serial1.txt in internal storage (hardcode serialization cases from computer)
            {
//                if(name.equals("serial1.txt")) {
//                    FileOutputStream fOut = null;
//                    try {
//                        fOut = new FileOutputStream(list[i]);
//                        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
//
//                        String res = "Round: 2\n" +
//                                "Computer:\n" +
//                                "   Score: 6\n" +
//                                "   Hand: 9C 5C 9D 9H\n" +
//                                "Human:\n" +
//                                "   Score: 0\n" +
//                                "   Hand: 5T 8D 7T 8T\n" +
//                                "Draw Pile:  4H 4C 3S 5S 5H 6H 8H 7H XC J2 XD QD KD KS XS 9T QT JT 5D QS 7C 6C 8C KH QH XH JC KC QC JH 4T 3T 3D 4D 4S 6T 6S 5S 4S 3S 7S J1 3C 5C 6C 7C 9H JH QH KH 4T 6T J3 QS XS 9S 8C 4C 9C QC KC JC XC 8S JS KS J2 6H 3H 4H 5H XH 8H 7H XD QD KD 6D 5D 3D 4D 7D JD 9D 8D 8T 5T 3T 9T XT QT KT JT 7T  \n" +
//                                "Discard Pile: JD J1 J3 KT XT 3C 6S 7S 3H JS 7D 6D 8S 9S\n" +
//                                "Next Player: Computer";
//
//                        myOutWriter.write(res);
//                        myOutWriter.close();
//                        fOut.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }

            // create buttons for text files only
            if (name.endsWith(".txt")) {
                // add file to list
                savedFiles.add(list[i]);

                // add a button for each file to load
                Button gameBtn = new Button(getApplicationContext());
                // set title to filename without ".txt"
                gameBtn.setText(name.substring(0, name.length() - 4));
                gameBtn.setOnClickListener(this);

                // create Game from file contents
                Game game = getGameFromFile(list[i]);
                savedGames.add(game);

                // add button to view and vector
                gamesScroll.addView(gameBtn);
                savedGameBtns.add(gameBtn);
            }
        }
    }

    /**
     * Set the selected game index to the given view (saved game button)
     * @param gameIdx the user selected game index
     */
    private void selectGame(int gameIdx) {
        // show the selected game stats
        TextView text = findViewById(R.id.tv_stats);

        // see if current game or loaded game is clicked
        if (gameIdx == -1) {
            // see if game is started
            if (currentGame == null) {
                text.setText("Start a new game or click a saved game to see the stats here.");
            }
            else {
                // show current game
                text.setText(currentGame.toString());
            }

            // only enable save if a game was passed
            if (currentGame.getRound().getRoundNum() == 0) {
                findViewById(R.id.b_save).setEnabled(false);
            }
            else {
                findViewById(R.id.b_save).setEnabled(true);
            }

            // disable load and delete
            findViewById(R.id.b_load).setEnabled(false);
            findViewById(R.id.b_delete).setEnabled(false);

        }
        else {
            // show saved game
            // set selected game to index of button in list
            selectedGameIdx = gameIdx;
            text.setText(savedGames.get(selectedGameIdx).toString());

            // disable save
            findViewById(R.id.b_save).setEnabled(false);

            // enable load and delete
            findViewById(R.id.b_load).setEnabled(true);
            findViewById(R.id.b_delete).setEnabled(true);
        }

    }

    private void load() {
        Game game = savedGames.get(selectedGameIdx);
        Intent returnIntent = new Intent();

        // add file title and game as string to the intent
        returnIntent.putExtra("loadedGameStr", game.toString());
        returnIntent.putExtra("loadedGameName", savedGameBtns.get(selectedGameIdx).getText());

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


    /**
     * delete the selected game
     */
    private void delete() {
        // delete selected game
        boolean deleted = savedFiles.get(selectedGameIdx).delete();

        // tell user if the game was deleted
        if (deleted) {
            Toast.makeText(getBaseContext(), "Game deleted", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getBaseContext(), "Game NOT deleted", Toast.LENGTH_SHORT).show();
        }

        // load games again
        populateSavedGames();
    }

    /**
     * Create Game from file contents string
     * https://stackoverflow.com/questions/24291721/reading-a-text-file-line-by-line-in-android
     * */
    private static Game getGameFromFile(File file) {
        // game stats to save
        int roundNum = 0;
        int computerScore = 0;
        Vector<Card> computerHand = null;
        int humanScore = 0;
        Vector<Card> humanHand = null;
        Vector<Card> drawPile = null;
        Vector<Card> discardPile = null;
        int nextPlayerIdx = 0;

        FileInputStream is;
        BufferedReader reader;

        try {
            if (file.exists()) {
                is = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();

                while(line != null) {

                    /** line.indexOf(str) needs to be offset by L where L = str.length() */

                    if (line.contains("Round:")) {
                        // get round number
                        String roundStr = line.substring(line.indexOf("Round: ") + 7);
                        roundNum = Integer.parseInt(roundStr);

                        // does not work for 10, 11
                        //roundNum = Character.getNumericValue(line.charAt(line.indexOf("Round: ") + 7));

//                        Toast.makeText(getBaseContext(), "Round: " + roundNum, Toast.LENGTH_SHORT).show();
                    }

                    if (line.contains("Computer:")) {
                        while (line != null) {
                            line = reader.readLine();

                            if (line.contains("Score: ")) {
                                computerScore = Integer.parseInt(line.substring(line.indexOf("Score: ") + 7));
//                                Toast.makeText(getBaseContext(), "Computer Score: " + computerScore, Toast.LENGTH_SHORT).show();
                            }

                            if (line.contains("Hand: ")) {
                                String computerStr = line.substring(line.indexOf("Hand: ") + 6);
                                computerHand = stringToHand(computerStr, roundNum + 2);
//                                Toast.makeText(getBaseContext(), "Computer hand: " + computerHand.toString(), Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }

                    if (line.contains("Human:")) {
                        while (line != null) {
                            line = reader.readLine();

                            if (line.contains("Score: ")) {
                                humanScore = Integer.parseInt(line.substring(line.indexOf("Score: ") + 7));
//                                Toast.makeText(getBaseContext(), "Human Score: " + humanScore, Toast.LENGTH_SHORT).show();
                            }
                            if (line.contains("Hand: ")) {
                                String humanStr = line.substring(line.indexOf("Hand: ") + 6);
                                humanHand = stringToHand(humanStr, roundNum + 2);
//                                Toast.makeText(getBaseContext(), "Human hand: " + humanHand .toString(), Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }

                    if (line.contains("Draw Pile: ")) {
                        String drawStr = line.substring(line.indexOf("Draw Pile: ") + 11);
                        drawPile = stringToHand(drawStr, roundNum + 2);
//                        Toast.makeText(getBaseContext(), "Draw Pile: " + drawPile .toString(), Toast.LENGTH_SHORT).show();
                    }

                    if (line.contains("Discard Pile: ")) {
                        String discardStr = line.substring(line.indexOf("Discard Pile: ") + 14);
                        discardPile = stringToHand(discardStr, roundNum + 2);
//                        Toast.makeText(getBaseContext(), "Discard Pile: " + discardPile .toString(), Toast.LENGTH_SHORT).show();
                    }

                    if (line.contains("Next Player:")) {
                        if (line.contains("Human")) {
                            nextPlayerIdx = 0;
                        }
                        else {
                            nextPlayerIdx = 1;
                        }
//                        Toast.makeText(getBaseContext(), "Next Player Idx: " + nextPlayerIdx, Toast.LENGTH_SHORT).show();

                    }

                    line = reader.readLine();
                }
            }

        } catch (Exception e) {
//            Toast.makeText(getBaseContext(), "ERROR: " + e, Toast.LENGTH_SHORT).show();
            return new Game();
        }

        Human human = new Human(humanScore, humanHand);
        Computer computer = new Computer(computerScore, computerHand);
        Round round = new Round(roundNum, human, computer, nextPlayerIdx, drawPile, discardPile);
        Game game = new Game(human, computer, round);

//        if (game == null) {
//            Toast.makeText(getBaseContext(), "Game is null :(", Toast.LENGTH_SHORT).show();
//            game = new Game();
//        }
//
//        Toast.makeText(getBaseContext(), "Game: " + game, Toast.LENGTH_SHORT).show();

        return game;
    }

    /**
     * Parse a given serialization string into a vector of cards
     * @param str the cards as string (e.g. "5H 6H 7H 9D")
     * @param wildcard the round's current wildcard
     * @return a vector of Cards represented by the given string
     */
    private static Vector<Card> stringToHand(String str, int wildcard) {

        // return variable
        Vector<Card> hand = new Vector<>();

        // trim input
        str = str.trim();

        // parse string to Cards and add to vector
        int tempFace;
        char tempSuit;
        boolean tempWild = false;
        for (int i = 0; i < str.length(); i += 3) {
            switch (str.charAt(i)) {
                case 'X':
                    tempFace = 10;
                    break;
                case 'J':
                    tempFace = 11;
                    break;
                case 'Q':
                    tempFace = 12;
                    break;
                case 'K':
                    tempFace = 13;
                    break;
                case ' ':
                    continue;
                default:
                    tempFace = Character.getNumericValue(str.charAt(i));
            }

            if (tempFace == wildcard) {
                tempWild = true;
            }
            tempSuit = str.charAt(i + 1);
            if (tempFace != ' ') {
                hand.add(new Card(tempSuit, tempFace, tempWild));
            }
        }

        // return Card list
        return hand;
    }

    /**
     * Parse a given string to a Game
     * @param string the serialized content of the game (Kumar's serialization file contents)
     * @return the Game with the given contents
     */
    public static Game stringToGame(String string) {
        // game stats to save
        int roundNum = 0;
        int computerScore = 0;
        Vector<Card> computerHand = new Vector<>();
        int humanScore = 0;
        Vector<Card> humanHand = new Vector<>();
        Vector<Card> drawPile = new Vector<>();
        Vector<Card> discardPile = new Vector<>();
        int nextPlayerIdx = 0;

        // loop through each line of the given string
        String gameStr[] = string.split("\n");
        for (int lineIdx = 0; lineIdx < gameStr.length; lineIdx++) {
            String line = gameStr[lineIdx];

            /** line.indexOf(str) needs to be offset by L where L = str.length() */

            // get round
            if (line.contains("Round:")) {
                // get round number
                String roundStr = line.substring(line.indexOf("Round: ") + 7).trim();
                roundNum = Integer.parseInt(roundStr);
//                        Toast.makeText(getBaseContext(), "Round: " + roundNum, Toast.LENGTH_SHORT).show();
            }

            // get computer
            if (line.contains("Computer:")) {
                // loop through lines after current location
                for (int i = lineIdx; i < gameStr.length; i++) {
                    line = gameStr[i];

                    // get computer score
                    if (line.contains("Score: ")) {
                        computerScore = Integer.parseInt(line.substring(line.indexOf("Score: ") + 7).trim());
//                                Toast.makeText(getBaseContext(), "Computer Score: " + computerScore, Toast.LENGTH_SHORT).show();
                    }

                    // get computer hand
                    if (line.contains("Hand: ")) {
                        String computerStr = line.substring(line.indexOf("Hand: ") + 6).trim();
                        computerHand = stringToHand(computerStr, roundNum + 2);
//                                Toast.makeText(getBaseContext(), "Computer hand: " + computerHand.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            // get human
            if (line.contains("Human:")) {
                // loop through lines after current location
                for (int i = lineIdx; i < gameStr.length; i++) {
                    line = gameStr[i];

                    // get human score
                    if (line.contains("Score: ")) {
                        humanScore = Integer.parseInt(line.substring(line.indexOf("Score: ") + 7).trim());
//                                Toast.makeText(getBaseContext(), "Human Score: " + humanScore, Toast.LENGTH_SHORT).show();
                    }
                    // get human hand
                    if (line.contains("Hand: ")) {
                        String humanStr = line.substring(line.indexOf("Hand: ") + 6).trim();
                        humanHand = stringToHand(humanStr, roundNum + 2);
//                                Toast.makeText(getBaseContext(), "Human hand: " + humanHand .toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            // get draw pile
            if (line.contains("Draw Pile: ")) {
                String drawStr = line.substring(line.indexOf("Draw Pile: ") + 11).trim();
                drawPile = stringToHand(drawStr, roundNum + 2);
//                        Toast.makeText(getBaseContext(), "Draw Pile: " + drawPile .toString(), Toast.LENGTH_SHORT).show();
            }

            // get discard pile
            if (line.contains("Discard Pile: ")) {
                String discardStr = line.substring(line.indexOf("Discard Pile: ") + 14).trim();
                discardPile = stringToHand(discardStr, roundNum + 2);
//                System.out.println("DISCARD_STR: " + discardStr);
//                System.out.println("DISCARD_PILE: " + discardPile.toString());
//                        Toast.makeText(getBaseContext(), "Discard Pile: " + discardPile .toString(), Toast.LENGTH_SHORT).show();
            }

            if (line.contains("Next Player:")) {
                if (line.contains("Human")) {
                    nextPlayerIdx = 0;
                }
                else {
                    nextPlayerIdx = 1;
                }
//                        Toast.makeText(getBaseContext(), "Next Player Idx: " + nextPlayerIdx, Toast.LENGTH_SHORT).show();

            }
        }

        Human human = new Human(humanScore, humanHand);
        Computer computer = new Computer(computerScore, computerHand);
        Round round = new Round(roundNum, human, computer, nextPlayerIdx, drawPile, discardPile);
        return new Game(human, computer, round);
    }

    /**
     * save gameStats to file
     * https://stackoverflow.com/questions/51565897/saving-files-in-android-for-beginners-internal-external-storage
     */
    private void save() {
        // save a file to storage
        try {

            File myFile = new File(getFilesDir(), filename + ".txt");
            myFile.createNewFile();

            // open streams
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            // write game stats to file
//            myOutWriter.append(gameStats);
            myOutWriter.append(currentGame.toString());

            // close streams
            myOutWriter.close();
            fOut.close();

            // toast user to let them know it's done saving
            Toast.makeText(getBaseContext(),"Done saving " + filename, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // report error if file is not saved
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // update saved game list
        populateSavedGames();
    }

    /**
     * Popup asking user to enter a filename
     * @help from S/O
     * TODO needs input validation
     */
    private void enterFilenamePopup() {
        // set up title
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a filename");

        // set title
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // set Save button
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filename = input.getText().toString();
                save();
            }
        });
        // set Cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // show popup
        builder.show();
    }
}
