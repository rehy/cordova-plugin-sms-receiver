package name.ratson.cordova.sms_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;

public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_EXTRA_NAME = "pdus";
    private CallbackContext callbackReceive;
    private boolean isReceiving = true;

    // This broadcast boolean is used to continue or not the message broadcast
    // to the other BroadcastReceivers waiting for an incoming SMS (like the native SMS app)
    private boolean broadcast = false;

    @Override
    public void onReceive(Context ctx, Intent intent) {

        // Get the SMS map from Intent
        Bundle extras = intent.getExtras();
        if (extras != null) {
            // Get received SMS Array
            Object[] smsExtra = (Object[]) extras.get(SMS_EXTRA_NAME);

            for (int i = 0; i < smsExtra.length; i++) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtra[i]);
                if (this.isReceiving && this.callbackReceive != null) {
                    JSONObject jsonObj = new JSONObject();
                    try {
                        jsonObj.put("messageBody", sms.getMessageBody());
                        jsonObj.put("originatingAddress", sms.getOriginatingAddress());
                    } catch (Exception e) {
                        System.out.println("Error: " + e);
                    }
                    PluginResult result = new PluginResult(PluginResult.Status.OK, jsonObj);
                    result.setKeepCallback(true);
                    callbackReceive.sendPluginResult(result);
                }
            }

            // If the plugin is active and we don't want to broadcast to other receivers
            if (this.isReceiving && !broadcast) {
                this.abortBroadcast();
            }
        }
    }

    public void broadcast(boolean v) {
        this.broadcast = v;
    }

    public void startReceiving(CallbackContext ctx) {
        this.callbackReceive = ctx;
        this.isReceiving = true;
    }

    public void stopReceiving() {
        this.callbackReceive = null;
        this.isReceiving = false;
    }
}
