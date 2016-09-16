SMS Receiver plugin for Cordova
===============================

This Cordova Android plugin allows you to receive incoming SMS. You have the possibility to stop the message broadcasting and, thus, avoid the incoming message native popup.

## Install

```
cordova plugin add cordova-plugin-sms-receiver --save
```

## Usage

### isSupported ###
Check if the SMS technology is supported by the device.

```js
SmsReceiver.isSupported((supported) => {
  if (supported) {
    alert("SMS supported!")
  } else {
    alert("SMS not supported")
  }
}), function() => {
  alert("Error while checking the SMS support")
})
```

### startReception ###
Start the SMS receiver waiting for incoming message
The success callback function will be called everytime a new message is received.
The given parameter is the received message formatted such as: phoneNumber>message (Example: +32472345678>Hello World)
The error callback is called if an error occurs.

Example:
```js
SmsReceiver.startReception(({messageBody, originatingAddress}) => {
  alert(messageBody)
}, () => {
  alert("Error while receiving messages")
})
```

### stopReception ###
Stop the SMS receiver

Example:
```js
SmsReceiver.stopReception(() => {
  alert("Correctly stopped")
}, () => {
  alert("Error while stopping the SMS receiver")
})
```

### Aborting a broadcast ###
If you abort the broadcast using this plugin (see ``broadcast`` boolean variable
in the ``name.ratson.cordova.sms_receiver.SmsReceiver``), the SMS will not be broadcast to other
applications like the native SMS app. So ... be careful !

A good way to manage this is to stop the SMS reception when the onPause event is fired and, when the onResume event is fired, restart the reception.
