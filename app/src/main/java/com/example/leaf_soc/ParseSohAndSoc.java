package com.example.leaf_soc;
import android.util.Log;

public class ParseSohAndSoc {
    static String TAG;
    static final String START_MSG_SOH = "7BB 8 25";
    static final String START_MSG_SOC = "7BB 8 24";
    static final double AHR_NEW_BATTARY =  65.4;
    static double SOH = 0.0;
    static double SOC = 0.0;;

    public static double parseSoh(String msg) {
        Log.i(TAG, "parseSoh run MSG: " + msg);
        if (msg.contains(START_MSG_SOH)) {
            String HEX_AHR;
            int startIndex = msg.indexOf(START_MSG_SOH);
            HEX_AHR = (msg.substring(startIndex + 12, startIndex + 14)) + (msg.substring(startIndex + 15, startIndex + 17)) + (msg.substring(startIndex + 18, startIndex + 20));
            String str_binAhr =  Long.toBinaryString(Long.parseLong(HEX_AHR, 16));
            int ahrInt = Integer.parseInt(str_binAhr, 2);
            double ahr =  ahrInt / 10000.0;
            SOH = ahr * 100/ AHR_NEW_BATTARY;
        }
        return SOH;
    }

    public static double parseSoc(String msg) {
        Log.i(TAG, "parseSoc run MSG: " + msg);
        String HEX_SOC;
        if (msg.contains(START_MSG_SOC)) {
        int startIndex = msg.indexOf(START_MSG_SOC);
        HEX_SOC = (msg.substring(startIndex + 21, startIndex + 23)) + (msg.substring(startIndex + 24, startIndex + 26)) + (msg.substring(startIndex + 27, startIndex + 29));
        String str_binSoc =  Long.toBinaryString(Long.parseLong(HEX_SOC, 16));
        SOC = (Integer.parseInt(str_binSoc, 2)) / 10000.0;
        }
        return SOC;
    }
}