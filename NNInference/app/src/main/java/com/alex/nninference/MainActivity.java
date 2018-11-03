package com.alex.nninference;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  private static final int INPUT_DIM = 7;
  private static final String INPUT_NAME = "input_1";
  private static final String[] OUTPUT_NAMES = {"pi/Softmax", "v/Tanh"};
  private static final String MODEL_FILE = "opt_model.pb";


  private TensorFlowInferenceInterface inferenceInterface;
  private LoadModelTask loadModelTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    loadModelTask = new LoadModelTask(this, getAssets());
    loadModelTask.execute(MODEL_FILE);
  }

  @Override
  protected void onDestroy() {
    if (loadModelTask != null) {
      loadModelTask.cancel(true);
    }
    if (inferenceInterface != null) {
      inferenceInterface.close();
    }
    super.onDestroy();
  }

  /**
   * Callback for LoadModelTask
   */
  public void onModelReady(TensorFlowInferenceInterface inferenceInterface) {
    this.inferenceInterface = inferenceInterface;
    if (inferenceInterface != null) {
      Log.d(TAG, "Model ready");
      predict();
    }
  }

  @SuppressLint("DefaultLocale")
  private void predict() {
    float[] board = {
        0, 0, 0, 1, 0, 0, 0,
        0, 0, 0, -1, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0
    };

    inferenceInterface.feed(INPUT_NAME, board, 1, INPUT_DIM, INPUT_DIM);
    inferenceInterface.run(OUTPUT_NAMES);

    float[] pi = new float[INPUT_DIM * INPUT_DIM];
    float[] value = {0.0f};

    inferenceInterface.fetch(OUTPUT_NAMES[0], pi);
    inferenceInterface.fetch(OUTPUT_NAMES[1], value);

    Log.d(TAG, "Pi:");
    for (int i = 0; i < INPUT_DIM; i++) {
      StringBuilder sb = new StringBuilder();
      for (int j = 0; j < INPUT_DIM; j++) {
        sb.append(String.format("%.2f", pi[INPUT_DIM * i + j])).append(" ");
      }
      Log.d(TAG, sb.toString());
    }
    Log.d(TAG, "Value: " + value[0]);
  }

  /**
   * Async task for loading model from asset
   */
  private static class LoadModelTask extends AsyncTask<String, Void, TensorFlowInferenceInterface> {

    private WeakReference<MainActivity> activityRef;
    private WeakReference<AssetManager> assetManagerRef;

    LoadModelTask(MainActivity activityRef, AssetManager assetManager) {
      this.activityRef = new WeakReference<>(activityRef);
      this.assetManagerRef = new WeakReference<>(assetManager);
    }

    @Override
    protected TensorFlowInferenceInterface doInBackground(String... strings) {
      if (strings.length == 0 || TextUtils.isEmpty(strings[0])) {
        Log.d(TAG, "Missing model name");
        return null;
      }
      return new TensorFlowInferenceInterface(assetManagerRef.get(), strings[0]);
    }

    @Override
    protected void onPostExecute(TensorFlowInferenceInterface tensorFlowInferenceInterface) {
      MainActivity activity = activityRef.get();
      if (activity != null) {
        activity.onModelReady(tensorFlowInferenceInterface);
      }
    }

    @Override
    protected void onCancelled() {
      super.onCancelled();
      Log.d(TAG, "Task cancelled");
    }
  }
}
