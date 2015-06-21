package svoyaigra;

import model.Player;
import model.*;
import model.Package;

public interface GameSession {
    void startGame();
    void stopGame();
    int getTime();
    int getTimerType();
    void answerTime();
    Question getCurrentQuestion();
    Package getPackage();
    Theme getCurrentTheme();
    Player getPlayer();
    boolean checkAnswer(String answer) throws GameStoppedException;
    String getRightAnswer();
    int getPoints();
    void switchQuestion();
}