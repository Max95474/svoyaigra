package logic;

import model.Question;

public interface GameSession {
    void startGame();
    void stopGame();
    int getCurrentTime();
    Question getCurrentQuestion();
}