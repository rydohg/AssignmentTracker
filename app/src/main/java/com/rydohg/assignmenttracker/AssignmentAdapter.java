package com.rydohg.assignmenttracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {
    private ArrayList<Assignment> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mAssignmentName;
        TextView mImportance;
        TextView mDueDate;
        ViewHolder(View v) {
            super(v);
            mAssignmentName = (TextView) v.findViewById(R.id.assignment_textView);
            mImportance = (TextView) v.findViewById(R.id.importance_textView);
            mDueDate = (TextView) v.findViewById(R.id.due_textView);
        }
    }

    public AssignmentAdapter(ArrayList<Assignment> assignments) {
        mDataset = assignments;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AssignmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_assignment, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mAssignmentName.setText(mDataset.get(position).getName());
        holder.mImportance.setText(mDataset.get(position).getImportance());
        holder.mDueDate.setText(mDataset.get(position).getFormattedDueDate());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
