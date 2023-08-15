package com.zybooks.weighttracker;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.zybooks.weighttracker.DAO.User;
import com.zybooks.weighttracker.database.Database;

public class SettingsActivity extends AppCompatActivity {

    Database db;
    SharedPreferences sharedPref;

    EditText phone;
    EditText goalWeight;
    SwitchCompat sendSMS;

    // Check if sendSMS switch is touched
    Boolean isTouched = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        goalWeight = findViewById(R.id.goal);
        phone = findViewById(R.id.phoneNumber);
        sendSMS = findViewById(R.id.sendSMSToggle);

        db = new Database(SettingsActivity.this);
        sharedPref = getSharedPreferences(MainActivity.SHARED_PREF, MODE_PRIVATE);
        String username  = sharedPref.getString("username", null);


        if (username == null) {
            return;
        }

        loadSettings(username);

        sendSMS.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouched = true;
                return false;
            }
        });

        sendSMS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isTouched) {
                    isTouched = false;
                    if(isChecked) {
                        // TODO: Update sendSMS settings
                    } else {
                        // TODO: Update sendSMS settings
                    }
                }
            }
        });

        // Update goal if text is changed
        goalWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 1) {
                    Log.d(TAG, "afterTextChanged: " + username);

                    String updatedMsg = getResources().getString(R.string.goalUpdated);

                    int newGoal = parseInt(editable.toString());
                    int rowsUpdated = db.updateGoals(username, newGoal);

                    if (rowsUpdated == 1) {
                        showMessage(updatedMsg);
                    }
                }
            }
        });
    }

    private void loadSettings(String username) {
        User foundUser = db.getUserByUsername(username);
        String goal = String.valueOf(foundUser.getGoal());
        goalWeight.setText(goal);
        phone.setText(foundUser.getPhone());
        int sendSMSSetting = foundUser.getSendSMS();
        if (sendSMSSetting == 1) {
            sendSMS.toggle();
        }
    }

    private void showMessage(String text) {
        Toast.makeText(SettingsActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}