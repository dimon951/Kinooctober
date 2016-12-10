package dmitriy.deomin.kinooctober.servis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by Admin on 20.11.2016.
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //запустить сервис
            context.startService(new Intent(context,Run_update_site.class));
        }
    }
}