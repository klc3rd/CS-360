package com.zybooks.weighttracker;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.weighttracker.DAO.User;
import com.zybooks.weighttracker.DAO.Weight;
import com.zybooks.weighttracker.database.Database;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREF = "weightTrackerPrefs";

    WeightsRecyclerViewAdapter adapter;
    EditText enteredWeight;
    Button logBtn;
    EditText goalWeight;
    Database db;
    SharedPreferences sharedPref;

    ArrayList<Weight> weights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new Database(MainActivity.this);

        String username  = loadUsername();

        goalWeight = findViewById(R.id.goalWeight);
        enteredWeight = findViewById(R.id.enteredWeight);
        logBtn = findViewById(R.id.logBtn);


        updateGoal(username);

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

        logBtn.setOnClickListener(v -> {
            String emptyWeightMsg = getResources().getString(R.string.errorEmptyWeight);
            if (enteredWeight.getText().toString().length() == 0) {
                showMessage(emptyWeightMsg);
            } else {
                int logWeight = parseInt(enteredWeight.getText().toString());
                if (logWeight > 0) {
                    logWeight(username, logWeight);
                }
            }
        });

        getWeights(username);
    }

    @Override
    public void onResume() {
        super.onResume();
        String username = loadUsername();
        getWeights(username);
    }

    String loadUsername() {
        sharedPref = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String username  = sharedPref.getString("username", null);

        if (username == null) {
            return null;
        }

        return username;
    }

    private void updateGoal(String username) {
        User foundUser = db.getUserByUsername(username);
        String goal = String.valueOf(foundUser.getGoal());
        goalWeight.setText(goal);
    }

    private void showMessage(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logWeight(String username, int weight) {
        String weightLoggedMsg = getResources().getString(R.string.weightLogged);
        String logDate = new Date().toString();
        db.logWeight(username, logDate, weight);
        getWeights(username);
        showMessage(weightLoggedMsg);
    }

    public void getWeights(String username) {
        weights = db.getWeights(username);

        // Setup recyclerView
        RecyclerView recyclerView = findViewById(R.id.weightsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WeightsRecyclerViewAdapter(this, weights);
        recyclerView.setAdapter(adapter);
    }
}