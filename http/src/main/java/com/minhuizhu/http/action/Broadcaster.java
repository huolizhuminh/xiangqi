package com.minhuizhu.http.action;

import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.Log;


import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Rex on 2016/7/17.
 */
public class Broadcaster {

    private final Handler handler;
    private static final Broadcaster broadcaster = new Broadcaster();
    private final Map<String, LinkedList<Receiver>> receiverMap = new ArrayMap<>(64);
    private final Map<String, LinkedList<EventReceiver>> eventReceiverMap = new ArrayMap<>(64);

    public static Broadcaster getInstance() {
        return broadcaster;
    }

    private Broadcaster() {
        handler = new Handler(Looper.getMainLooper());
    }

    public boolean hasReceiver(String key) {
        List<Receiver> receivers = receiverMap.get(key);
        if (receivers == null || receivers.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void registerEvent(String event, EventReceiver receiver) {
        if (eventReceiverMap.get(event) == null) {
            eventReceiverMap.put(event, new LinkedList<EventReceiver>());
        }
        if (!eventReceiverMap.get(event).contains(receiver)) {
            eventReceiverMap.get(event).add(receiver);
        }
    }

    public void unregisterEvent(String event, EventReceiver receiver) {
        LinkedList<EventReceiver> eventReceivers = eventReceiverMap.get(event);
        if (eventReceivers != null && eventReceivers.contains(receiver)) {
            eventReceivers.remove(receiver);
        }
    }

    public void registerReceiver(String key, Receiver receiver) {
        if (receiverMap.get(key) == null) {
            receiverMap.put(key, new LinkedList<Receiver>());
        }
        if (!receiverMap.get(key).contains(receiver)) {
            receiverMap.get(key).add(receiver);
        }
    }

    public void notifyEvent(String event) {
        List<EventReceiver> receivers = eventReceiverMap.get(event);
        if (receivers == null || receivers.isEmpty()) {
            return;
        }
        List<EventReceiver> receiverList = new ArrayList<>(receivers);
        for (final EventReceiver receiver : receiverList) {
            receiver.onReceive(event);
        }
    }

    public void unregisterReceiver(String key, Receiver receiver) {
        receiverMap.get(key).remove(receiver);
    }


    public <T> void postData(final Receiver<T> receiver, final T data) {
        if (receiver == null) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    receiver.onReceive(data);
                } catch (Exception e) {
                    Logger.e("when posting data -> " + Log.getStackTraceString(e));
                }
                handler.removeCallbacks(this);
            }
        });
    }

}
