package com.example.android.adamska_habit_tracker.data;

import android.provider.BaseColumns;

/**
 * Created by kasia on 18.07.17.
 */

public class HabitContract {

    private HabitContract() {
    }

    public static abstract class HabitEntry implements BaseColumns {

        public static final String TABLE_NAME = "habits";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_HABIT = "activity";
        public static final String COLUMN_WEEK_DAY = "week_day";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_REMARKS = "additional_remarks";

        //possible values for week_day
        public static final int EVERYDAY = 0;
        public static final int MONDAY = 1;
        public static final int TUESDAY = 2;
        public static final int WEDNESDAY = 3;
        public static final int THURSDAY = 4;
        public static final int FRIDAY = 5;
        public static final int SATURDAY = 6;
        public static final int SUNDAY = 7;
    }
}
