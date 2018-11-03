package com.example.inok.parsejson;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

class HttpHandler {

  private static final String TAG = HttpHandler.class.getSimpleName();

  HttpHandler() {}

  String makeHttpRequest(String reqUrl) {
    String response = null;
    URL url = null;
    HttpURLConnection connection = null;
    InputStream inputStream = null;

    try {
      // Establish connection
      url = new URL(reqUrl);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setReadTimeout(10000);
      connection.setConnectTimeout(15000);
      connection.connect();
      // Read the response
      inputStream = new BufferedInputStream(connection.getInputStream());
      response = convertStreamToString(inputStream);
    } catch (MalformedURLException e) {
      Log.e(TAG, "MalformedURLException: " + e.getMessage());
    } catch (ProtocolException e) {
      Log.e(TAG, "ProtocolException: " + e.getMessage());
    } catch (IOException e) {
      Log.e(TAG, "IOException: " + e.getMessage());
    } catch (Exception e) {
      Log.e(TAG, "Exception: " + e.getMessage());
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          Log.e(TAG, "IOException: " + e.getMessage());
        }
      }
    }

    return response;
  }

  private String convertStreamToString(InputStream is) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line;

    try {
      while ((line = reader.readLine()) != null) {
        sb.append(line).append('\n');
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return sb.toString();
  }
}