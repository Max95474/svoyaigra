package svoyaigra;

import model.Player;
import model.Game;
import model.Tour;
import service.QuestionService;

import java.util.*;

public class SvoyaIgraLogic {
    private final static int NUMBER_OF_PLAYERS = 3;
    public final static int SINGLE = 1;
    public final static int MULTIPLAYER = 2;
    private Map<String, GameSession> gamesMap = new HashMap<>();
    private Map<String, Player> playerMap = new HashMap<>();
    private List<Player> playerQueue = new LinkedList<>();

    private QuestionService questionService = QuestionService.getInstance();
    private final Game game = questionService.getGamesList().get(13);
    private int playerCount = 0;

    public void addPlayer(String sessionId, String playerName, int type,
                          int packageNumber/*negative if random*/) {
        Player player = new Player(playerName);
        Tour tour;
        playerMap.put(sessionId, player);
        if(SINGLE == type) {
            if(packageNumber < 0) {
                int n = new Random().nextInt(questionService.getToursList(game).size());
                tour = questionService.getToursList(game).get(n);
            } else {
                tour = questionService.getToursList(game).get(packageNumber);
            }
            GameSession sg = new SingleGameSession(player, questionService.getPackage(game, tour));
        } else if (MULTIPLAYER == type){
            if(playerCount < 2) {
                playerQueue.add(new Player(playerName));
                playerCount++;
            } else {
                //start multi player
                playerCount = 0;
            }
        }
    }

    public GameSession getGameSession(String sessionId) {
        return gamesMap.get(sessionId);
    }
}