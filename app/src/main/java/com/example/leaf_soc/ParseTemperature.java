package com.example.leaf_soc;
import android.util.Log;
import java.util.Locale;

public class ParseTemperature {
    static String TAG;
    static final int VALUE_AT_TEMP_EQUAL_TO_0 = 720;
    static final String START_MSG_TEMP = "21047BB 8 10";
    static double TEMP = 0.0;

    public static double parseTemp(String msg) {
        Log.i(TAG, "parseTemp run MSG: " + msg);
        String HEX_RAW_TEMP;
        if (msg.contains(START_MSG_TEMP)) {
            int startStrIndex = msg.indexOf(START_MSG_TEMP);
            HEX_RAW_TEMP = (msg.substring(startStrIndex + 22, startStrIndex + 24)) + (msg.substring(startStrIndex + 25, startStrIndex + 27));
            String str_binRawTemp =  Long.toBinaryString(Long.parseLong(HEX_RAW_TEMP, 16));
            int rawTemp = Integer.parseInt(str_binRawTemp, 2);
            int offset = VALUE_AT_TEMP_EQUAL_TO_0 - rawTemp;
            TEMP = offset / 9.6;
            Log.i(TAG, "parseTemp run " + TEMP);
        }
        return TEMP;
    }
}
