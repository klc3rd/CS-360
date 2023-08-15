package com.zybooks.weighttracker;

import static android.content.ContentValues.TAG;
import static com.zybooks.weighttracker.MainActivity.SHARED_PREF;
import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.zybooks.weighttracker.database.Database;

public class EditWeight extends AppCompatActivity {

    public static final String WEIGHT_ID = "weightId";
    private long id;
    private EditText mWeight;
    private Button mUpdate;
    private Button mDelete;

    Database db;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_weight);

        mWeight = findViewById(R.id.weightField);
        mUpdate = findViewById(R.id.updateBtn);
        mDelete = findViewById(R.id.deleteBtn);

        Intent intent = getIntent();
        id = intent.getLongExtra(WEIGHT_ID, 0);

        db = new Database(EditWeight.this);

        sharedPref = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String username  = sharedPref.getString("username", null);

        if (username == null) {
            return;
        }

        mWeight.setText(String.valueOf(db.getWeight(id)));

        mDelete.setOnClickListener(v -> {
            int cols = db.deleteWeight(id);
            Log.d(TAG, "Deleted Rows: " + cols);
            finish();
        });

        mUpdate.setOnClickListener(v -> {
            int weight = parseInt(mWeight.getText().toString());
            db.updateWeight(id, weight);
            finish();
        });
    }
}