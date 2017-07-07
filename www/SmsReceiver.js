import exec from 'cordova/exec'

export function isSupported(successCallback, failureCallback) {
  return exec(successCallback, failureCallback, 'SmsReceiverPlugin', 'hasSMSPossibility', [])
}

export function startReception(successCallback, failureCallback) {
  return exec(successCallback, failureCallback, 'SmsReceiverPlugin', 'startReception', [])
}

export function stopReception(successCallback, failureCallback) {
  return exec(successCallback, failureCallback, 'SmsReceiverPlugin', 'stopReception', [])
}

export function requestPermission(successCallback, errorCallback) {
  return exec(successCallback, errorCallback, 'SmsReceiverPlugin', 'requestPermission', []);
}
