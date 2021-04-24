package com.covid19tracker.status.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.covid19tracker.status.CountryCovidInformation;
import com.covid19tracker.status.R;
import com.covid19tracker.status.model.CountryNameModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AffectedCountryAdapter extends RecyclerView.Adapter<AffectedCountryAdapter.Holder> implements Filterable {
    private List<CountryNameModel> countryNameModelList;
    private List<CountryNameModel> countryNameModelListFilter;
    private Activity activity;
    private String value;

    public AffectedCountryAdapter(List<CountryNameModel> countryNameModelList, Activity activity, String value) {
        this.countryNameModelList = countryNameModelList;
        this.activity = activity;
        this.value = value;
        countryNameModelListFilter = countryNameModelList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_list,parent,false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CountryNameModel countryNameModel = countryNameModelListFilter.get(position);
        Picasso.get().load(countryNameModel.getFlag()).fit().into(holder.flagImg);
        holder.countryNameTxt.setText(countryNameModel.getCountry());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CountryCovidInformation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("country", countryNameModel.getCountry());
                intent.putExtra("cases", countryNameModel.getCases());
                intent.putExtra("TodayCases", countryNameModel.getTodayCases());
                intent.putExtra("death", countryNameModel.getDeaths());
                intent.putExtra("TodayDeath", countryNameModel.getTodayDeaths());
                intent.putExtra("recovered", countryNameModel.getRecovered());
                intent.putExtra("TodayRecovered", countryNameModel.getTodayRecovered());
                intent.putExtra("active", countryNameModel.getActive());
                intent.putExtra("critical", countryNameModel.getCritical());
                intent.putExtra("tests", countryNameModel.getTests());
                intent.putExtra("population", countryNameModel.getPopulation());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryNameModelListFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSeq = constraint.toString();
                if (charSeq.isEmpty()){
                    countryNameModelListFilter = countryNameModelList;
                }else {
                    ArrayList<CountryNameModel> filterList = new ArrayList<>();
                    for (CountryNameModel countryNameModel : countryNameModelList){
                        if (countryNameModel.getCountry().toLowerCase().contains(charSeq.toLowerCase())){
                            filterList.add(countryNameModel);
                        }
                    }
                    countryNameModelListFilter = filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = countryNameModelListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                countryNameModelListFilter = (ArrayList<CountryNameModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView flagImg;
        TextView countryNameTxt;
        public Holder(@NonNull View itemView) {
            super(itemView);
            flagImg = itemView.findViewById(R.id.imageFlag);
            countryNameTxt = itemView.findViewById(R.id.tvCountryName);
        }
    }
}
