package entrainement.timer.p7_go4lunch.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ConnectivityChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        debugIntent(intent,context, "grokkingandroid");
        context.unregisterReceiver(this);

    }
    private void debugIntent(Intent intent, Context context, String tag) {
        Other.GPSOnVerify(context);
        Other.internetIsOn(context);
        Log.v(tag, "action: " + intent.getAction());

        Log.v(tag, "component: " + intent.getComponent());
        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String key: extras.keySet()) {
                Log.v(tag, "key [" + key + "]: " +
                        extras.get(key));
            }
        }
        else {
            Log.v(tag, "no extras");
        }
    }
}
