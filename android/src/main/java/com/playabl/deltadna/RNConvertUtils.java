package com.playabl.deltadna;

import android.support.annotation.Nullable;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

public class RNConvertUtils {
  @Nullable
  public static JSONObject readableMapToJsonObject(@Nullable ReadableMap readableMap) throws JSONException {
    if (readableMap == null) {
      return null;
    }

    JSONObject object = new JSONObject();

    ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
    if (!iterator.hasNextKey()) {
      return null;
    }

    while (iterator.hasNextKey()) {
      String key = iterator.nextKey();
      switch (readableMap.getType(key)) {
        case Null:
          object.put(key, JSONObject.NULL);
          break;
        case Boolean:
          object.put(key, readableMap.getBoolean(key));
          break;
        case Number:
          object.put(key, readableMap.getDouble(key));
          break;
        case String:
          object.put(key, readableMap.getString(key));
          break;
        case Map:
          object.put(key, readableMapToJsonObject(readableMap.getMap(key)));
          break;
        case Array:
          object.put(key, readableArrayToJsonObject(readableMap.getArray(key)));
          break;
        default:
          throw new IllegalArgumentException("Could not convert object with key: " + key + ".");
      }
    }

    return object;
  }

  @Nullable
  public static JSONArray readableArrayToJsonObject(@Nullable ReadableArray readableArray) throws JSONException {
    if (readableArray == null) {
      return null;
    }

    JSONArray array = new JSONArray();
    if (readableArray.size() == 0) {
      return null;
    }

    for (int i = 0; i < readableArray.size(); i++) {
      switch (readableArray.getType(i)) {
        case Null:
          break;
        case Boolean:
          array.put(readableArray.getBoolean(i));
          break;
        case Number:
          array.put(readableArray.getDouble(i));
          break;
        case String:
          array.put(readableArray.getString(i));
          break;
        case Map:
          array.put(readableMapToJsonObject(readableArray.getMap(i)));
          break;
        case Array:
          array.put(readableArrayToJsonObject(readableArray.getArray(i)));
          break;
        default:
          // Fail silently
      }
    }

    return array;
  }
}
