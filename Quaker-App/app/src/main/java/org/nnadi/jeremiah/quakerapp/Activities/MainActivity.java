package org.nnadi.jeremiah.quakerapp.Activities;

/*
 - Name: Jeremiah Nnadi
 - StudentID: S1903336
*/

// Import the needed packages and modules

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nnadi.jeremiah.quakerapp.Adapters.ItemAdapter;
import org.nnadi.jeremiah.quakerapp.Item;
import org.nnadi.jeremiah.quakerapp.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Jeremiah Nnadi
 * Main Activity class for the application
 * Initializes all the components and contains the other
 */
public class MainActivity extends AppCompatActivity {
    public static List<Item> itemList = new ArrayList<>();
    String text;
    String url = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    Button startButton;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    ProgressDialog progressDialog;


    /**
     * The state of the application can be saved and re-passed to onCreate when called.
     * This is used to recreate the activity in certain conditions, like when the orientation is being changed
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the widgets
        startButton = findViewById(R.id.startButton);
        recyclerView = findViewById(R.id.recycler_view);

        // Set a listener on the start button to launch functionality when the button is pressed
        startButton.setOnClickListener(view -> {
            itemList = new ArrayList<>();
            new AsyncTaskExample().execute(url);
        });

        // Check for the value of savedInstanceState (can return application to former Instance )
        if (savedInstanceState != null) {
            itemList.clear();
            itemList.addAll(savedInstanceState.getParcelableArrayList("key"));
            // Attach the item adapter to the recycler view
            itemAdapter = new ItemAdapter(MainActivity.this, itemList);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(itemAdapter);
        }
    }


    /**
     * This method retrieves the per-instance state from an activity before it is stopped
     * It is called before or after onStop()
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        itemList.clear();
        itemList.addAll(savedInstanceState.getParcelableArrayList("key"));
    }

    /**
     * This method is called as the app moves to stop an activity, allowing us to specify more state data
     * The Items are saved in a bundle where the parcelable interface is implemented
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key", new ArrayList<Item>(itemList));

    }


    /**
     * Specifies the options menu for the activity
     * Inflates the menu and adds present items to the action bar
     *
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    /**
     * Handles item selection
     *
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_filter:
                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Gets the data from the URl
     * Implements the XML PullParser
     */
    private class AsyncTaskExample extends AsyncTask<String, String, List<Item>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Implement the progress dialog
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading Earthquake Data ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        /**
         * Invoked on the background thread immediately after onPreExecute() finishes executing.
         * This step is used to perform background parsing that can take a long time.
         * The parameters of the AsyncTaskExample are passed to this step.
         *
         * @param strings
         * @return
         */
        @Override
        protected List<Item> doInBackground(String... strings) {
            int i = 0;
            Item item = null;
            URL url;
            URLConnection urlConnection;
            BufferedReader in = null;

            try {
                Log.e("MyTag", "in try");
                url = new URL(strings[0]);
                urlConnection = url.openConnection();
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                Log.e("MyTag", "after ready");

                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(urlConnection.getInputStream(), null);
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        String tagName = parser.getName();
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if (tagName.equalsIgnoreCase("item")) {
                                    item = new Item();
                                }
                                break;

                            case XmlPullParser.TEXT:
                                text = parser.getText();
                                break;

                            case XmlPullParser.END_TAG:
                                if (item != null) {
                                    if (tagName.equalsIgnoreCase("title")) {
                                        item.setTitle(text);
                                    } else if (tagName.equalsIgnoreCase("description")) {
                                        String[] strings1 = text.split(";");
                                        String location = strings1[1].split(":")[1].trim();
                                        String[] latLong = strings1[2].split(":")[1].trim().split(",");
                                        String depth = strings1[3].replaceAll("[^\\d.]", "").replaceAll(":", "");
                                        String magnitude = strings1[4].replaceAll("[^\\d.]", "").replaceAll(":", "");
                                        item.setDescription(text);
                                        item.setLocation(location);
                                        item.setDepth(Double.parseDouble(depth));
                                        item.setMagnitude(Double.parseDouble(magnitude));
                                        item.setLatitude(Double.parseDouble(latLong[0]));
                                        item.setLongitude(Double.parseDouble(latLong[1]));
                                    } else if (tagName.equalsIgnoreCase("link")) {
                                        item.setLink(text);
                                    } else if (tagName.equalsIgnoreCase("pubDate")) {
                                        item.setPubDate(text);
                                    } else if (tagName.equalsIgnoreCase("category")) {
                                        item.setCategory(text);
                                    } else if (tagName.equalsIgnoreCase("item")) {
                                        i++;
                                        item.setId(i);
                                        Log.d("theS", "doInBackground: " + item.toString());
                                        itemList.add(item);
                                    }
                                }

                                break;

                            default:
                                break;
                        }
                        eventType = parser.next();
                    }

                    Collections.sort(itemList, (obj1, obj2) -> {
                        // Sort in descending order
                        return Double.compare(obj2.getMagnitude(), obj1.getMagnitude());
                    });
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
                in.close();
            } catch (IOException ae) {
                Log.e("Tag", "IOException Met In Run");
            }
            return itemList;
        }


        /**
         * invoked on the UI thread after the background computation finishes.
         * The result of the background computation is passed to this step as a parameter.
         *
         * @param itemList
         */
        @Override
        protected void onPostExecute(List<Item> itemList) {
            super.onPostExecute(itemList);
            // Using an if statement, check to see if the itemList is null
            if (itemList != null) {
                progressDialog.hide();
                // Attach itemAdapter to recyclerView
                itemAdapter = new ItemAdapter(MainActivity.this, itemList);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(itemAdapter);
            } else {
                progressDialog.show();
            }
        }
    }
}