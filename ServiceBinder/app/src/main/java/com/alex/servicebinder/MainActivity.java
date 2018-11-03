package com.alex.servicebinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private LocalService service;
  private boolean bound = false;
  private Toast toast;

  /**
   * Defines callbacks for service binding, passed to bindService()
   */
  private ServiceConnection connection = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
      // We've bound to LocalService, cast the IBinder and get LocalService instance
      LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
      MainActivity.this.service = binder.getService();
      bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
      bound = false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onStart() {
    super.onStart();
    // Bind to LocalService
    Intent intent = new Intent(this, LocalService.class);
    bindService(intent, connection, Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onStop() {
    super.onStop();
    unbindService(connection);
    bound = false;
  }

  public void onButtonClick(View view) {
    Log.i(TAG, "bound = " + bound);
    if (bound) {
      // Call a method from the LocalService.
      // However, if this call were something that might hang, then this request should
      // occur in a separate thread to avoid slowing down the activity performance.
      int num = service.getRandomNumber();
      if (toast != null) {
        toast.cancel();
      }
      toast = Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT);
      toast.show();
    }
  }
}
