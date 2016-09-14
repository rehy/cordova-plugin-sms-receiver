SMS Receiver plugin for Cordova
===============================

This Cordova Android plugin allows you to receive incoming SMS. You have the possibility to stop the message broadcasting and, thus, avoid the incoming message native popup.

## Adding this plugin to your project ##
0. (Make sure you are using Phonegap > 2.0)
1. Move SmsInboxPlugin.js to your project's www folder and include a reference to it in your html files.
2. Add the java files from src to your project's src hierarchy
3. Reference the plugin in your res/config.xml file
```<plugin name="SmsInboxPlugin" value="org.apache.cordova.plugin.SmsInboxPlugin"/>```
4. Ensure that your manifest contains the necessary permissions to send SMS messages:
```<uses-permission android:name="android.permission.RECEIVE_SMS" />```


## Using the plugin ##
To instantiate the plugin object:
```javascript
var smsInboxPlugin = cordova.require('cordova/plugin/smsinboxplugin');
```

### isSupported ###
Check if the SMS technology is supported by the device.

Example:
```javascript
  smsInboxPlugin.isSupported ((function(supported) {
    if(supported)
      alert("SMS supported !");
    else
      alert("SMS not supported");
  }), function() {
    alert("Error while checking the SMS support");
  });
```

### startReception ###
Start the SMS receiver waiting for incoming message
The success callback function will be called everytime a new message is received.
The given parameter is the received message formatted such as: phoneNumber>message (Exemple: +32472345678>Hello World)
The error callback is called if an error occurs.

Example:
```javascript
  smsInboxPlugin.startReception (function(msg) {
    alert(msg);
  }, function() {
    alert("Error while receiving messages");
  });
```

### stopReception ###
Stop the SMS receiver

Example:
```javascript
  smsInboxPlugin.stopReception (function() {
    alert("Correctly stopped");
  }, function() {
    alert("Error while stopping the SMS receiver");
  });
```

### Aborting a broadcast ###
If you abort the broadcast using this plugin (see ``broadcast`` boolean variable
in the ``org.apache.cordova.plugin.SmsReceiver``), the SMS will not be broadcast to other
applications like the native SMS app. So ... be careful !

A good way to manage this is to stop the sms reception when the onPause event is fired and, when the onResume event is fired, restart the reception.
