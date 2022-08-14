package com.raihan.dailyfamily.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.raihan.dailyfamily.R;

import java.util.ArrayList;


public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MyViewHolder>
        implements Filterable {
    public static Object ContactsAdapterListener;
    private Context context;
    private ArrayList<Meal> contactList = null;
    private ArrayList<Meal> contactListFiltered;
    private OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_breakfast, tv_launch, tv_dinner, tv_name_label;
        Button btn_payment_details;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_breakfast = (TextView) itemView.findViewById(R.id.tv_breakfast);
            tv_launch = (TextView) itemView.findViewById(R.id.tv_launch);
            tv_dinner = (TextView) itemView.findViewById(R.id.tv_dinner);
            tv_name_label = (TextView) itemView.findViewById(R.id.tv_name_label);
            btn_payment_details = (Button) itemView.findViewById(R.id.btn_payment_details);

           /* view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });*/
        }
    }


    public MealListAdapter(Context context, ArrayList<Meal> contactList, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_report_daily_meal, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Meal data = contactList.get(position);


        holder.tv_name_label.setText("Date");
        holder.tv_name.setText(data.getDate());
        holder.tv_breakfast.setText(String.valueOf(ValidationUtil.replacecommaDouble(data.getBreakfast())));
        holder.tv_launch.setText(String.valueOf(ValidationUtil.replacecommaDouble(data.getLaunch())));
        holder.tv_dinner.setText(String.valueOf(ValidationUtil.replacecommaDouble(data.getDinner())));
        holder.btn_payment_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onContactSelected(contactList.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactList = contactListFiltered;
                } else {
                    ArrayList<Meal> filteredList = new ArrayList<>();
                    for (Meal row : contactListFiltered) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getId().toLowerCase().contains(charString.toLowerCase())
                                || row.getDate().contains(charSequence)) {
                            filteredList.add(row);

                            // || row.getAccountTitle().contains(charSequence)
                        }
                    }

                    contactList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactList = (ArrayList<Meal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onContactSelected(Meal contact);
    }
}