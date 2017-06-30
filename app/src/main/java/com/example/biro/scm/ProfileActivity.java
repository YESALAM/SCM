package com.example.biro.scm;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biro.scm.DataBase.Contract;
import com.example.biro.scm.DataBase.DataBaseManger;
import com.example.biro.scm.Models.User;
import com.pkmmte.view.CircularImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    Button update;
    EditText username;
    EditText Email;
    EditText password;
    Toolbar toolbar;
    byte[] image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        username = (EditText) findViewById(R.id.editTextUserName);
        Email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        update = (Button) findViewById(R.id.buttonUpdate);
        CircularImageView circularImageView = (CircularImageView) findViewById(R.id.imageprofile);


        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        circularImageView.setBorderWidth(10);
        circularImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });


        Cursor rowID = DataBaseManger.getInstance(this).select(Contract.User.TABLE_NAME, Contract.User._ID, SessionManager.getInstance(this).getUserId());
        if (rowID.moveToNext()) {
            byte[] bitmapdata = rowID.getBlob(rowID.getColumnIndex(Contract.User.COLUMN_Image));
            password.setText(rowID.getString(rowID.getColumnIndex(Contract.User.COLUMN_PASSWORD)));
            username.setText(rowID.getString(rowID.getColumnIndex(Contract.User.COLUMN_NAME)));
            Email.setText(rowID.getString(rowID.getColumnIndex(Contract.User.COLUMN_EMAIL)));
            if (bitmapdata != null) {
                circularImageView.setImageBitmap(BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length));
            }
        }
        updateUser(image);
    }

    public void updateUser(final byte[] image) {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();


                contentValues.put(Contract.User.COLUMN_NAME, username.getText().toString());

                contentValues.put(Contract.User.COLUMN_EMAIL, Email.getText().toString());

                contentValues.put(Contract.User.COLUMN_PASSWORD, password.getText().toString());
                if(image!=null)
                    contentValues.put(Contract.User.COLUMN_Image, image);


                long rowID = DataBaseManger.getInstance(ProfileActivity.this).update(Contract.User.TABLE_NAME, contentValues, Contract.User._ID, SessionManager.getInstance(ProfileActivity.this).getUserId());
                if (rowID > 0) {
                    Toast.makeText(getApplicationContext(), "UPDATE SUCCESS", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "UPDATE FAILD", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {


            Uri imageUri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(imageUri);
                assert inputStream != null;
                image = IOUtils.toByteArray(inputStream);
                updateUser(image);


            } catch (IOException e) {
                e.printStackTrace();

            }

        }
    }
}
