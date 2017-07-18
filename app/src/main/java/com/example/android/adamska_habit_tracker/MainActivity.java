package com.example.android.adamska_habit_tracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.adamska_habit_tracker.data.HabitDbHelper;
import com.example.android.adamska_habit_tracker.data.HabitContract.HabitEntry;

public class MainActivity extends AppCompatActivity {

    HabitDbHelper mDbHelper = new HabitDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Add_Button to open EditorActivity
        ImageView button = (ImageView) findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new HabitDbHelper(this);
        displayDatabaseInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * The method which allows us to display information in the main_activity about the state of
     * the habits database.
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT,
                HabitEntry.COLUMN_WEEK_DAY,
                HabitEntry.COLUMN_TIME,
                HabitEntry.COLUMN_REMARKS
        };

        Cursor cursor = db.query(
                HabitEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        TextView displayView = (TextView) findViewById(R.id.display_text);

        try {
            // Create a header in the Text View that looks like this:

            // _id - habit - week_day - time - additional_remarks
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText(HabitEntry._ID + " - " +
                    HabitEntry.COLUMN_HABIT + " - " +
                    HabitEntry.COLUMN_WEEK_DAY + " - " +
                    HabitEntry.COLUMN_TIME + " - " +
                    HabitEntry.COLUMN_REMARKS + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int habitColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT);
            int dayColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_WEEK_DAY);
            int timeColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_TIME);
            int remarksColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_REMARKS);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                int minutes = cursor.getInt(timeColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView using the indexes
                //to extract the String or Int value of the word
                displayView.append(("\n" + cursor.getInt(idColumnIndex) + " - " +
                        cursor.getString(habitColumnIndex) + " - " +
                        cursor.getInt(dayColumnIndex) + " - " +
                        (minutes / 60) + ":" + (minutes % 60) + " - " +
                        cursor.getString(remarksColumnIndex)));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }
}
