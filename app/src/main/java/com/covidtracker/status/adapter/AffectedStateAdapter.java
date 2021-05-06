package com.covidtracker.status.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.covidtracker.status.R;
import com.covidtracker.status.StateCovidInformation;
import com.covidtracker.status.model.StateName;

import java.util.ArrayList;

public class AffectedStateAdapter extends RecyclerView.Adapter<AffectedStateAdapter.StateHolder> implements Filterable {
    private ArrayList<StateName> stateNameList;
    private ArrayList<StateName> filterStateNameList;
    private Activity activity;

    public AffectedStateAdapter(ArrayList<StateName> stateNameList,Activity activity) {
        this.stateNameList = stateNameList;
        filterStateNameList = stateNameList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public StateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_list,parent,false);
        StateHolder stateHolder = new StateHolder(view);
        return stateHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StateHolder holder, int position) {
        holder.stateNameTxt.setText(filterStateNameList.get(position).getState());
        holder.itemCountTxt.setText(position + 1 + ".");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, StateCovidInformation.class);
                intent.putExtra("state",filterStateNameList.get(position).getState());
                intent.putExtra("totalCases",filterStateNameList.get(position).getConfirmed());
                intent.putExtra("active",filterStateNameList.get(position).getActive());
                intent.putExtra("death",filterStateNameList.get(position).getDeaths());
                intent.putExtra("recovered",filterStateNameList.get(position).getRecovered());
                intent.putExtra("time",filterStateNameList.get(position).getLastupdatedtime());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterStateNameList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSeq = constraint.toString();
                if (charSeq.isEmpty()){
                    filterStateNameList = stateNameList;
                }else {
                    ArrayList<StateName> filterList = new ArrayList<>();
                    for (StateName stateName : stateNameList){
                        if (stateName.getState().toLowerCase().contains(charSeq.toLowerCase())){
                            filterList.add(stateName);
                        }
                    }
                    filterStateNameList = filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterStateNameList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterStateNameList = (ArrayList<StateName>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class StateHolder extends RecyclerView.ViewHolder{
        TextView stateNameTxt,itemCountTxt;
        public StateHolder(@NonNull View itemView) {
            super(itemView);
            stateNameTxt = itemView.findViewById(R.id.stateName);
            itemCountTxt = itemView.findViewById(R.id.itemCount);
        }
    }

}
