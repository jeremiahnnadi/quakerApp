package org.nnadi.jeremiah.quakerapp.Adapters;

/*
 - Name: Jeremiah Nnadi
 - StudentID: S1903336
*/

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.nnadi.jeremiah.quakerapp.Activities.DetailActivity;
import org.nnadi.jeremiah.quakerapp.Item;
import org.nnadi.jeremiah.quakerapp.R;

import java.util.List;

/**
 * The ItemAdapter class deals with the data of each earthquake Item
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.Holder> {
    Context context;
    List<Item> itemList;

    public ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    /**
     * This method calls onCreateViewHolder(ViewGroup, int) to create a new RecyclerView.ViewHolder
     * and initializes some private fields to be used by RecyclerView.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.earthquake, parent, false);
        return new Holder(view);
    }

    /**
     * update the RecyclerView.ViewHolder contents with the item at the given position and
     * also sets up some private fields to be used by RecyclerView.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        // Sets the values on the respective textviews
        holder.tvTitle.setText("Location: " + itemList.get(position).getLocation());
        holder.tvMagnitude.setText("M" + itemList.get(position).getMagnitude());
        holder.cDate.setText("Date: " + itemList.get(position).getPubDate());

        //Parse the magnitude of each earthquake item and color-code it accordingly
        double magnitude = itemList.get(position).getMagnitude();

        if (magnitude <= 0.9 && magnitude > 0.0) {
            holder.tvMagnitude.setBackgroundColor(ContextCompat.getColor(context, R.color.p_green));
        } else if (magnitude <= 1.5 && magnitude >= 1.0) {
            holder.tvMagnitude.setBackgroundColor(ContextCompat.getColor(context, R.color.p_blue));
        } else if (magnitude <= 1.9 && magnitude >= 1.6) {
            holder.tvMagnitude.setBackgroundColor(ContextCompat.getColor(context, R.color.p_yellow));
        } else if (magnitude <= 2.9 && magnitude >= 2.0) {
            holder.tvMagnitude.setBackgroundColor(ContextCompat.getColor(context, R.color.p_orange));
        } else if (magnitude <= 3.9 && magnitude >= 3.0) {
            holder.tvMagnitude.setBackgroundColor(ContextCompat.getColor(context, R.color.p_tulip));
        } else if (magnitude <= 5.9 && magnitude >= 4.0) {
            holder.tvMagnitude.setBackgroundColor(ContextCompat.getColor(context, R.color.p_red));
        } else if (magnitude >= 6.0) {
            holder.tvMagnitude.setBackgroundColor(ContextCompat.getColor(context, R.color.p_red_plus));
        } else {
            holder.tvMagnitude.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
        }

        // Adds a click listener on the itemView (Needed to get the Item details page for each item)
        holder.itemView.setOnClickListener(view -> {
            // Create intent to launch the Detail activity with the specific item data
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("item", itemList.get(position));
            context.startActivity(intent);
        });
    }

    /**
     * Method to count the number of items in the itemList
     *
     * @return itemList size
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }


    /**
     * The ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    public class Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle, tvMagnitude, cDate;

        public Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMagnitude = itemView.findViewById(R.id.tv_magnitude);
            cDate = itemView.findViewById(R.id.tv_date);
        }
    }


}
