package entrainement.timer.p7_go4lunch.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import entrainement.timer.p7_go4lunch.DI.DI;
import entrainement.timer.p7_go4lunch.api.collegue.ExtendedServiceCollegue;

public class Broadcaster extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
          ExtendedServiceCollegue service = DI.getService();
            service.notifyme(context);
      }
    }
}
