package com.example.leaf_soc;
import android.util.Log;
import java.util.Locale;

public class ParseClimatePower {
    static String TAG;
    static final String START_MSG_CLIMATE = "510 8 ";
    static double climatePowerKWt = 0.0;
    static int climatePowerScale = 0;

    public static void climatePower (String msg) {
        Log.i(TAG, "climatePower run");
        if (msg.contains(START_MSG_CLIMATE)) {
            try {
                int startStrclimatePower = msg.indexOf(START_MSG_CLIMATE);
                String strHexClimatePower = msg.substring(startStrclimatePower);
                if (strHexClimatePower.length() > 28) {
                    strHexClimatePower = strHexClimatePower.substring(15, 17);
                    String str_climatePower = Long.toBinaryString(Long.parseLong(strHexClimatePower, 16));
                    int rawClimatePower = Integer.parseInt(str_climatePower, 2);
                    climatePowerKWt = rawClimatePower * 0.25;

                    if (climatePowerKWt >= 0.0 & climatePowerKWt <=6.0){
                        climatePowerScale = (int) (climatePowerKWt * 10);
                        climatePowerToDisplay();}
                    if (climatePowerKWt >= 32.0){
                        climatePowerKWt = climatePowerKWt - 32.0;
                        climatePowerScale = (int) (climatePowerKWt * 10);
                        climatePowerToDisplay();}
                    if (climatePowerKWt > 6.0 & climatePowerKWt <32.0){
                        climatePowerKWt = 0.0;
                        climatePowerScale = 0;
                        climatePowerToDisplay();}
                }
            } catch (Exception e) {
                Log.i(TAG, "climatePower Exception: " + e);
            }
        }
    }
    public static void climatePowerToDisplay(){
        MainActivity.progressBarClimatePower.setProgress(climatePowerScale);
        MainActivity.textViewClimatePower.setText(" " + (String.format(Locale.US,"%.1f",climatePowerKWt)) + " ");
    }
}
