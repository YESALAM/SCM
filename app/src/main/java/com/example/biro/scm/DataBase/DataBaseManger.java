package com.example.biro.scm.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.biro.scm.Models.Attendance;
import com.example.biro.scm.Models.Homeworks;
import com.example.biro.scm.Models.Subjects;
import com.example.biro.scm.Models.User;
import com.example.biro.scm.SessionManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Biro on 6/18/2017.
 */

public class DataBaseManger extends SQLiteOpenHelper {

    private final static String DBNAME = "SCM";
    private static DataBaseManger instance = null;
    private static String DB_PATH = "/data/data/com.example.biro.scm/databases/";

    private final static int DATABASE_VERSION = 10;
    private Context context;


    private DataBaseManger(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
        this.context = context;
    }


    public static DataBaseManger getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseManger(context);
        }
        return instance;
    }

    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DBNAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }


        return checkDB != null;
    }

    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DBNAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DBNAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }


    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();



            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }



    @Override
    public void onCreate(SQLiteDatabase db) {


        String user_type = "create table   " + Contract.User_Type.TABLE_NAME + "   (" +
                Contract.User_Type._ID + " integer primary key autoincrement , " +
                Contract.User_Type.COLUMN_TYPE + " VARCHAR" +
                " )";

        String user = "create table   " + Contract.User.TABLE_NAME + "   (" +
                Contract.User._ID + " integer primary key autoincrement ," +
                Contract.User.COLUMN_NAME + " VARCHAR, " +
                Contract.User.COLUMN_EMAIL + " VARCHAR, " +
                Contract.User.COLUMN_PASSWORD + " VARCHAR , " +
                Contract.User.COLUMN_Image + " blob," +
                Contract.User.COLUMN_USER_TYPE + " INTEGER, FOREIGN KEY(user_type_id) REFERENCES " + Contract.User_Type.TABLE_NAME + " (_id))";


        String homework = "create table   " + Contract.HomeWorks.TABLE_NAME + "   (" +
                Contract.HomeWorks._ID + " integer primary key autoincrement ," +
                Contract.HomeWorks.COLUMN_CONTENT + " blob, " +
                Contract.HomeWorks.COLUMN_DATE + " Varchar, " +
                Contract.HomeWorks.COLUMN_NAME + " Varchar, " +
                Contract.HomeWorks.COLUMN_TITLE + " Varchar, " +
                Contract.HomeWorks.COLUMN_DESCRIPTION + " Varchar, " +
                Contract.HomeWorks.COLUMN_USER_ID + " INTEGER, FOREIGN KEY(user_id) REFERENCES users(_id)" +

                " )";


        String subject = "create table   " + Contract.Subjects.TABLE_NAME + "   (" +
                Contract.Subjects._ID + " integer primary key autoincrement ," +
                Contract.Subjects.COLUMN_NAME + " VARCHAR, " +
                Contract.Subjects.COLUMN_GRADE + " integer, " +
                Contract.Subjects.COLUMN_STATUS + " VARCHAR, " +
                Contract.Subjects.COLUMN_USER_ID + " INTEGER, FOREIGN KEY(user_id) REFERENCES users(_id)" +

                " )";

        String attendance = "create table   " + Contract.Attendance.TABLE_NAME + "   (" +
                Contract.Attendance._ID + " integer primary key autoincrement ," +
                Contract.Attendance.COLUMN_DATE + " VARCHAR, " +

                Contract.Attendance.COLUMN_STATUS + " VARCHAR, " +
                Contract.Attendance.COLUMN_USER_ID + " INTEGER, FOREIGN KEY(user_id) REFERENCES users(_id)" +

                " )";


        db.execSQL(user);
        db.execSQL(attendance);
        db.execSQL(user_type);
        db.execSQL("INSERT INTO " + Contract.User_Type.TABLE_NAME + " (" + Contract.User_Type.COLUMN_TYPE + ")" + " VALUES ('Student')");
        db.execSQL("INSERT INTO " + Contract.User_Type.TABLE_NAME + " (" + Contract.User_Type.COLUMN_TYPE + ")" + " VALUES ('Parent')");
        db.execSQL(subject);
        db.execSQL(homework);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long register(User user, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.User.COLUMN_NAME, user.getName());
        values.put(Contract.User.COLUMN_EMAIL, user.getEmail());
        values.put(Contract.User.COLUMN_PASSWORD, user.getPassword());
        values.put(Contract.User.COLUMN_USER_TYPE, user.getType());
        values.put(Contract.User.COLUMN_Image, image);


        long rowInserted = db.insert(Contract.User.TABLE_NAME, null, values);
        db.close();

        return rowInserted;

    }

    public Cursor select(String table, String select, String selectarrg) {

        return getWritableDatabase().rawQuery("select * from " + table + " where " + select + " = ?", new String[]{selectarrg});
    }

    public boolean isUserExist(String email) {
        Cursor c = select(Contract.User.TABLE_NAME, Contract.User.COLUMN_EMAIL, email);
        return c.getCount() > 0;


    }

    public void saveHomework(byte[] file, Homeworks homeworks,String position) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.HomeWorks.COLUMN_CONTENT, file);
        values.put(Contract.HomeWorks.COLUMN_USER_ID, Integer.valueOf(SessionManager.getInstance(context).getUserId()));
        values.put(Contract.HomeWorks.COLUMN_DATE, homeworks.getDate());
        values.put(Contract.HomeWorks.COLUMN_NAME, homeworks.getName());
        update(Contract.HomeWorks.TABLE_NAME,values,Contract.HomeWorks._ID,position);
        db.close();


    }

    public ArrayList<Subjects> getSubjects() {
        ArrayList<Subjects> subjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db = this.getWritableDatabase();

        Cursor c = select(Contract.Subjects.TABLE_NAME,
                Contract.Subjects.COLUMN_USER_ID, SessionManager.getInstance(context).getUserId());

        Subjects[] temp = new Subjects[c.getCount()];
        for (int i = 0; i < c.getCount(); i++) {
            while (c.moveToNext()) {
                temp[i] = new Subjects();
                temp[i].setName(c.getString(c.getColumnIndex(Contract.Subjects.COLUMN_NAME)));
                temp[i].setGrade(c.getInt(c.getColumnIndex(Contract.Subjects.COLUMN_GRADE)));
                temp[i].setStatus(c.getString(c.getColumnIndex(Contract.Subjects.COLUMN_STATUS)));

                subjects.add(temp[i]);
            }
        }
        return subjects;

    }

    public ArrayList<Homeworks> getHomeworks() {
        ArrayList<Homeworks> homeworks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db = this.getWritableDatabase();

        String x = SessionManager.getInstance(context).getUserId();
        if (SessionManager.getInstance(context).getUserId() == null || SessionManager.getInstance(context).getUserId().isEmpty())
            return new ArrayList<Homeworks>();

        Cursor c = select(Contract.HomeWorks.TABLE_NAME,
                Contract.HomeWorks.COLUMN_USER_ID, SessionManager.getInstance(context).getUserId());


        Homeworks[] temp = new Homeworks[c.getCount()];
        for (int i = 0; i < c.getCount(); i++) {
            while (c.moveToNext()) {
                temp[i] = new Homeworks();
                temp[i].setId(c.getInt(c.getColumnIndex(Contract.HomeWorks._ID)));
                temp[i].setName(c.getString(c.getColumnIndex(Contract.HomeWorks.COLUMN_NAME)));
                temp[i].setDate(c.getString(c.getColumnIndex(Contract.HomeWorks.COLUMN_DATE)));
                temp[i].setDescription(c.getString(c.getColumnIndex(Contract.HomeWorks.COLUMN_DESCRIPTION)));
                temp[i].setTitle(c.getString(c.getColumnIndex(Contract.HomeWorks.COLUMN_TITLE)));

                homeworks.add(temp[i]);
            }
        }
        return homeworks;

    }

