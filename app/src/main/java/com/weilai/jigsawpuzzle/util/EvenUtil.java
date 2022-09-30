package com.weilai.jigsawpuzzle.util;

import org.greenrobot.eventbus.EventBus;

public class EvenUtil {
    public static void register(Object subscriber){
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            try {
                EventBus.getDefault().register(subscriber);
            } catch (Exception ignored) {
            }
        }
    }

    public static void unregister(Object subscriber){
        if (EventBus.getDefault().isRegistered(subscriber)) {
            try {
                EventBus.getDefault().unregister(subscriber);
            } catch (Exception ignored) {
            }
        }
    }

    public static void post(Object event){
        EventBus.getDefault().post(event);
    }


}
