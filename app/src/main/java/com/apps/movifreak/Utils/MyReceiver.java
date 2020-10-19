package com.apps.movifreak.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.movifreak.Home.MainActivity;
import com.apps.movifreak.R;

/**
 * Created by abhinav on 18/10/20.
 */

public class MyReceiver extends BroadcastReceiver {
    private Dialog error_dialog;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtils.getConnectivityStatusString(context);
        error_dialog = new Dialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.custom_internet_dialog, null);
        error_dialog.setContentView(dialogView);
        Button retry = (Button) dialogView.findViewById(R.id.retry);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_dialog.dismiss();
                ((Activity) context).finish();
                Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);
            }
        });

        if (status.isEmpty() || status.equals("No internet is available") || status.equals("No Internet Connection")) {
            status = "No Internet Connection";
            error_dialog.setCancelable(false);
            error_dialog.show();
        }

//        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}