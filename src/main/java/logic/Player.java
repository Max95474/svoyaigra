package logic;

public class Player {
    private String name;
    private int score;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addPoints(int point) {
        score += point;
    }

    public void subtractPoints(int point) {
        score -= point;
    }
}