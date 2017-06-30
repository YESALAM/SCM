package com.example.biro.scm.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biro.scm.Models.Subjects;
import com.example.biro.scm.R;

import java.util.ArrayList;

/**
 * Created by Biro on 6/21/2017.
 */

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {


    private ArrayList<Subjects> subjects;
    Context contex;

    public SubjectsAdapter(ArrayList<Subjects> subjects , Context c) {
        this.subjects = subjects;
        this.contex = c;
        if(subjects.isEmpty())
            Toast.makeText(c,"No subjects to show",Toast.LENGTH_LONG).show();

    }

    @Override
    public SubjectsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(subjects.size()>0)
        {
            holder.subject_status.setText(subjects.get(position).getStatus());
            holder.subject_name.setText(subjects.get(position).getName());
            holder.subject_grade.setText(String.valueOf(subjects.get(position).getGrade()));
            if(subjects.get(position).getGrade()<50)
                holder.subject_status.setTextColor(Color.RED);
            else
                holder.subject_status.setTextColor(Color.GREEN);
//            Toast.makeText(contex, "No subjects to show"+subjects.size(), Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(contex, "No subjects to show"+subjects.size(), Toast.LENGTH_SHORT).show();


    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView subject_name, subject_grade, subject_status;

        public ViewHolder(View itemView) {
            super(itemView);

            subject_grade = (TextView) itemView.findViewById(R.id.subject_grade);
            subject_name = (TextView) itemView.findViewById(R.id.subject_name);
            subject_status = (TextView) itemView.findViewById(R.id.subject_status);

        }


    }


}
