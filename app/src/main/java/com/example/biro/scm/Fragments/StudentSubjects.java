package com.example.biro.scm.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.biro.scm.Adapters.SubjectsAdapter;
import com.example.biro.scm.DataBase.DataBaseManger;
import com.example.biro.scm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentSubjects extends Fragment {

    private RecyclerView subjectList;
    private SubjectsAdapter subjectsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_subjects, container, false);
        subjectList = (RecyclerView) view.findViewById(R.id.subjectsList);
        subjectList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        subjectList.setLayoutManager(layoutManager);

        subjectsAdapter = new SubjectsAdapter(DataBaseManger.getInstance(getActivity()).getSubjects(),getContext());
        subjectList.setAdapter(subjectsAdapter);
//        Toast.makeText(getContext(), "No subjeasdadqweqcts to show", Toast.LENGTH_SHORT).show();

        return view;
    }

}
