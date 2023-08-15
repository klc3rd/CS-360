package com.zybooks.weighttracker;

import static android.content.ContentValues.TAG;
import static com.zybooks.weighttracker.EditWeight.WEIGHT_ID;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.weighttracker.DAO.Weight;

import java.util.ArrayList;

public class WeightsRecyclerViewAdapter extends RecyclerView.Adapter<WeightsRecyclerViewAdapter.ViewHolder> {

    private static ArrayList<Weight> localDataSet;
    private LayoutInflater mInflater;


    public WeightsRecyclerViewAdapter(Context context, ArrayList<Weight> weights) {
        this.mInflater = LayoutInflater.from(context);
        this.localDataSet = weights;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateField;
        private TextView weightField;
        private Button editBtn;


        public ViewHolder(View view) {
            super(view);
            dateField = view.findViewById(R.id.dateField);
            weightField = view.findViewById(R.id.weightField);
            editBtn = view.findViewById(R.id.editBtn);
        }

        public TextView getDateField() {
            return dateField;
        }

        public TextView getWeightField() {
            return weightField;
        }

        public Button getEditBtn() {
            return editBtn;
        }

    }

    public void CustomAdapter(ArrayList<Weight> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.weights_row, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        String date = localDataSet.get(position).getDate().toString();
        String weight = String.valueOf(localDataSet.get(position).getWeight());

        viewHolder.getDateField().setText(date);
        viewHolder.getWeightField().setText(weight);
        viewHolder.getEditBtn().setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: EDIT PRESSED");

            Intent intent = new Intent(view.getContext(), EditWeight.class);
            intent.putExtra(WEIGHT_ID, localDataSet.get(position).getId());
            view.getContext().startActivity(intent);
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
