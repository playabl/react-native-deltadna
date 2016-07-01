package com.playabl.deltadna;

import com.deltadna.android.sdk.DDNA;
import com.deltadna.android.sdk.Event;
import com.deltadna.android.sdk.Engagement;
import com.deltadna.android.sdk.listeners.EngageListener;

import android.content.ActivityNotFoundException;
import android.util.Log;

import org.json.JSONException;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.Callback;

public class RNDeltaDNAModule extends ReactContextBaseJavaModule {

  private final String TAG = "RNDeltaDNA";
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
  public void newSession() {
    DDNA.instance().newSession();
  }

  @ReactMethod
  public void clearPersistentData() {
    DDNA.instance().clearPersistentData();
  }

  @ReactMethod
  public void upload() {
    DDNA.instance().upload();
  }

  @ReactMethod
  public void recordEvent(ReadableMap options) {
    Event ev = new Event(options.getString("name"));

    if (hasValidKey("params", options)) {
      ReadableMap params = options.getMap("params");

      ReadableMapKeySetIterator iterator = params.keySetIterator();
      if (iterator.hasNextKey()) {
        while (iterator.hasNextKey()) {
          try {
            String key = iterator.nextKey();
            ev.putParam(key, RNConvertUtils.readableMapToJsonObject(params.getMap(key)));
          } catch (JSONException exception) {
            Log.e(TAG, "Error converting params object");
          }
        }
      }
    }

    DDNA.instance().recordEvent(ev);
  }

  @ReactMethod
  public void recordPushNotification(ReadableMap options, boolean launch) {
    DDNA.instance().recordNotificationOpened(launch);
  }

  @ReactMethod
  public void engage(ReadableMap options, Callback callback) {
    Engagement engagement = new Engagement(options.getString("name"));

    if (hasValidKey("params", options)) {
      ReadableMap params = options.getMap("params");

      ReadableMapKeySetIterator iterator = params.keySetIterator();
      if (iterator.hasNextKey()) {
        while (iterator.hasNextKey()) {
          try {
            String key = iterator.nextKey();
            engagement.putParam(key, RNConvertUtils.readableMapToJsonObject(params.getMap(key)));
          } catch (JSONException exception) {
            Log.e(TAG, "Error converting params object");
          }
        }
      }
    }

    DDNA.instance().requestEngagement(engagement, new EngageListenerCallback(callback));
  }


  private class EngageListenerCallback implements EngageListener<Engagement> {
    private Callback callback;

    EngageListenerCallback(Callback callback) {
      this.callback = callback;
    }

    @Override
    public void onCompleted(Engagement engagement) {
      callback.invoke(engagement.getJson());
    }

    @Override
    public void onError(Throwable t) {
      Log.w(TAG, "Engagement error");
    }
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
