package entrainement.timer.p7_go4lunch.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class initBroadcaster4GPS extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            Other.GPSOnVerify(context);
            context.unregisterReceiver(this);

        }
    }
}
