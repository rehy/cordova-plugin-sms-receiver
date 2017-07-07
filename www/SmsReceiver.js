var exec = require('cordova/exec')

exports.isSupported = function (successCallback, failureCallback) {
  return exec(successCallback, failureCallback, 'SmsReceiverPlugin', 'hasSMSPossibility', [])
}

exports.startReception = function (successCallback, failureCallback) {
  return exec(successCallback, failureCallback, 'SmsReceiverPlugin', 'startReception', [])
}

exports.stopReception = function (successCallback, failureCallback) {
  return exec(successCallback, failureCallback, 'SmsReceiverPlugin', 'stopReception', [])
}

exports.requestPermission = function (successCallback, errorCallback) {
  return exec(successCallback, errorCallback, 'SmsReceiverPlugin', 'requestPermission', []);
}
