//package entrainement.timer.smssender;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.telephony.SmsManager;
//import android.widget.Toast;
//
//import androidx.test.platform.app.InstrumentationRegistry;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static org.junit.Assert.*;
//
///**
// * Instrumented test, which will execute on an Android device.
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//@RunWith(AndroidJUnit4.class)
//public class ExampleInstrumentedTest {
//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//
//        assertEquals("entrainement.timer.smssender", appContext.getPackageName());
//    }
//}
//package entrainement.timer.smssender;
//
//        import android.content.BroadcastReceiver;
//        import android.content.Context;
//        import android.content.Intent;
//        import android.os.Bundle;
//        import android.telephony.SmsManager;
//        import android.widget.Toast;
//
//public class Receiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String zone= intent.getStringExtra("zt");
//        String num= intent.getStringExtra("numero");
//        try{
//            SmsManager smgr = SmsManager.getDefault();
//            smgr.sendTextMessage(num,null,zone,null,null);
//            Toast.makeText(context, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
//        }
//        catch (Exception e){
//            Toast.makeText(context, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
