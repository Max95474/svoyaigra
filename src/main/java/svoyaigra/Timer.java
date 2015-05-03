package svoyaigra;

import java.util.*;

public class Timer {
    public static final int TYPE_STOPPED = 0;
    public static final int TYPE_THINK  = 1;
    public static final int TYPE_ANSWER = 2;
    public final int CLICK_TIME;
    public final int ANSWER_TIME;
    private java.util.Timer timer;
    private int currentTime = 0;
    private int currentType;

    public Timer() {
        CLICK_TIME = 5;
        ANSWER_TIME = 7;
    }

    public Timer(int clickTime, int answerTime) {
        this.CLICK_TIME = clickTime;
        this.ANSWER_TIME = answerTime;
    }

    public void startTimer(int type) {
        currentType = type;
        timer = new java.util.Timer();
        final int time;
        if(type == TYPE_THINK) time = CLICK_TIME;
        else if (type == TYPE_ANSWER) time = ANSWER_TIME;
        else return;
        currentTime = 1;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(currentTime < time) {
                    currentTime++;
                } else {
                    timer.cancel();
                    currentTime = 1;
                }
            }
        }, 1000, 1000);
    }

    public void stopTimer() {
        currentType = TYPE_STOPPED;
        if(timer == null) {
            currentTime = 0;
            return;
        }
        timer.cancel();
        currentTime = 0;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public int getType() {
        return currentType;
    }
}