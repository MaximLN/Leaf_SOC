package com.example.leaf_soc;
import android.util.TypedValue;

public class DisplayBuilder {
    public static void setSize (){
        double progressBarSocWidth = MainActivity.DISPLAY_WIDTH/1.695;
        int size260px = (int) (MainActivity.DISPLAY_HEIGHT/2.30);
        MainActivity.progressBarSoc.getLayoutParams().width = ((int)progressBarSocWidth);
        MainActivity.progressBarSoh.getLayoutParams().width = size260px;
        MainActivity.progressBarSoh.getLayoutParams().height = (size260px);
        MainActivity.progressBarTempAvg.getLayoutParams().width = (size260px);
        MainActivity.progressBarTempAvg.getLayoutParams().height = (size260px);
        MainActivity.progressBarClimatePower.getLayoutParams().width = (size260px);
        MainActivity.progressBarClimatePower.getLayoutParams().height = (size260px);
        MainActivity.textViewTempAvg.getLayoutParams().width = (size260px);
        MainActivity.textViewTempAvg.getLayoutParams().height = (size260px);
        MainActivity.textViewTempAvg.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/8);
        MainActivity.textViewSoh.getLayoutParams().width = (size260px);
        MainActivity.textViewSoh.getLayoutParams().height = (size260px);
        MainActivity.textViewSoh.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/8);
        MainActivity.textViewClimatePower.getLayoutParams().width = (size260px);
        MainActivity.textViewClimatePower.getLayoutParams().height = (size260px);
        MainActivity.textViewClimatePower.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/8);

        MainActivity.textViewTempPercent.getLayoutParams().width = (size260px);
        MainActivity.textViewTempPercent.getLayoutParams().height = ((int)(size260px * 1.5));
        MainActivity.textViewClimatePowerPercent.getLayoutParams().width = (size260px);
        MainActivity.textViewClimatePowerPercent.getLayoutParams().height = ((int)(size260px * 1.5));
        MainActivity.textViewSohPercent.getLayoutParams().width = (size260px);
        MainActivity.textViewSohPercent.getLayoutParams().height = ((int)(size260px / 2));
        MainActivity.textViewTempPercent.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/15);
        MainActivity.textViewClimatePowerPercent.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/15);
        MainActivity.textViewSohPercent.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/15);
        MainActivity.textViewSohDescription.getLayoutParams().width = (size260px);
        MainActivity.textViewSohDescription.getLayoutParams().height = ((int)(size260px * 1.5));
        MainActivity.textViewTempAvgDescription.getLayoutParams().width = (size260px);
        MainActivity.textViewTempAvgDescription.getLayoutParams().height = ((int)(size260px / 2));
        MainActivity.textViewClimatePowerDescription.getLayoutParams().width = (size260px);
        MainActivity.textViewClimatePowerDescription.getLayoutParams().height = ((int)(size260px / 2));
        MainActivity.textViewSohDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/15);
        MainActivity.textViewTempAvgDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/15);
        MainActivity.textViewClimatePowerDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/15);

        MainActivity.textViewSocDescription.getLayoutParams().width = (MainActivity.DISPLAY_WIDTH);
        MainActivity.textViewSocDescription.getLayoutParams().height = ((int)(size260px/1.2));
        MainActivity.textViewSocDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/10);

        MainActivity.textViewSoc.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/2);
        MainActivity.textViewSocPercent.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/10);
        MainActivity.textViewSocPercent.getLayoutParams().height = (int) (size260px / 1.5);
    }
}
