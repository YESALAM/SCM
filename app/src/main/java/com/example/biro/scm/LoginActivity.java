package com.example.biro.scm;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.biro.scm.DataBase.Contract;
import com.example.biro.scm.DataBase.DataBaseManger;
import com.example.biro.scm.Models.User;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin, btnsignup;
    AutoCompleteTextView txtemail, txtpassword;
    public static final String USER_DATA = "Logged in user";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        btnsignup = (Button) findViewById(R.id.btnsignup);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        SessionManager.getInstance(this).checkLogin();


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(LoginActivity.this, Registration.class);
                startActivity(mainIntent);

            }
        });


        txtemail = (AutoCompleteTextView) findViewById(R.id.txtemail2);
        txtpassword = (AutoCompleteTextView) findViewById(R.id.txtpassword);
        btnLogin = (Button) findViewById(R.id.btnlogin);
        btnsignup = (Button) findViewById(R.id.btnsignup);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txtemail.getText().toString();
                String password = txtpassword.getText().toString();
                if (email.trim().equals(""))
                    txtemail.setError("Please enter email Address");
                else if (!email.contains("@") || !email.contains("."))
                    txtemail.setError("Please enter a valid email Address");
                else if (password.trim().equals(""))
                    txtpassword.setError("Not a valid password");

                else {

                    Cursor rowID = DataBaseManger.getInstance(getApplicationContext()).select(Contract.User.TABLE_NAME, Contract.User.COLUMN_EMAIL, email);
                    if (rowID.getCount() > 0) {
                        rowID.moveToFirst();
                        if (rowID.getString(rowID.getColumnIndex(Contract.User.COLUMN_PASSWORD)).equals(password)) {

                            String userid = rowID.getString(rowID.getColumnIndex(Contract.User._ID));
                            SessionManager.getInstance(getApplicationContext()).createLoginSession(userid);
                            rowID.close();
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                            GoToHome(userid);


                        } else {

                            Toast.makeText(getApplicationContext(), "Faild Wrong email or password", Toast.LENGTH_LONG).show();
                        }
                    }
                    else  Toast.makeText(getApplicationContext(), "Faild Wrong email or password", Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    public void GoToHome(String userid) {

        Intent mainIntent;


        mainIntent = new Intent(getApplicationContext(), MainActivity.class);


        //put user data
        Cursor rowID = DataBaseManger.getInstance(getApplicationContext()).select(Contract.User.TABLE_NAME, Contract.User._ID,
                userid);
        if (rowID.getCount() > 0) {

            rowID.moveToNext();
            User loggedInUser = new User();
            loggedInUser.setName(rowID.getString(rowID.getColumnIndex(Contract.User.COLUMN_NAME)));
            loggedInUser.setPassword(rowID.getString(rowID.getColumnIndex(Contract.User.COLUMN_PASSWORD)));
            loggedInUser.setEmail(rowID.getString(rowID.getColumnIndex(Contract.User.COLUMN_EMAIL)));
            loggedInUser.setType(rowID.getInt(rowID.getColumnIndex(Contract.User.COLUMN_USER_TYPE)));
            rowID.close();
            mainIntent.putExtra(USER_DATA, loggedInUser);
            startActivity(mainIntent);
            this.finish();
//            Toast.makeText(getApplicationContext(),"hellow",Toast.LENGTH_LONG).show();
        }


    }
}
