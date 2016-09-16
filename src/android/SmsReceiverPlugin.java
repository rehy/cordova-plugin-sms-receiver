package name.ratson.cordova.sms_receiver;

import android.Manifest;
import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

public class SmsReceiverPlugin extends CordovaPlugin {
    private static final String ACTION_REQUEST_PERMISSION = "requestPermission";
    public final String ACTION_HAS_SMS_POSSIBILITY = "hasSMSPossibility";
    public final String ACTION_RECEIVE_SMS = "startReception";
    public final String ACTION_STOP_RECEIVE_SMS = "stopReception";
    private CallbackContext callbackReceive;
    private SmsReceiver smsReceiver = null;
    private boolean isReceiving = false;
    private int requestCode = 20160916;

    public SmsReceiverPlugin() {
        super();
    }

    @Override
    public boolean execute(String action, JSONArray arg1,
                           final CallbackContext callbackContext) throws JSONException {

        if (ACTION_HAS_SMS_POSSIBILITY.equals(action)) {
            hasSmsPossibility(callbackContext);
            return true;
        } else if (ACTION_RECEIVE_SMS.equals(action)) {
            receiveSms(callbackContext);
            return true;
        } else if (ACTION_STOP_RECEIVE_SMS.equals(action)) {
            stopReceiveSms(callbackContext);
            return true;
        } else if (ACTION_REQUEST_PERMISSION.equals(action)) {
            requestPermission(Manifest.permission.RECEIVE_SMS, callbackContext);
            return true;
        }

        return false;
    }

    private void stopReceiveSms(CallbackContext callbackContext) {
        if (this.smsReceiver != null) {
            smsReceiver.stopReceiving();
        }

        this.isReceiving = false;

        // 1. Stop the receiving context
        PluginResult pluginResult = new PluginResult(
                PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(false);
        this.callbackReceive.sendPluginResult(pluginResult);

        // 2. Send result for the current context
        pluginResult = new PluginResult(
                PluginResult.Status.OK);
        callbackContext.sendPluginResult(pluginResult);
    }

    private void receiveSms(CallbackContext callbackContext) {
        // if already receiving (this case can happen if the startReception is called
        // several times
        if (this.isReceiving) {
            // close the already opened callback ...
            PluginResult pluginResult = new PluginResult(
                    PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(false);
            this.callbackReceive.sendPluginResult(pluginResult);

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
        this.callbackReceive = callbackContext;
    }

    private void hasSmsPossibility(CallbackContext callbackContext) {
        Activity ctx = this.cordova.getActivity();
        if (ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, true));
        } else {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, false));
        }
    }

    private boolean hasPermissionGranted(String type) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return (PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(this.cordova.getActivity(), type));
    }

    private void requestPermission(String type, CallbackContext callbackContext) {
        if (!hasPermissionGranted(type)) {
            ActivityCompat.requestPermissions(this.cordova.getActivity(), new String[]{type}, requestCode);
        }
        callbackContext.success();
    }
}
