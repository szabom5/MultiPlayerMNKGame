package hu.multiplayermnkgame.game.heuristic;

import hu.multiplayermnkgame.game.gamerepresentation.GameState;

import java.util.concurrent.ThreadLocalRandom;

public class RandomHeuristic implements Heuristic {
    @Override
    public double heuristic(GameState gameState, int player) {
        return ThreadLocalRandom.current().nextInt(0, 20 + 1);
    }

    @Override
    public String name() {
        return "Random Heuristic";
    }
}
