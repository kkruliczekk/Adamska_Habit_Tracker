package com.example.android.adamska_habit_tracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.adamska_habit_tracker.data.HabitContract.HabitEntry;
import com.example.android.adamska_habit_tracker.data.HabitDbHelper;

import static com.example.android.adamska_habit_tracker.R.id.timePicker;

/**
 * Allows user to create a new activity.
 */
public class EditorActivity extends AppCompatActivity {

    private EditText mHabitEditText;

    private Spinner mDaysSpinner;

    //private EditText mTimeEditText;

    private TimePicker mTimePicker;

    private EditText mRemarksEditText;

    /**
     * Day of the week. The possible values are:
     * 0 for everyday, 1 for monday, 2 for tuesday
     * 3 for wednesday, 4 for thursday, 5 for friday,
     * 6 for saturday, 7 for sunday.
     */
    private int mDay = HabitEntry.EVERYDAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mHabitEditText = (EditText) findViewById(R.id.habit_edit);
        mDaysSpinner = (Spinner) findViewById(R.id.spinner_day);
        //mTimeEditText = (EditText) findViewById(R.id.time_edit);
        mTimePicker = (TimePicker) findViewById(timePicker);
        mTimePicker.setIs24HourView(true);
        mRemarksEditText = (EditText) findViewById(R.id.additional_edit);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the day of the activity.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter daysSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_days_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        daysSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mDaysSpinner.setAdapter(daysSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mDaysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.monday))) {
                        mDay = HabitEntry.MONDAY;
                    } else if (selection.equals(getString(R.string.tuesday))) {
                        mDay = HabitEntry.TUESDAY;
                    } else if (selection.equals(getString(R.string.wednesday))) {
                        mDay = HabitEntry.WEDNESDAY;
                    } else if (selection.equals(getString(R.string.thursday))) {
                        mDay = HabitEntry.THURSDAY;
                    } else if (selection.equals(getString(R.string.friday))) {
                        mDay = HabitEntry.FRIDAY;
                    } else if (selection.equals(getString(R.string.saturday))) {
                        mDay = HabitEntry.SATURDAY;
                    } else if (selection.equals(getString(R.string.sunday))) {
                        mDay = HabitEntry.SUNDAY;
                    } else {
                        mDay = HabitEntry.EVERYDAY;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mDay = HabitEntry.EVERYDAY;
            }
        });
    }

    //get what the user input and save the new habit into the database
    private boolean insertHabit() {

        String habitString = mHabitEditText.getText().toString().trim();
        //String timeString = mTimeEditText.getText().toString().trim();
        int hour = mTimePicker.getCurrentHour();
        int min = mTimePicker.getCurrentMinute();
        String remarksString = mRemarksEditText.getText().toString().trim();

        //Check if the User inserts required field - habit
        //if not - display message
        if (habitString.isEmpty()) {
             Toast.makeText(this, "Field 'Habit' can not be empty", Toast.LENGTH_SHORT).show();

            //if required field is not empty - add the data to database
        } else {
            //save the time as amount of minutes from midnight
            int time = hour * 60 + min;

            HabitDbHelper mDbHelper = new HabitDbHelper(this);

            // Gets the database in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            // Create a ContentValues object where column names are the keys,
            // and introduced attributes are the values.
            ContentValues values = new ContentValues();
            values.put(HabitEntry.COLUMN_HABIT, habitString);
            values.put(HabitEntry.COLUMN_WEEK_DAY, mDay);
            values.put(HabitEntry.COLUMN_TIME, time);
            values.put(HabitEntry.COLUMN_REMARKS, remarksString);

            long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

            if (newRowId == -1) {
                Toast.makeText(this, "Error inserting the data to the base", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_save:
                //if the habit were inserted - go to main activity
                //if not - stay in editor activity
                if (insertHabit()) {
                    //Leave the menu
                    finish();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}