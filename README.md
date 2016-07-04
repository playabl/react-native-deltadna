# react-native-deltadna

Integrating DeltaDNA Game Analytics into your React Native app.

### Installation
```
$ npm install react-native-deltadna
```
(If you want to persist the installed package in your `package.json`, add `--save` to this command.)

#### Mostly automatic installation
If you haven't RNPM installed, install it with `npm install -g rnpm`.

Call `rnpm link` to link the native parts against your application and continue with `Additional configuration`.

#### Manual installation

##### Android
1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.playable.deltadna.RNDeltaDNAPackage;` to the imports at the top of the file
  - Add `new RNDeltaDNAPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-deltadna'
  	project(':react-native-deltadna').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-deltadna/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-deltadna')
  	```

##### iOS
1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-deltadna` and add `RNDeltaDNA.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNDeltaDNA.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)

#### Additional configuration
Since this package relies on a third party library, there are a few additional things that need to be configured.

##### Android
DeltaDNA uses their own repository, we need to add it to the `android/build.grade` file in the `allprojects -> repositories` section. In the end, this section should look something like this:
```
allprojects {
    repositories {
        mavenLocal()
        jcenter()
        maven {
            // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
            url "$rootDir/../node_modules/react-native/android"
        }
        maven {
            url "http://deltadna.bintray.com/android"
        }
    }
}
```

##### iOS
For iOS, the application needs to link against `DeltaDNA`. This can be done in different ways:
- Add the compiled `DeltaDNA.framework` to your application by compiling the `DeltaDNA` library first and then adding it as a dependency into your application
- Add `DeltaDNA.xcodeproj` as a dependency into your application
- Use CocoaPods and follow the instructions from http://docs.deltadna.com/advanced-integration/ios-sdk/

Allow HTTP communication in your iOS app by setting the following keys in your `Info.plist` file:
```xml
<key>NSAppTransportSecurity</key>
<dict>
  <key>NSAllowsArbitraryLoads</key>
  <true/>
</dict>
```

![](http://docs.deltadna.com/wp-content/uploads/2014/08/Screen-Shot-2015-09-18-at-09.05.30.png)

### Running the example
To run the example, add `credentials.json` to root level of the example folder. It needs to be in form of:
```json
{
  "environmentKey": "<My Environment Key, actual numbers as a string here please>",
  "collectURL": "<CollectURL here please>",
  "engageURL": "<EngageURL here please>"
}
```

### Methods
With the DeltaDNA's iOS and Android SDK being slightly different in terms of their API, this library tends to implement a common superset of both APIs which follows the API of the iOS SDK more closely than the Android SDK.

To access `DeltaDNA` in your React Native application, import the module first.
```javascript
import DeltaDNA from 'react-native-deltadna';
```

#### `DeltaDNA.start`
```javascript
DeltaDNA.start({
  environmentKey: 'My environment key',
  collectURL: 'My collectURL',
  engageURL: 'My engageURL',
  userID: 'Optional, the userID for a session, will be automatically generated otherwise',
});
```
This method starts the DeltaDNA SDK and by extension, emits the `newPlayer`, `gameStarted` and `clientDevice` events by default.

This function needs to be called at the start of the application.

#### `DeltaDNA.stop`
```javascript
DeltaDNA.stop();
```
Stops the SDK and emits the `gameEnded` event.

#### `DeltaDNA.newSession`
```javascript
DeltaDNA.newSession();
```
Starts a new session

#### `DeltaDNA.upload`
```javascript
DeltaDNA.upload();
```
By default, DeltaDNA uploads all new data every minute. If you want or need to force an upload, call this method.

#### `DeltaDNA.clearPersistantData`
```javascript
DeltaDNA.clearPersistantData();
```
Clears all data on the device.

#### `DeltaDNA.recordEvent`
```javascript
DeltaDNA.recordEvent({
  name: 'missionCompompleted',
});
```
This method uploads an event to with the specified `name`. `name` is the only property that's necessarily needed. If you need send additional parameters as well, simply define a `params` property.
```javascript
DeltaDNA.recordEvent({
  name: 'missionCompleted',
  params: {
    level: 5,
  },
});
```

#### `DeltaDNA.recordPushNotification`
```javascript
DeltaDNA.recordPushNotification({
  name: 'PushNotification',
}, false);
```
Upload a `notificationOpened` event. On iOS, a payload can be defined as the first parameter, while the payload does not have any effect on Android. The second parameter indicates if the notification was used to open the app (if `true`) or if the app was already running.

#### `DeltaDNA.engage`
```javascript
DeltaDNA.engage({
  name: 'Engagement'
}, result => {
  alert(result);
});
```
Similar to sending an event with the difference that we can react to the response with a callback. Params can be added in the same way they are added to events.

### License

MIT, see `LICENSE`
