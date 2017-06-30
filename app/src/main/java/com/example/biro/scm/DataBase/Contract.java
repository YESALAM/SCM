package com.example.biro.scm.DataBase;

import android.os.Build;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;

/**
 * Created by Biro on 6/18/2017.
 */

public class Contract {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static final class User implements Telephony.BaseMmsColumns {

        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_Image = "image";
        public static final String COLUMN_USER_TYPE = "user_type_id";

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static final class User_Type implements Telephony.BaseMmsColumns {
        public static final String TABLE_NAME = "user_type";
        public static final String COLUMN_TYPE = "type";
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static final class HomeWorks implements Telephony.BaseMmsColumns {
        public static final String TABLE_NAME = "homeworks";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static final class Subjects implements Telephony.BaseMmsColumns {
        public static final String TABLE_NAME = "subjects";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_GRADE = "grade";
        public static final String COLUMN_STATUS = "status";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static final class Attendance implements Telephony.BaseMmsColumns {
        public static final String TABLE_NAME = "attendance";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_STATUS = "status";
    }
}
