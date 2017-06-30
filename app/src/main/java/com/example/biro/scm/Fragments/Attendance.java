package com.example.biro.scm.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.biro.scm.Adapters.AttendanceAdapter;
import com.example.biro.scm.DataBase.DataBaseManger;
import com.example.biro.scm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Attendance extends Fragment {

    AttendanceAdapter attendanceAdapter;
    RecyclerView attendanceList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_attendance, container, false);

        attendanceList = (RecyclerView) view.findViewById(R.id.attendanceList);
        attendanceList.setLayoutManager(new LinearLayoutManager(getContext()));
        attendanceAdapter = new AttendanceAdapter(DataBaseManger.getInstance(getActivity()).getAttendance(),getContext());
        attendanceList.setHasFixedSize(true);
        attendanceList.setAdapter(attendanceAdapter);
        return view;
    }

}
