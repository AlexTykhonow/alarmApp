package com.ora.alarmapp.util;

import android.view.animation.AlphaAnimation;

public class animation {
    public static void startAnimationFrame(){
        AlphaAnimation outAnimation;

        outAnimation = new AlphaAnimation(1f,1f);
        outAnimation.setDuration(30000);

    }
    public static void stopAnimationFrame(){
        AlphaAnimation outAnimation;

        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(50);
    }
}
