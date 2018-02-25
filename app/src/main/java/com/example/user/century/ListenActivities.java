package com.example.user.century;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.user.century.GuestLoginActivity.GuestActivity;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 12/6/2017.
 */

class ListenActivities extends Thread{
    boolean exit = false;
    ActivityManager am = null;
    Context context = null;

    public ListenActivities(Context con){
        context = con;
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public void run(){

        Looper.prepare();

        while(!exit){

            // get the info from the currently running task
            List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(MAX_PRIORITY);

            String activityName = taskInfo.get(0).topActivity.getClassName();


            Log.d("topActivity", "CURRENT Activity ::"
                    + activityName);

            if (activityName.equals("com.android.packageinstaller.UninstallerActivity")) {
                // User has clicked on the Uninstall button under the Manage Apps settings

                //do whatever pre-uninstallation task you want to perform here
                // show dialogue or start another activity or database operations etc..etc..

                // context.startActivity(new Intent(context, MyPreUninstallationMsgActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                try {
                    File dir = context.getCacheDir();
                    deleteDir(dir);
                } catch (Exception e) {}
                exit = true;
                Toast.makeText(context, "Aplikasi berhasil teruninstall", Toast.LENGTH_SHORT).show();
            } else if(activityName.equals("com.android.settings.ManageApplications")) {
                // back button was pressed and the user has been taken back to Manage Applications window
                // we should close the activity monitoring now
                exit=true;
            }
        }
        Looper.loop();
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}