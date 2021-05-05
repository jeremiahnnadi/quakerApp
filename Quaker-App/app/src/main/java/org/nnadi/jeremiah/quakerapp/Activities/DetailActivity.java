package org.nnadi.jeremiah.quakerapp.Activities;

/*
 - Name: Jeremiah Nnadi
 - StudentID: S1903336
*/

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.nnadi.jeremiah.quakerapp.Item;
import org.nnadi.jeremiah.quakerapp.R;

/**
 * A compounded DetailActivity class for the individual earthquake items
 */
public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    Item item;
    TextView tvDate, tvLocation, tvMagnitude, tvDepth, tvLatLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Adds the 'Back' Icon in the Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Get the item from the adapter
        item = getIntent().getParcelableExtra("item");
        // Initialize the required (textView(tv)) widgets
        tvDate = findViewById(R.id.tv_date);
        tvLocation = findViewById(R.id.tv_location);
        tvMagnitude = findViewById(R.id.tv_magnitude);
        tvDepth = findViewById(R.id.tv_depth);
        tvLatLong = findViewById(R.id.tv_lat_long);
        // Set the item values on the textviews themselves
        tvDate.setText("Date: " + item.getPubDate());
        tvLocation.setText("Location: " + item.getLocation());
        tvMagnitude.setText("Magnitude: " + item.getMagnitude());
        tvDepth.setText("Depth: " + item.getDepth() + " km");
        tvLatLong.setText("Lat/long: " + item.getLatitude() + "," + item.getLongitude());
        // Creates the google maps fragment for each item
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    /**
     * Generates the google map object using the latlong object, title and icon color provided
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // The latitude|longitude object is created
        LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());
        // Adds the location marker for the map based
        googleMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .title(item.getLocation())
                        .icon(getMarkerIcon(getPinColor(item.getMagnitude()))));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F));
        // Enables Zoom controls for zooming functionality
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }


    /**
     * This method uses the earthquake severity scaling to set the pin color according to the earthquake magnitude
     *
     * @param magnitude
     * @return Pin Color
     */
    public int getPinColor(double magnitude) {
        if (magnitude >= 6.0) {
            return ContextCompat.getColor(DetailActivity.this, R.color.p_red_plus);
        } else if (magnitude <= 5.9 && magnitude >= 4.0) {
            return ContextCompat.getColor(DetailActivity.this, R.color.p_red);
        } else if (magnitude <= 3.9 && magnitude >= 3.0) {
            return ContextCompat.getColor(DetailActivity.this, R.color.p_tulip);
        } else if (magnitude <= 2.9 && magnitude >= 2.0) {
            return ContextCompat.getColor(DetailActivity.this, R.color.p_orange);
        } else if (magnitude <= 1.9 && magnitude >= 1.6) {
            return ContextCompat.getColor(DetailActivity.this, R.color.p_yellow);
        } else if (magnitude <= 1.5 && magnitude >= 1.0) {
            return ContextCompat.getColor(DetailActivity.this, R.color.p_blue);
        } else {
            return ContextCompat.getColor(DetailActivity.this, R.color.p_green);
        }
    }


    /**
     * Creates the getMarkerIcon for changing the marker color
     *
     * @param color
     * @return
     */
    //    Create the  getMarkerIcon for changing the marker color
    public BitmapDescriptor getMarkerIcon(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
