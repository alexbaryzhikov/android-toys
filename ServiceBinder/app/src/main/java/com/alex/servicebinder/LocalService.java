package com.alex.servicebinder;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class LocalService extends Service {
  // Binder given to clients
  private final IBinder mBinder = new LocalBinder();
  // Random number generator
  private final Random mGenerator = new Random();

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  /**
   * Method for clients
   */
  public int getRandomNumber() {
    return mGenerator.nextInt(100);
  }

  /**
   * Class used for the client Binder.  Because we know this service always
   * runs in the same process as its clients, we don't need to deal with IPC.
   */
  public class LocalBinder extends Binder {
    LocalService getService() {
      // Return this instance of LocalService so clients can call public methods
      return LocalService.this;
    }
  }
}
