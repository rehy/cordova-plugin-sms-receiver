package name.ratson.cordova.sms_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_EXTRA_NAME = "pdus";
    private CallbackContext callback_receive;
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
                if (this.isReceiving && this.callback_receive != null) {
                    String formattedMsg = sms.getMessageBody();
                    PluginResult result = new PluginResult(PluginResult.Status.OK, formattedMsg);
                    result.setKeepCallback(true);
                    callback_receive.sendPluginResult(result);
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
        this.callback_receive = ctx;
        this.isReceiving = true;
    }

    public void stopReceiving() {
        this.callback_receive = null;
        this.isReceiving = false;
    }
}
