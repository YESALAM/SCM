package com.example.biro.scm.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biro.scm.Models.Attendance;
import com.example.biro.scm.Models.Homeworks;
import com.example.biro.scm.R;

import java.util.ArrayList;

/**
 * Created by Biro on 6/23/2017.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {


    private ArrayList<Attendance> attendance;
    Context context;

    public AttendanceAdapter(ArrayList<Attendance> attendance , Context c) {
        this.attendance = attendance;
        this.context = c;
        if(attendance.isEmpty())
            Toast.makeText(c,"No Attendance to show",Toast.LENGTH_LONG).show();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_row, parent, false);
        return new AttendanceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.attendanceDate.setText(attendance.get(position).getDate());
        holder.attendanceStatus.setText(attendance.get(position).getStatus());
        if(attendance.get(position).getStatus().equals("yes"))
        {
            holder.attendanceStatus.setTextColor(Color.GREEN);
        }
        else
            holder.attendanceStatus.setTextColor(Color.RED);
    }

    @Override
    public int getItemCount() {
        return attendance.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {


        TextView attendanceDate, attendanceStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            attendanceDate = (TextView) itemView.findViewById(R.id.attendanceDate);
            attendanceStatus = (TextView) itemView.findViewById(R.id.attendanceStatus);


        }


    }


}
