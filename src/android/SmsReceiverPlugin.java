package name.ratson.cordova.sms_receiver;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

public class SmsReceiverPlugin extends CordovaPlugin {
    public final String ACTION_HAS_SMS_POSSIBILITY = "HasSMSPossibility";
    public final String ACTION_RECEIVE_SMS = "StartReception";
    public final String ACTION_STOP_RECEIVE_SMS = "StopReception";

    private CallbackContext callback_receive;
    private SmsReceiver smsReceiver = null;
    private boolean isReceiving = false;

    public SmsReceiverPlugin() {
        super();
    }

    @Override
    public boolean execute(String action, JSONArray arg1,
                           final CallbackContext callbackContext) throws JSONException {

        if (action.equals(ACTION_HAS_SMS_POSSIBILITY)) {

            Activity ctx = this.cordova.getActivity();
            if (ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, true));
            } else {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, false));
            }
            return true;
        } else if (action.equals(ACTION_RECEIVE_SMS)) {

            // if already receiving (this case can happen if the startReception is called
            // several times
            if (this.isReceiving) {
                // close the already opened callback ...
                PluginResult pluginResult = new PluginResult(
                        PluginResult.Status.NO_RESULT);
                pluginResult.setKeepCallback(false);
                this.callback_receive.sendPluginResult(pluginResult);

                // ... before registering a new one to the sms receiver
            }
            this.isReceiving = true;

            if (this.smsReceiver == null) {
                this.smsReceiver = new SmsReceiver();
                IntentFilter fp = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                fp.setPriority(1000);
                // fp.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
                this.cordova.getActivity().registerReceiver(this.smsReceiver, fp);
            }

            this.smsReceiver.startReceiving(callbackContext);

            PluginResult pluginResult = new PluginResult(
                    PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
            this.callback_receive = callbackContext;

            return true;
        } else if (action.equals(ACTION_STOP_RECEIVE_SMS)) {

            if (this.smsReceiver != null) {
                smsReceiver.stopReceiving();
            }

            this.isReceiving = false;

            // 1. Stop the receiving context
            PluginResult pluginResult = new PluginResult(
                    PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(false);
            this.callback_receive.sendPluginResult(pluginResult);

            // 2. Send result for the current context
            pluginResult = new PluginResult(
                    PluginResult.Status.OK);
            callbackContext.sendPluginResult(pluginResult);

            return true;
        }

        return false;
    }
}
