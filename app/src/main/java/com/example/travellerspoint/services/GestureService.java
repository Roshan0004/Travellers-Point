package com.example.travellerspoint.services;

import android.app.Service;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

public class GestureService extends Service {

    private static int REQUEST_CALL_PERMISSION = 0004;

    public GestureService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
            gestureOverlayView.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
            gestureOverlayView.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
                @Override
                public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                    String phoneNumber = "9372379725";

                    // Create an Intent with the ACTION_CALL action and the phone number
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber));

                    startActivity(callIntent);
                }
            });

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.addView(gestureOverlayView, layoutParams);
        } catch (Exception e) {
            Log.e("roshanTest", "onStartCommand: " + e.getMessage());
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}