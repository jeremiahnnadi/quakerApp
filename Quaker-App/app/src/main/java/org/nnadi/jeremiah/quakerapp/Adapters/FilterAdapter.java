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

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.Holder> {
    Context context;
    List<Item> itemList;
    int highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId;
    int highestIndex;
    int lowestIndex;

    public FilterAdapter(Context context, List<Item> itemList, int highestIndex, int lowestIndex, int highestLatitudeId, int lowestLatitudeId, int highestLongitudeId, int lowestLongitudeId) {
        this.context = context;
        this.itemList = itemList;
        this.highestIndex = highestIndex;
        this.lowestIndex = lowestIndex;
        this.highestLatitudeId = highestLatitudeId;
        this.lowestLatitudeId = lowestLatitudeId;
        this.highestLongitudeId = highestLongitudeId;
        this.lowestLongitudeId = lowestLongitudeId;
    }

    /**
     * This method calls onCreateViewHolder(ViewGroup, int) to create a new RecyclerView.ViewHolder
     * and initializes some private fields to be used by RecyclerView.
     *
     * @param parent
     * @param viewType
     * @return Holder
     */
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.earthquake_filter, parent, false);
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
        // Sets text values on the textviews
        holder.tvMagnitude.setText("M" + itemList.get(position).getMagnitude());
        holder.tvArea.setText("Location: " + itemList.get(position).getLocation());
        // Changes the color of the card bar to reflect the magnitude of the earthquake item
        double magnitude = itemList.get(position).getMagnitude();

        if (magnitude <= 0.9 && magnitude > 0.0) {
            holder.bar.setBackgroundColor(ContextCompat.getColor(context, R.color.p_green));
        } else if (magnitude <= 1.5 && magnitude >= 1.0) {
            holder.bar.setBackgroundColor(ContextCompat.getColor(context, R.color.p_blue));
        } else if (magnitude <= 1.9 && magnitude >= 1.6) {
            holder.bar.setBackgroundColor(ContextCompat.getColor(context, R.color.p_yellow));
        } else if (magnitude <= 2.9 && magnitude >= 2.0) {
            holder.bar.setBackgroundColor(ContextCompat.getColor(context, R.color.p_orange));
        } else if (magnitude <= 3.9 && magnitude >= 3.0) {
            holder.bar.setBackgroundColor(ContextCompat.getColor(context, R.color.p_tulip));
        } else if (magnitude <= 5.9 && magnitude >= 4.0) {
            holder.bar.setBackgroundColor(ContextCompat.getColor(context, R.color.p_red));
        } else if (magnitude >= 6.0) {
            holder.bar.setBackgroundColor(ContextCompat.getColor(context, R.color.p_red_plus));
        } else {
            holder.bar.setBackgroundColor(ContextCompat.getColor(context, R.color.p_yellow));
        }

        // Sets the shallowest or deepest title on the textView
        if (itemList.get(position).getId() == lowestIndex) {
            holder.tvLabel.setVisibility(View.VISIBLE);
            holder.tvLabel.setText("Shallowest Earthquake: " + itemList.get(position).getDepth() + " km");
        } else if (itemList.get(position).getId() == highestIndex) {
            holder.tvLabel.setVisibility(View.VISIBLE);
            holder.tvLabel.setText("Deepest Earthquake: " + itemList.get(position).getDepth() + " km");
        } else {
            holder.tvLabel.setVisibility(View.GONE);
        }

        // Reviews the listItems and sets the Northerly, Westerly etc. tags and sets the item as visible
        if (itemList.get(position).getId() == highestLatitudeId) {
            holder.tvLabel2.setVisibility(View.VISIBLE);
            holder.tvLabel2.setText("Most Northerly");
        } else if (itemList.get(position).getId() == lowestLatitudeId) {
            holder.tvLabel2.setVisibility(View.VISIBLE);
            holder.tvLabel2.setText("Most Southerly");
        } else if (itemList.get(position).getId() == highestLongitudeId) {
            holder.tvLabel2.setVisibility(View.VISIBLE);
            holder.tvLabel2.setText("Most Easterly");
        } else if (itemList.get(position).getId() == lowestLongitudeId) {
            holder.tvLabel2.setVisibility(View.VISIBLE);
            holder.tvLabel2.setText("Most Westerly");
        } else {
            holder.tvLabel2.setVisibility(View.GONE);
        }

        // Adds a click listener on the itemView (Needed to get the Item details page for each item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent to launch the Detail activity with the specific item data
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("item", itemList.get(position));
                context.startActivity(intent);
            }
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
     * This method reloads and refreshes data every time the the data in the adapter is modified
     *
     * @param highestIndex
     * @param lowestIndex
     * @param highestLatitudeId
     * @param lowestLatitudeId
     * @param highestLongitudeId
     * @param lowestLongitudeId
     */
    public void reloadData(int highestIndex, int lowestIndex, int highestLatitudeId, int lowestLatitudeId, int highestLongitudeId, int lowestLongitudeId) {
        this.highestIndex = highestIndex;
        this.lowestIndex = lowestIndex;
        this.highestLatitudeId = highestLatitudeId;
        this.lowestLatitudeId = lowestLatitudeId;
        this.highestLongitudeId = highestLongitudeId;
        this.lowestLongitudeId = lowestLongitudeId;
        // Calling notifyDataSetChanged() on the Adapter object once the data in the adapter has been modified.
        notifyDataSetChanged();
    }

    /**
     * The ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    public class Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        View bar;
        TextView tvMagnitude, tvLabel, tvLabel2, tvArea;

        public Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            tvMagnitude = itemView.findViewById(R.id.tv_magnitude);
            tvLabel = itemView.findViewById(R.id.tv_label);
            tvLabel2 = itemView.findViewById(R.id.tv_label2);
            tvArea = itemView.findViewById(R.id.tv_area);
            bar = itemView.findViewById((R.id.bar));
        }
    }
}
