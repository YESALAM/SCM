package com.example.biro.scm;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biro.scm.DataBase.DataBaseManger;
import com.example.biro.scm.Models.User;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Registration extends AppCompatActivity {

    private Button btnSignUp, btnClear;
    private AutoCompleteTextView txtpass, txtemail, txtname;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private TextView imageText;
    private Button userImage;
    private static final int SELECT_PICTURE = 1;
    private Toolbar toolbar;
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_registration);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtemail = (AutoCompleteTextView) findViewById(R.id.txtemail);
        txtpass = (AutoCompleteTextView) findViewById(R.id.txtpass);
        txtname = (AutoCompleteTextView) findViewById(R.id.txtname);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnClear = (Button) findViewById(R.id.btnClear);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        userImage = (Button) findViewById(R.id.userImage);
        imageText = (TextView) findViewById(R.id.textImage);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        enable_button();
        registerUser(image);


    }

    public void enable_button() {
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
    }





    public void registerUser(final byte[] image) {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtemail.getText().toString();
                String pass = txtpass.getText().toString();
                String name = txtname.getText().toString();
                if (name.trim().equals(""))
                    txtname.setError("Please enter your name");
                else if (email.trim().equals(""))
                    txtemail.setError("Please enter email Address");
                else if (!email.contains("@") || !email.contains("."))
                    txtemail.setError("Please enter a valid email Address");
                else if (pass.trim().length() < 8)
                    txtpass.setError("Not a valid password -less than 8-");
                else if (DataBaseManger.getInstance(getApplicationContext()).isUserExist(email))
                    txtemail.setError("This email is already registered");
                else if (image == null)
                    imageText.setError("Upload Image");

                else {

                    int select = radioGroup.getCheckedRadioButtonId();
                    User user = new User();
                    user.setEmail(email);
                    user.setName(name);
                    user.setPassword(pass);
                    switch (select) {
                        case R.id.studentButton:
                            user.setType(1);
                            if (DataBaseManger.getInstance(getApplicationContext()).register(user, image) > 0) {
                                Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                            } else
                                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                            break;
                        case R.id.parentButton:
                            user.setType(2);
                            if (DataBaseManger.getInstance(getApplicationContext()).register(user, image) > 0) {
                                Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                            } else
                                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "choose role ", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtemail.setText("");
                txtpass.setText("");
                txtname.setText("");
                txtname.setError(null);
                txtemail.setError(null);
                txtpass.setError(null);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {


            Uri imageUri  = data.getData();
            InputStream inputStream = null;
            try {
                inputStream= getContentResolver().openInputStream(imageUri);
                assert inputStream != null;
                image = IOUtils.toByteArray(inputStream);
                registerUser(image);
                Toast.makeText(this,"sdasd",Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
                    Toast.makeText(this,"sdasdaasdqweqweasdwqfwegETQERVsd",Toast.LENGTH_LONG).show();
            }




        }
    }
}


