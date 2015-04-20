//package logic;
//
//import svoyaigra.GameSession;
//import svoyaigra.SingleGameSession;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class Logic {
//    private static final short DEFAULT_PLAYER_AMOUNT = 3;
//    private short playerCounter = 0;
//    private Map<String, Player> playerMap = new HashMap<>();
//    private Map<String, GameSession> sessionMap = new HashMap<>();
//
//    public void addPlayer(String sessionId, String playerName, boolean isSingle) {
//        Player player = new Player(playerName);
//        System.err.println(sessionId + " - " +player.getName());
//        playerMap.put(sessionId, player);
//        if(isSingle) {
//            SingleGameSession gameSession = new SingleGameSession(player);
//            sessionMap.put(sessionId, gameSession);
//            gameSession.startGame();
//        }
//    }
//
//    public boolean checkAnswer(String sessionId, String answer) {
//        return sessionMap.get(sessionId).checkAnswer(answer);
//    }
//
//    public int getTime(String sessionId) {
//        return sessionMap.get(sessionId).getTime();
//    }
//
//    /*
//    * ALIVE    = 0
//    * STOPPED  = 1
//    * TIME_OUT = 2
//    */
//    public int getTimerStatus(String sessionId) {
//        return  sessionMap.get(sessionId).getTimerStatus().ordinal();
//    }
//
//    public String getRightAnswer(String sessionId) {
//        return sessionMap.get(sessionId).getCurrentQuestion().getAnswer();
//    }
//
//    public int endGame(String sessionId) {
//        return sessionMap.get(sessionId).endGame();
//    }
//
//    public String getQuestion(String sessionId) {
//        return sessionMap.get(sessionId).getCurrentQuestion().getQuestion();
//    }
//
//    public String getPackageName(String sessionId) {
//        return sessionMap.get(sessionId).getCurrentPackage();
//    }
//
//    public String getTheme(String sessionId) {
//        return sessionMap.get(sessionId).getCurrentTheme();
//    }
//
//    public String getPoints(String sessionId) {
//        return Integer.toString(sessionMap.get(sessionId).getCurrentQuestion().getPoints());
//    }
//
//    public int getUserPoints(String sessionId) {
//        return playerMap.get(sessionId).getScore();
//    }
//
//    public void rbClick(String sessionId) {
//        ((SingleGameSession)sessionMap.get(sessionId)).buttonClicked();
//    }
//}