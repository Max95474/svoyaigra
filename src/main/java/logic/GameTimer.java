package logic;

import java.util.*;

public class GameTimer {
    private static final int CLICK = 1;
    private static final int ANSWER = 2;
    public final int CLICK_TIME;
    public final int ANSWER_TIME;
    private Timer timer;
    private int currentTime;

    public GameTimer() {
        CLICK_TIME = 5;
        ANSWER_TIME = 30;
    }

    public GameTimer(int clickTime, int answerTime) {
        this.CLICK_TIME = clickTime;
        this.ANSWER_TIME = answerTime;
    }

    private void startTimer(int type) {
        timer = new Timer();
        final int time;
        if(type == CLICK) time = CLICK_TIME;
        else if (type == ANSWER) time = ANSWER_TIME;
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

    public void startClickTimer() {
        startTimer(CLICK);
    }

    public void startAnswerTimer() {
        startTimer(ANSWER);
    }

    public void cancelTimer() {
        timer.cancel();
        currentTime = 1;
    }

    public int getCurrentTime() {
        return currentTime;
    }
}