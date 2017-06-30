package com.example.biro.scm.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biro.scm.Fragments.StudentHomeworks;
import com.example.biro.scm.Models.Homeworks;
import com.example.biro.scm.Models.Subjects;
import com.example.biro.scm.R;
import com.nbsp.materialfilepicker.MaterialFilePicker;

import java.util.ArrayList;

/**
 * Created by Biro on 6/21/2017.
 */

public class HomeworksAdapter extends RecyclerView.Adapter<HomeworksAdapter.ViewHolder> {


    private ArrayList<Homeworks> homeworks;
    Activity contex;

    public HomeworksAdapter(ArrayList<Homeworks> homeworks, Activity c) {
        this.homeworks = homeworks;
        this.contex = c;
        if (homeworks.isEmpty())
            Toast.makeText(c, "No homeworks to show", Toast.LENGTH_LONG).show();

    }

    @Override
    public HomeworksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.homework_row, parent, false);
        final ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity(contex)
                        .withRequestCode(homeworks.get(vh.getAdapterPosition()).getId())

                        .start();

            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (homeworks.size() > 0) {
            if(homeworks.get(position).getName()==null)
            {
                holder.homework_name.setText("click here to upload");
            }
            else
                holder.homework_name.setText(homeworks.get(position).getName());
            if( homeworks.get(position).getDate()!=null)
                 holder.homework_date.setText(homeworks.get(position).getDate());

            holder.homework_desc.setText(homeworks.get(position).getDescription());
            holder.homework_title.setText(homeworks.get(position).getTitle());

        }


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return homeworks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView homework_name, homework_date, homework_title, homework_desc;

        public ViewHolder(View itemView) {
            super(itemView);

            homework_date = (TextView) itemView.findViewById(R.id.homeworkDate);
            homework_name = (TextView) itemView.findViewById(R.id.homeworkName);
            homework_title = (TextView) itemView.findViewById(R.id.homeworkTitle);
            homework_desc = (TextView) itemView.findViewById(R.id.homeworkDescription);

        }


    }


}
