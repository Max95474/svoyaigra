package svoyaigra;

import logic.GameSession;
import logic.Player;
import model.Package;
import model.Question;

public class SingleGameSession implements GameSession {
    private final Player player;
    private final Package pack;
    private final Timer timer;

    private int totalThemes;
    private int currentTheme;
    private int currentQuestion;

    private boolean gameStared;

    public SingleGameSession(Player player, Package pack) {
        this.player = player;
        this.pack = pack;
        timer = new Timer();
        totalThemes = pack.size();
        currentTheme = 0;
        currentQuestion = 0;
        gameStared = false;
    }

    public void startGame() {
        gameStared = true;
        timer.startTimer(Timer.TYPE_THINK);
    }

    public void stopGame() {
        gameStared = false;
        timer.stopTimer();
    }

    public void answerTime() {
        timer.stopTimer();
        timer.startTimer(Timer.TYPE_ANSWER);
    }

    public boolean checkAnswer(String answer) throws GameStoppedException {
        if(!gameStared) throw new GameStoppedException("Game not started");
        //TODO check answer with Lucene
        Question question = pack.getThemes().get(currentTheme)
                .getQuestions().get(currentQuestion);
        if(answer.equals(question.getAnswer().trim())) {
            switchQuestion();
            player.addPoints(question.getPoints());
            timer.stopTimer();
            timer.startTimer(Timer.TYPE_THINK);
            return true;
        }
        switchQuestion();
        player.subtractPoints(question.getPoints());
        timer.stopTimer();
        timer.startTimer(Timer.TYPE_THINK);
        return false;
    }

    public int getCurrentTime() {
        return timer.getCurrentTime();
    }

    public Question getCurrentQuestion() {
        return pack.getThemes().get(currentTheme)
                .getQuestions().get(currentQuestion);
    }

    public Player getPlayer() {
        return player;
    }

    public Package getPackage() {
        return pack;
    }

    private void switchQuestion() {
        if(currentTheme < totalThemes) {
            if(currentQuestion < 4) {
                currentQuestion++;
            } else {
                currentTheme++;
                currentQuestion = 0;
            }
        } else {
            totalThemes = 0; //means: no themes left
        }
    }
}
