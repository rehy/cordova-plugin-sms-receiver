import exec from 'cordova/exec'

export function isSupported(successCallback, failureCallback) {
  return exec(successCallback, failureCallback, 'SmsReceiverPlugin', 'HasSMSPossibility', [])
}

export function startReception(successCallback, failureCallback) {
  return exec(successCallback, failureCallback, 'SmsReceiverPlugin', 'StartReception', [])
}

export function stopReception(successCallback, failureCallback) {
  return exec(successCallback, failureCallback, 'SmsReceiverPlugin', 'StopReception', [])
}
