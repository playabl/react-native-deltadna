package com.playabl.deltadna;

import com.deltadna.android.sdk.DDNA;

import android.content.ActivityNotFoundException;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.Callback;

public class RNDeltaDNAModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private boolean isInitialized = false;

  public RNDeltaDNAModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNDeltaDNA";
  }

  @ReactMethod
  public void start(ReadableMap options) {
    if (!isInitialized) {
      DDNA.initialise(new DDNA.Configuration(
        getCurrentActivity().getApplication(),
        options.getString("environmentKey"),
        options.getString("collectURL"),
        options.getString("engageURL")));

      isInitialized = true;
    }

    DDNA.instance().startSdk();
  }

  @ReactMethod
  public void stop() {
    DDNA.instance().stopSdk();
  }

  @ReactMethod
  public void upload() {
    DDNA.instance().upload();
  }

  /**
   * Checks if a given key is valid
   * @param @{link String} key
   * @param @{link ReadableMap} options
   * @return boolean representing whether the key exists and has a value
   */
  private boolean hasValidKey(String key, ReadableMap options) {
    return options.hasKey(key) && !options.isNull(key);
  }
}
