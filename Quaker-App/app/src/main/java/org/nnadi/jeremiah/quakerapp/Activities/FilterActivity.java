package org.nnadi.jeremiah.quakerapp.Activities;

/*
 - Name: Jeremiah Nnadi
 - StudentID: S1903336
*/

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nnadi.jeremiah.quakerapp.Adapters.FilterAdapter;
import org.nnadi.jeremiah.quakerapp.Item;
import org.nnadi.jeremiah.quakerapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Compounds the FilterActivity Class
 */
public class FilterActivity extends AppCompatActivity {
    TextView tvSpecificDate, tvRangeDate1, tvRangeDate2;
    Button btnSearch;
    RadioButton rbSpecific, rbRange;
    RecyclerView recyclerView;
    FilterAdapter dateAdapter;
    List<Item> itemList = new ArrayList<>();
    int highestIndexId, lowestIndexId, highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId;

    //     When the back button is clicked, return back to the main menu
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivityForResult(myIntent, 0);
//        return true;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        // Adds all the itemList data 
        itemList.addAll(MainActivity.itemList);
        // Adds the 'back' icon in the actionbar 
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Initializes the required widgets 
        rbSpecific = findViewById(R.id.rb_specific);
        rbRange = findViewById(R.id.rb_range);
        tvSpecificDate = findViewById(R.id.tv_date);
        tvRangeDate1 = findViewById(R.id.tv_range_date1);
        tvRangeDate2 = findViewById(R.id.tv_range_date2);
        btnSearch = findViewById(R.id.btn_search);
        recyclerView = findViewById(R.id.recycler_view);
        // Check to ensure that the list is not empty 
        if (itemList.size() > 0) {
            // Sort the list according to Magnitude
            sortList();
            // Attach the dateAdapter to the recyclerView
            dateAdapter = new FilterAdapter(FilterActivity.this, itemList, highestIndexId, lowestIndexId, highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId);
            recyclerView.setLayoutManager(new LinearLayoutManager(FilterActivity.this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(dateAdapter);
        }

        // Add click listeners for the radio buttons
        rbSpecific.setOnClickListener(view -> rbRange.setChecked(false));

        rbRange.setOnClickListener(view -> rbSpecific.setChecked(false));

        // Add click listeners for the date picker dialogs
        tvSpecificDate.setOnClickListener(view -> datePickerDialog(0));

        tvRangeDate1.setOnClickListener(view -> datePickerDialog(1));

        tvRangeDate2.setOnClickListener(view -> datePickerDialog(2));

        // Check to see which of the radio buttons has been selected
        btnSearch.setOnClickListener(view -> {
            if (rbSpecific.isChecked()) {
                if (TextUtils.isEmpty(tvSpecificDate.getText().toString())) {
                    Toast.makeText(FilterActivity.this, "Please choose a single specific date", Toast.LENGTH_SHORT).show();
                } else {
                    // Gets the data for the specific date
                    getSingleDateData();
                }
            } else if (rbRange.isChecked()) {
                if (TextUtils.isEmpty(tvRangeDate1.getText().toString())) {
                    Toast.makeText(FilterActivity.this, "Choose a range start date", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(tvRangeDate2.getText().toString())) {
                    Toast.makeText(FilterActivity.this, "Choose a range end date", Toast.LENGTH_SHORT).show();
                } else {
                    // Gets the data for the date range
                    getRangeDateData();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    /**
     * This method creates a date picker dialog for selecting the dates
     *
     * @param index
     */
    public void datePickerDialog(int index) {
        DatePickerDialog mDatePicker;
        final Calendar mCalendar = Calendar.getInstance();
        mDatePicker = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String formattedDate = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(mCalendar.getTime());
                if (index == 0) {
                    tvSpecificDate.setText(formattedDate);
                } else if (index == 1) {
                    tvRangeDate1.setText(formattedDate);
                } else if (index == 2) {
                    tvRangeDate2.setText(formattedDate);
                }
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePicker.show();
    }


    /**
     * This method checks to ensure that the dates selected are not equal
     *
     * @param eqDateStr
     * @param newDateStr
     * @return result
     */
    public boolean isDateEquals(String eqDateStr, String newDateStr) {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            Date eqDate = sdf.parse(eqDateStr);
            Date newDate = sdf.parse(newDateStr);
            result = newDate.equals(eqDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This method checks to ensure that there are dates present between the start date and the end date selected
     *
     * @param eqDateStr
     * @param startDateStr
     * @param endDateStr
     * @return result
     */
    //   create isDateEqualToRange for checking date are present in between range
    public boolean isDateEqualToRange(String eqDateStr, String startDateStr, String endDateStr) {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            Date eqDate = sdf.parse(eqDateStr);
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);
            result = (eqDate.equals(startDate) || eqDate.after(startDate)) && (eqDate.equals(endDate) || eqDate.before(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This method gets the data for the range of dates entered
     */
    public void getRangeDateData() {
        String startDateStr = tvRangeDate1.getText().toString();
        String endDateStr = tvRangeDate2.getText().toString();
        itemList.clear();
        itemList.addAll(MainActivity.itemList);
        List<Item> newItemList = new ArrayList<>();
        for (Item item : itemList) {
            if (isDateEqualToRange(item.getPubDate(), startDateStr, endDateStr)) {
                newItemList.add(item);
            }
        }
        if (newItemList.size() > 0) {
            itemList.clear();
            itemList.addAll(newItemList);
            // Sorts the list according to magnitude
            sortList();
            dateAdapter.reloadData(highestIndexId, lowestIndexId, highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("No earthquakes were recorded between " + startDateStr + " and " + endDateStr);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
            itemList.clear();
        }
        dateAdapter.notifyDataSetChanged();
    }

    /**
     * Gets the data for the single specific date
     */
    public void getSingleDateData() {
        String eqDateStr = tvSpecificDate.getText().toString();
        itemList.clear();
        itemList.addAll(MainActivity.itemList);
        List<Item> newItemList = new ArrayList<>();
        for (Item item : itemList) {
            if (isDateEquals(item.getPubDate(), eqDateStr)) {
                newItemList.add(item);
            }
        }
        if (newItemList.size() > 0) {
            itemList.clear();
            itemList.addAll(newItemList);
            // Sorts the list according to magnitude
            sortList();
            dateAdapter.reloadData(highestIndexId, lowestIndexId, highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity.this).create();
            alertDialog.setTitle("Try Again");
            alertDialog.setMessage("No earthquakes recorded on " + eqDateStr);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
            itemList.clear();
        }
        dateAdapter.notifyDataSetChanged();
    }


    /**
     * The sortList sorts the list according to earthquake magnitude
     */
    public void sortList() {
        Collections.sort(itemList, (obj1, obj2) -> {
            // Arrange in descending order
            return Double.compare(obj2.getMagnitude(), obj1.getMagnitude());
        });
        getHighestIndex();
        getLowestIndex();
        getLatitudeId();
        getLongitudeId();
        Log.d("theI", "sortList: highestIndexId: " + highestIndexId + " lowestIndexId: " + lowestIndexId);
        Log.d("theI", "sortList: highestLatitudeId: " + highestLatitudeId + " lowestLatitudeId: " + lowestLatitudeId +
                " highestLongitudeId: " + highestLongitudeId + " lowestLongitudeId: " + lowestLongitudeId);
        List<Item> itemList1 = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            int id = itemList.get(i).getId();
            if (id == highestIndexId || id == lowestIndexId ||
                    id == highestLatitudeId || id == lowestLatitudeId || id == highestLongitudeId || id == lowestLongitudeId) {
                itemList1.add(itemList.get(i));
                Log.d("I", "sortList: i: " + i + " ID: " + id);
            }
        }
        itemList.clear();
        itemList.addAll(itemList1);
    }

    /**
     * This method is used to derive the id of the item with the lowest depth within the List
     */
    public void getLowestIndex() {
        double lowest = itemList.get(0).getDepth();
        lowestIndexId = itemList.get(0).getId();
        for (int s = 1; s < itemList.size(); s++) {
            double curValue = itemList.get(s).getDepth();
            if (curValue < lowest) {
                lowest = curValue;
                lowestIndexId = itemList.get(s).getId();
            }
        }
    }

    /**
     * This method is used to derive the id of the item with the greatest depth within the List
     */
    public void getHighestIndex() {
        double highest = itemList.get(0).getDepth();
        highestIndexId = itemList.get(0).getId();
        for (int s = 1; s < itemList.size(); s++) {
            double curValue = itemList.get(s).getDepth();
            if (curValue > highest) {
                highest = curValue;
                highestIndexId = itemList.get(s).getId();
            }
        }
    }

    /**
     * This method gets the Longitude ID of the items
     * It is good for getting the ID of the items with the highest and lowest longitude
     */
    public void getLongitudeId() {
        double highest = itemList.get(0).getLongitude();
        double lowest = itemList.get(0).getLongitude();
        highestLongitudeId = itemList.get(0).getId();
        lowestLongitudeId = itemList.get(0).getId();
        for (int s = 1; s < itemList.size(); s++) {
            double curValue = itemList.get(s).getLongitude();
            if (curValue > highest) {
                highest = curValue;
                highestLongitudeId = itemList.get(s).getId();
            }
            if (curValue < lowest) {
                lowest = curValue;
                lowestLongitudeId = itemList.get(s).getId();
            }
        }
    }

    /**
     * This method gets the Latitude ID of the items
     * It is good for getting the ID of the items with the highest and lowest latitude
     */
    public void getLatitudeId() {
        double highest = itemList.get(0).getLatitude();
        double lowest = itemList.get(0).getLatitude();
        highestLatitudeId = itemList.get(0).getId();
        lowestLatitudeId = itemList.get(0).getId();
        for (int s = 1; s < itemList.size(); s++) {
            double curValue = itemList.get(s).getLatitude();
            if (curValue > highest) {
                highest = curValue;
                highestLatitudeId = itemList.get(s).getId();
            }
            if (curValue < lowest) {
                lowest = curValue;
                lowestLatitudeId = itemList.get(s).getId();
            }
        }
    }
}