package com.test.nasatest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String NORTH = "N";
    private static final String EAST = "E";
    private static final String SOUTH = "S";
    private static final String WEST = "W";

    private static final String LEFT = "L";
    private static final String MOVE = "M";
    private static final String RIGHT = "R";

    private LinearLayout firstLayout, secondLayout, thirdLayout;
    private TextView inputTextView, outputTextView;
    private EditText upperRightEditText, startXEditText, startYEditText;
    private RadioButton northRadio, eastRadio, southRadio, westRadio;

    private int maxX = -1, maxY = -1, currentX = -1, currentY = -1;
    private String currentOrientation = "", input = "", output = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstLayout = findViewById(R.id.firstLayout);
        secondLayout = findViewById(R.id.secondLayout);
        thirdLayout = findViewById(R.id.thirdLayout);

        inputTextView = findViewById(R.id.inputTextView);
        outputTextView = findViewById(R.id.outputTextView);

        upperRightEditText = findViewById(R.id.upperRightNumberEditText);

        startXEditText = findViewById(R.id.startXEditText);
        startYEditText = findViewById(R.id.startYEditText);

        northRadio = findViewById(R.id.startNorthRadio);
        eastRadio = findViewById(R.id.startEastRadio);
        southRadio = findViewById(R.id.startSouthRadio);
        westRadio = findViewById(R.id.startWestRadio);

    }

    public void resetClicked(View view) {
        maxX = -1;
        maxY = -1;
        currentX = -1;
        currentY = -1;
        currentOrientation = "";
        input = "";
        inputTextView.setText(input);
        output = "";
        outputTextView.setText(output);

        firstLayout.setVisibility(View.VISIBLE);
        secondLayout.setVisibility(View.GONE);
        thirdLayout.setVisibility(View.GONE);
    }

    public void firstOKClicked(View view) {
        hideKeyboard(this);

        if (upperRightEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Add upper right number.", Toast.LENGTH_SHORT).show();
            return;
        }
        int maxXY = Integer.valueOf(upperRightEditText.getText().toString());
        upperRightEditText.setText(null);
        maxX = maxXY;
        maxY = maxXY;
        input = maxX + " " + maxY + "\n";
        inputTextView.setText(input);
        firstLayout.setVisibility(View.GONE);
        secondLayout.setVisibility(View.VISIBLE);
    }

    public void secondOKClicked(View view) {
        hideKeyboard(this);

        if (startXEditText.getText().toString().isEmpty() || startYEditText.getText().toString().isEmpty() || (!northRadio.isChecked() && !eastRadio.isChecked() && !southRadio.isChecked() && !westRadio.isChecked())) {
            Toast.makeText(this, "Add starting X, Y and orientation.", Toast.LENGTH_SHORT).show();
            return;
        }

        currentX = Integer.valueOf(startXEditText.getText().toString());
        currentY = Integer.valueOf(startYEditText.getText().toString());

        if (currentX > maxX) {
            Toast.makeText(this, "X must be less than upper right X", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentY > maxY) {
            Toast.makeText(this, "Y must be less than upper right Y", Toast.LENGTH_SHORT).show();
            return;
        }

        startXEditText.setText(null);
        startYEditText.setText(null);

        if (northRadio.isChecked()) {
            currentOrientation = NORTH;
        } else if (eastRadio.isChecked()) {
            currentOrientation = EAST;
        } else if (southRadio.isChecked()) {
            currentOrientation = SOUTH;
        } else if (westRadio.isChecked()) {
            currentOrientation = WEST;
        }

        input = input + currentX + " " + currentY + " " + currentOrientation + "\n";
        inputTextView.setText(input);

        secondLayout.setVisibility(View.GONE);
        thirdLayout.setVisibility(View.VISIBLE);
    }

    public void leftClicked(View view) {
        input = input + LEFT;
        inputTextView.setText(input);

        switch (currentOrientation) {
            case NORTH:
                currentOrientation = WEST;
                break;
            case WEST:
                currentOrientation = SOUTH;
                break;
            case SOUTH:
                currentOrientation = EAST;
                break;
            case EAST:
                currentOrientation = NORTH;
                break;
        }
    }

    public void moveClicked(View view) {
        switch (currentOrientation) {
            case NORTH:
                if (currentY >= maxY) {
                    Toast.makeText(this, "Out of bounds!", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentY++;
                break;
            case EAST:
                if (currentX >= maxX) {
                    Toast.makeText(this, "Out of bounds!", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentX++;
                break;
            case SOUTH:
                if (currentY <= 0) {
                    Toast.makeText(this, "Out of bounds!", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentY--;
                break;
            case WEST:
                if (currentX <= 0) {
                    Toast.makeText(this, "Out of bounds!", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentX--;
                break;
        }

        input = input + MOVE;
        inputTextView.setText(input);
    }

    public void rightClicked(View view) {
        input = input + RIGHT;
        inputTextView.setText(input);

        switch (currentOrientation) {
            case NORTH:
                currentOrientation = EAST;
                break;
            case EAST:
                currentOrientation = SOUTH;
                break;
            case SOUTH:
                currentOrientation = WEST;
                break;
            case WEST:
                currentOrientation = NORTH;
                break;
        }
    }

    public void thirdOKClicked(View view) {
        input = input + "\n";
        inputTextView.setText(input);

        output = output + currentX + " " + currentY + " " + currentOrientation + "\n";
        outputTextView.setText(output);

        secondLayout.setVisibility(View.VISIBLE);
        thirdLayout.setVisibility(View.GONE);
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
