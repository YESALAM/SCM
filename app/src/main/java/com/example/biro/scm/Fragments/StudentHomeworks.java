package com.example.biro.scm.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.biro.scm.Adapters.HomeworksAdapter;
import com.example.biro.scm.DataBase.Contract;
import com.example.biro.scm.DataBase.DataBaseManger;
import com.example.biro.scm.MainActivity;
import com.example.biro.scm.Models.Homeworks;
import com.example.biro.scm.R;
import com.example.biro.scm.SessionManager;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentHomeworks extends Fragment {

    Button uploadBtn;
    RecyclerView homeworkList;
    HomeworksAdapter homeworksAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_student_homeworks, container, false);


        homeworkList = (RecyclerView) view.findViewById(R.id.homeworkList);
        homeworkList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        homeworkList.setLayoutManager(layoutManager);
        homeworksAdapter = new HomeworksAdapter(DataBaseManger.getInstance(getContext()).getHomeworks(), getActivity());
        homeworkList.setAdapter(homeworksAdapter);

        return view;
    }




//    private void enable_button() {
//        uploadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                new MaterialFilePicker()
//                        .withActivity(getActivity())
//                        .withRequestCode(10)
//                        .start();
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ( resultCode == RESULT_OK) {


            try {

                FileInputStream fis = new FileInputStream(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                byte[] pdf = new byte[fis.available()];
                fis.read(pdf);
                Homeworks homeworks = new Homeworks();

                long date = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
                String dateString = sdf.format(date);
                homeworks.setDate(dateString);
                Toast.makeText(getActivity(), dateString, Toast.LENGTH_LONG).show();
                homeworks.setName(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                 DataBaseManger.getInstance(getActivity()).saveHomework(pdf, homeworks,String.valueOf(requestCode));
                Toast.makeText(getContext(), "success", Toast.LENGTH_LONG).show();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
