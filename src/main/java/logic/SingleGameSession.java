package logic;

import service.QuestionService;
import model.Question;
import model.Package;

public class SingleGameSession implements GameSession {
    private Package pack = QuestionService.getInstance().getTestPackage();
    private GameTimer timer = new GameTimer();
    private Player player;

    private int totalThemes = pack.getThemes().size();
    private int currentThemeNumber = 0;
    private int currentQuestionNumber = 0;
    private Question currentQuestion;
    private TimerStatus timerStatus;

    public SingleGameSession(Player player) {
        this.player = player;
        timerStatus = TimerStatus.ALIVE;
    }

    @Override
    public void startGame() {
        if(currentThemeNumber < totalThemes) {
            currentQuestion = pack
                    .getThemes().get(currentThemeNumber)
                    .getQuestions().get(currentQuestionNumber);
        }
        timer.startClickTimer();
    }

    @Override
    public int endGame() {
        return 0;
    }

    public void buttonClicked() {
        timer.cancelTimer();
        timerStatus = TimerStatus.STOPPED_BY_BUTTON;
    }

    @Override
    public boolean checkAnswer(String answer) {
        System.out.println(answer + " - "+ currentQuestion.getAnswer());
        boolean result = answer.trim().replace(".", "").equalsIgnoreCase(currentQuestion.getAnswer().trim().replace(".", ""));
        if(result) {
            player.addPoints(currentQuestion.getPoints());
        } else {
            player.subtractPoints(currentQuestion.getPoints());
        }
        timer.cancelTimer();
        timerStatus = TimerStatus.ALIVE;
        timer.startClickTimer();
        return result;
    }

    @Override
    public int getTime() {
        if(timer.getCurrentTime() >= timer.CLICK_TIME) {
            timer.cancelTimer();
            timerStatus = TimerStatus.STOPPED_BY_TIMER;
            switchToNextQuestion();
            return 5;
        }
        return timer.getCurrentTime();
    }

    @Override
    public Question getCurrentQuestion() {
        timer.cancelTimer();
        timerStatus = TimerStatus.ALIVE;
        timer.startClickTimer();
        return currentQuestion;
    }

    @Override
    public String getCurrentPackage() {
        return pack.getName();
    }

    public String getCurrentTheme() {
        return pack.getThemes().get(currentThemeNumber).getThemeTitle();
    }

    @Override
    public TimerStatus getTimerStatus() {
        return timerStatus;
    }

    private void switchToNextQuestion(){ //throws OutOfQuestionsException {
        if(currentThemeNumber < totalThemes - 1) {//<- can't get last theme
            if(currentQuestionNumber < pack.getThemes().get(currentThemeNumber).size() - 1) {
                currentQuestionNumber++;
            } else {
                currentThemeNumber++;
                currentQuestionNumber = 0;
            }
            currentQuestion = pack
                    .getThemes().get(currentThemeNumber)
                    .getQuestions().get(currentQuestionNumber);
        } else {
            currentThemeNumber = 0;
            currentQuestionNumber = 0;
            //throw new OutOfQuestionsException("There is no more questions in package");
        }
    }

    public static void main(String[] args) throws OutOfQuestionsException {
        /*SingleGameSession gs = new SingleGameSession(new Player("Player_1"));
        System.out.println("Total themes = " + gs.totalThemes);
        gs.startGame();
        for(int i = 0; i < 1000; i++) {
            System.out.println("________________________________");
            System.out.println("current theme = " + gs.currentThemeNumber);
            System.out.println("current question = " + gs.currentQuestionNumber);
            System.out.println("Theme: " + gs.pack.getThemes().get(gs.currentThemeNumber).getThemeTitle());
            System.out.println("Question: " + gs.getCurrentQuestion().getQuestion());
            System.out.println("Answer: " + gs.getCurrentQuestion().getAnswer());
            System.out.println("Points: " + gs.getCurrentQuestion().getPoints());
            gs.switchToNextQuestion();
        }
        for(int i = 0; i < gs.pack.getThemes().size(); i++) {
            System.out.println(gs.pack.getThemes().get(gs.currentThemeNumber).getThemeTitle());
            System.out.println(gs.pack.getThemes().get(gs.currentThemeNumber).size());
            for(int j = 0; j < 5; j++) {
                gs.switchToNextQuestion();
            }
        }*/
    }
}
