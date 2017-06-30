package com.example.biro.scm;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.biro.scm.DataBase.Contract;
import com.example.biro.scm.DataBase.DataBaseManger;
import com.example.biro.scm.Fragments.About;
import com.example.biro.scm.Fragments.Attendance;
import com.example.biro.scm.Fragments.StudentHomeworks;
import com.example.biro.scm.Fragments.StudentSubjects;
import com.example.biro.scm.Models.User;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    User user;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;
    StudentHomeworks mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);




        if (!SessionManager.getInstance(this).isLoggedIn()) {
            Intent intent = getIntent();
            user = intent.getParcelableExtra(LoginActivity.USER_DATA);

        } else {
            String userId = SessionManager.getInstance(this).getUserId();
            //put user data
            Cursor rowID = DataBaseManger.getInstance(getApplicationContext()).select(Contract.User.TABLE_NAME, Contract.User._ID,
                    userId);
            if (rowID.getCount() > 0) {

                rowID.moveToNext();
                user = new User();

                user.setName(rowID.getString(rowID.getColumnIndex(Contract.User.COLUMN_NAME)));
                user.setPassword(rowID.getString(rowID.getColumnIndex(Contract.User.COLUMN_PASSWORD)));
                user.setEmail(rowID.getString(rowID.getColumnIndex(Contract.User.COLUMN_EMAIL)));
                user.setType(rowID.getInt(rowID.getColumnIndex(Contract.User.COLUMN_USER_TYPE)));
                rowID.close();

            }
        }


        SecondaryDrawerItem homeworks = new SecondaryDrawerItem();
        SecondaryDrawerItem subjetcs = new SecondaryDrawerItem();
        SecondaryDrawerItem about = new SecondaryDrawerItem();
        SecondaryDrawerItem attendnce = new SecondaryDrawerItem();
        homeworks.withName("Homeworks");
        subjetcs.withName("Subjects");
        about.withName("About");

        if (user.getType() == 1) {
            mainFragment = new StudentHomeworks();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragContainer, mainFragment).commit();
            toolbar.setTitle("Homeworks");
            toolbar.setTitleTextColor(Color.WHITE);
        }
        else
        {
            Attendance attendance = new Attendance();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragContainer,attendance).commit();
            toolbar.setTitle("Attendance");
            toolbar.setTitleTextColor(Color.WHITE);

        }
        attendnce.withName("Attendnce");

        if (SessionManager.getInstance(this).isLoggedIn()) {

            Cursor c = DataBaseManger.getInstance(this).select(Contract.User.TABLE_NAME,
                    Contract.User._ID, SessionManager.getInstance(this).getUserId());
            byte[] image = null;
            Bitmap bitmap = null;
            if (c.moveToNext()) {
                image = c.getBlob(c.getColumnIndex(Contract.User.COLUMN_Image));

            }
            if (image != null) {
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            }

            AccountHeader headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(R.color.primary)
                    .withProfileImagesClickable(false)
                    .addProfiles(
                            new ProfileDrawerItem().withName(user.getName()).withEmail(user.getEmail()).withIcon(bitmap)
                    )
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                            return false;
                        }
                    })
                    .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                        @Override
                        public boolean onClick(View view, IProfile profile) {
                            Toast.makeText(getApplicationContext(), "profile", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            intent.putExtra(LoginActivity.USER_DATA, user);
                            startActivity(intent);
                            return false;
                        }
                    })

                    .build();

            if (user.getType() == 1) {
                Drawer drawer = new DrawerBuilder()
                        .withActivity(this)
                        .withToolbar(toolbar)
                        .addDrawerItems(homeworks, subjetcs, about)
                        .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                                switch (position) {
                                    case 0:


                                        fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.fragContainer, mainFragment).commit();

                                        toolbar.setTitle("Homework");
                                        toolbar.setTitleTextColor(Color.WHITE);
                                        break;

                                    case 1:
                                        StudentSubjects studentSubjects = new StudentSubjects();
                                        fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.fragContainer, studentSubjects).commit();
                                        toolbar.setTitle("Subjects");
                                        toolbar.setTitleTextColor(Color.WHITE);
                                        break;

                                    case 2:
                                        toolbar.setTitle("About");
                                        toolbar.setTitleTextColor(Color.WHITE);
                                        About about = new About();
                                        fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.fragContainer, about).commit();
                                        break;
                                }
                                return false;
                            }
                        })
                        .withSelectedItem(0)
                        .withAnimateDrawerItems(true)
                        .withAccountHeader(headerResult)
                        .withHeaderDivider(true)
                        .build();
            } else {

                Drawer drawer = new DrawerBuilder()
                        .withActivity(this)
                        .withToolbar(toolbar)
                        .addDrawerItems(subjetcs, attendnce, about)
                        .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                                switch (position) {

                                    case 0:
                                        StudentSubjects studentSubjects = new StudentSubjects();
                                        fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.fragContainer, studentSubjects).commit();
                                        toolbar.setTitle("Subjects");
                                        toolbar.setTitleTextColor(Color.WHITE);
                                        break;
                                    case 1:
                                        toolbar.setTitle("Attendnce");
                                        toolbar.setTitleTextColor(Color.WHITE);
                                        Attendance attendanceFrag = new Attendance();
                                        fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.fragContainer, attendanceFrag).commit();
                                        break;
                                    case 2:
                                        toolbar.setTitle("About");
                                        toolbar.setTitleTextColor(Color.WHITE);
                                        About about = new About();
                                        fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.fragContainer, about).commit();
                                }
                                return false;
                            }
                        })
                        .withSelectedItem(1)
                        .withAnimateDrawerItems(true)
                        .withAccountHeader(headerResult)
                        .withHeaderDivider(true)
                        .build();
            }


            toolbar.inflateMenu(R.menu.menu_main);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Toast.makeText(getApplicationContext(), "Loging out!", Toast.LENGTH_SHORT).show();
                    SessionManager.getInstance(getApplicationContext()).logoutUser();
                    return true;
                }
            });


        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        StudentHomeworks fragment = (StudentHomeworks) getSupportFragmentManager().findFragmentById(R.id.fragContainer);
        fragment.onActivityResult(requestCode, resultCode, data);
    }


}
