package com.example.a5crowns;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CoinTossActivity extends AppCompatActivity {

    boolean heads;
    boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_toss);

        final Button headsBtn = findViewById(R.id.b_heads);
        final Button tailsBtn = findViewById(R.id.b_tails);
        final Button startBtn = findViewById(R.id.b_startGame);
        final TextView resultTV = findViewById(R.id.tv_result);

        // disable the Start Game button
        startBtn.setEnabled(false);

        // randomly determine 'heads' (0) or 'tails' (1)
        if (Math.random() < 0.5) {
            heads = true;
        }
        else {
            heads = false;
        }

        /** heads button onClick listener */
        headsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (heads) {
                    resultTV.setText("You called it! You will go first.");
                    result = true;
                }
                else {
                    resultTV.setText("Sorry! You will go second.");
                    result = false;
                }

                toggleButtonsEnabled();
            }

        });

        /** tails button onClick listener */
        tailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!heads) {
                    resultTV.setText("You called it! You will go first.");
                    result = true;
                }
                else {
                    resultTV.setText("Sorry! You will go second.");
                    result = false;
                }
                toggleButtonsEnabled();
            }
        });

        /** Start Game button onClick listener */
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            /** finish activity and return heads */
            public void onClick(View view) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", result);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            }
        });



    }

        /** disable the Heads/Tails buttons and enable Start Game */
        private void toggleButtonsEnabled() {
            final Button headsBtn = findViewById(R.id.b_heads);
            final Button tailsBtn = findViewById(R.id.b_tails);
            final Button startBtn = findViewById(R.id.b_startGame);

            headsBtn.setEnabled(false);
            tailsBtn.setEnabled(false);
            startBtn.setEnabled(true);
        }

}
