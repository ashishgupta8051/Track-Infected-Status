package com.tracked.infectedstatus.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tracked.infectedstatus.R;
import com.tracked.infectedstatus.model.StateName;
import com.tracked.infectedstatus.utils.StateClickListener;

import java.util.ArrayList;
import java.util.List;

public class AffectedStateAdapter extends RecyclerView.Adapter<AffectedStateAdapter.StateHolder> implements Filterable {
    private List<StateName> stateNameList;
    private List<StateName> filterStateNameList;
    private Activity activity;
    private StateClickListener stateClickListener;

    public AffectedStateAdapter(List<StateName> stateNameList,Activity activity,StateClickListener stateClickListener) {
        this.stateNameList = stateNameList;
        filterStateNameList = stateNameList;
        this.activity = activity;
        this.stateClickListener = stateClickListener;
    }

    @NonNull
    @Override
    public StateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_list,parent,false);
        StateHolder stateHolder = new StateHolder(view);
        stateHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateClickListener.onClickListener(stateNameList.get(stateHolder.getAdapterPosition()));
            }
        });
        return stateHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StateHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.stateNameTxt.setText(filterStateNameList.get(position).getState());
        holder.itemCountTxt.setText(position + 1 + ".");
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

            @SuppressLint("NotifyDataSetChanged")
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