//    public ArrayList<Homeworks> getHomewokrs() {
//        ArrayList<Homeworks> homeworks = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        db = this.getWritableDatabase();
//
//        String x = SessionManager.getInstance(context).getUserId();
//        if (SessionManager.getInstance(context).getUserId() == null || SessionManager.getInstance(context).getUserId().isEmpty())
//            return new ArrayList<Homeworks>();
//
//        Cursor c = select(Contract.HomeWorks.TABLE_NAME,
//                Contract.HomeWorks.COLUMN_USER_ID, SessionManager.getInstance(context).getUserId());
//
//
//        Homeworks[] temp = new Homeworks[c.getCount()];
//        for (int i = 0; i < c.getCount(); i++) {
//            while (c.moveToNext()) {
//                temp[i] = new Homeworks();
//                temp[i].setName(c.getString(c.getColumnIndex(Contract.HomeWorks.C)));
//                temp[i].setDate(c.getString(c.getColumnIndex(Contract.HomeWorks.COLUMN_DATE)));
//
//
//                homeworks.add(temp[i]);
//            }
//        }
//        return homeworks;
//    }


    public ArrayList<Attendance> getAttendance() {
        ArrayList<Attendance> attendance = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db = this.getWritableDatabase();

        Cursor c = select(Contract.Attendance.TABLE_NAME,
                Contract.Attendance.COLUMN_USER_ID, SessionManager.getInstance(context).getUserId());

        Attendance[] temp = new Attendance[c.getCount()];
        for (int i = 0; i < c.getCount(); i++) {
            while (c.moveToNext()) {
                temp[i] = new Attendance();
                temp[i].setStatus(c.getString(c.getColumnIndex(Contract.Attendance.COLUMN_STATUS)));
                temp[i].setDate(c.getString(c.getColumnIndex(Contract.Attendance.COLUMN_DATE)));


                attendance.add(temp[i]);
            }
        }
        return attendance;

    }

    public int update(String table, ContentValues contentValues, String select, String selectarrg) {

        return getWritableDatabase().update(table, contentValues, select + " =? ", new String[]{selectarrg});
    }


}
