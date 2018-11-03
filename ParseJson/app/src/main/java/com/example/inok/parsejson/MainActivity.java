package com.example.inok.parsejson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

  private String TAG = MainActivity.class.getSimpleName();

  private ArrayList<HashMap<String, String>> contactList;
  private ListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    contactList = new ArrayList<>();
    listView = findViewById(R.id.list);

    new GetContacts().execute();
  }

  private class GetContacts extends AsyncTask<Void, Void, Void> {

    private final String REQUEST_URL = "https://api.androidhive.info/contacts/";

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      Toast.makeText(MainActivity.this, "JSON Data is downloading...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
      HttpHandler sh = new HttpHandler();
      String jsonStr = null;

      // Make a request to url and get a response
      jsonStr = sh.makeHttpRequest(REQUEST_URL);
      Log.i(TAG, "Response from url: " + jsonStr);

      if (jsonStr != null) {
        try {
          JSONObject jsonObj = new JSONObject(jsonStr);

          // Get JSON Array node
          JSONArray contacts = jsonObj.getJSONArray("contacts");

          // Loop through all contacts
          for (int i = 0; i < contacts.length(); i++) {
            JSONObject c = contacts.getJSONObject(i);
            String id = c.getString("id");
            String name = c.getString("name");
            String email = c.getString("email");
            String address = c.getString("address");
            String gender = c.getString("gender");

            // Phone node is a JSON Object
            JSONObject phone = c.getJSONObject("phone");
            String mobile = phone.getString("mobile");
            String home = phone.getString("home");
            String office = phone.getString("office");

            // Hash map for single contact
            HashMap<String, String> contact = new HashMap<>();

            // Add each child node to HashMap
            contact.put("id", id);
            contact.put("name", name);
            contact.put("email", email);
            contact.put("mobile", mobile);

            // Add contact to contact list
            contactList.add(contact);
          }
        } catch (final JSONException e) {
          Log.e(TAG, "JSON parsing error: " + e.getMessage());
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              Toast.makeText(getApplicationContext(),
                  "JSON parsing error: " + e.getMessage(),
                  Toast.LENGTH_LONG).show();
            }
          });
        }
      } else {
        Log.e(TAG, "Couldn't get JSON from server.");
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Toast.makeText(getApplicationContext(),
                "Couldn't get JSON from server. Check Logcat for possible errors",
                Toast.LENGTH_LONG).show();
          }
        });
      }

      return null;
    }

    @Override
    protected void onPostExecute(Void result) {
      super.onPostExecute(result);
      ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList, R.layout.item,
          new String[]{"name", "email"}, new int[]{R.id.name_text, R.id.email_text});
      listView.setAdapter(adapter);
    }
  }
}
