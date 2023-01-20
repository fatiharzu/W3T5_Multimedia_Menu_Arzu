package w3t4.unterricht.w3t5_multimedia_menu_arzu;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;

public class AppGlobal
{
    private static Context context = null;

    // --------------------------------------------------------------
    public static Context getContext()
    {
        return context;
    }

    // --------------------------------------------------------------
    public static void setContext(Context context)
    {
        if ((AppGlobal.context == null) && (context != null))
            AppGlobal.context = context;
    }

    // --------------------------------------------------------------
    public static Button getButton(Activity activity, int id, View.OnClickListener ocl)
    {
        Button b = (Button) activity.findViewById(id);
        b.setOnClickListener(ocl);
        return b;
    }

    // --------------------------------------------------------------
    public static void permissions(Activity activity, String... permissions)
    {
                /*
        Hierdurch wird ein Dialog angezeigt, in dem die Berechtigung für das
        Durchführen eines Telefonanrufs erteilt werden soll.
         */
        ActivityCompat.requestPermissions(activity, permissions, 13);

        if (Build.VERSION.SDK_INT >= 30)
        {
            if (!Environment.isExternalStorageManager())
            {
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activity.startActivity(getpermission);
            }
        }
    }

}
