package com.playabl.deltadna;

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
          object.put(key, RNConvertUtils.readableMapToJsonObject(readableMap.getMap(key)));
          break;
        case Array:
          object.put(key, RNConvertUtils.readableArrayToJsonObject(readableMap.getArray(key)));
          break;
        default:
          throw new IllegalArgumentException("Could not convert object with key: " + key + ".");
      }
    }

    return object;
  }

  @Nullable
  public static JSONArray readableArrayToJsonObject(@Nullable ReadableArray readableArray) throws JSONException {
    if (readableMap == null) {
      return null;
    }

    JSONArray array = new JSONArray();
    if (readableArray.size() === 0) {
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
          array.put(convertMapToJson(readableArray.getMap(i)));
          break;
        case Array:
          array.put(convertArrayToJson(readableArray.getArray(i)));
          break;
        default:
          // Fail silently
      }
    }

    return array;
  }
}
