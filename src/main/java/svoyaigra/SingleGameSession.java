package svoyaigra;

import model.Player;
import model.Package;
import model.Question;
import model.Theme;

public class SingleGameSession implements GameSession {
    private final Player player;
    private final Package pack;
    private final Timer timer;

    private int totalThemes; //set to 0 when out of questions
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

    public String getRightAnswer() {
        return pack.getThemes().get(currentTheme)
                .getQuestions().get(currentQuestion).getAnswer();
    }

    public int getPoints() {
        return player.getScore();
    }

    public int getTime() {
        return timer.getCurrentTime();
    }

    public int getTimerType() {
        return timer.getType();
    }

    public Question getCurrentQuestion() {
        return pack.getThemes().get(currentTheme)
                .getQuestions().get(currentQuestion);
    }

    public Theme getCurrentTheme() {
        return pack.getThemes().get(currentTheme);
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
            totalThemes = 0; //assumes no themes left
        }
    }
}
