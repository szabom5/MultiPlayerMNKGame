package hu.multiplayermnkgame.game.gamerepresentation;

import hu.multiplayermnkgame.game.algorithm.MultiPlayerAlgorithm;
import hu.multiplayermnkgame.game.heuristic.Heuristic;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameAttributes {
    private final int M;

    private final int N;

    private final int K;

    private final int numberOfPlayers;

    private final Map<Integer, Pair<MultiPlayerAlgorithm, Heuristic>> mapOfPlayerStrategies;

    private final List<Color> playerColors;

    private final boolean logging;

    private GameAttributes(GameAttributesBuilder builder) {
        numberOfPlayers = builder.numberOfPlayers;
        M = builder.M;
        N = builder.N;
        K = builder.K;

        this.mapOfPlayerStrategies = new HashMap<>(builder.mapOfPlayerStrategies);
        this.playerColors = new ArrayList<>(builder.playerColors);
        logging = builder.logging;
    }

    public int getM() {
        return M;
    }

    public int getN() {
        return N;
    }

    public int getK() {
        return K;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Map<Integer, Pair<MultiPlayerAlgorithm, Heuristic>> getMapOfPlayerStrategies() {
        return mapOfPlayerStrategies;
    }

    public List<Color> getPlayerColors() {
        return playerColors;
    }

    public boolean isLogging() {
        return logging;
    }

    public static class GameAttributesBuilder {
        private int M;
        private int N;
        private int K;
        private int numberOfPlayers;
        private Map<Integer, Pair<MultiPlayerAlgorithm, Heuristic>> mapOfPlayerStrategies;
        private List<Color> playerColors;
        private boolean logging;

        public GameAttributesBuilder() {
        }

        public GameAttributesBuilder setNumberOfPlayers(int player) {
            this.numberOfPlayers = player;
            return this;
        }

        public GameAttributesBuilder setBoardParameters(int m, int n) {
            this.M = m;
            this.N = n;
            return this;
        }

        public GameAttributesBuilder setWinningNumber(int k) {
            this.K = k;
            return this;
        }

        public GameAttributesBuilder setLogging(boolean logging) {
            this.logging = logging;
            return this;
        }

        public GameAttributesBuilder setMapOfPlayerStrategies(Map<Integer, Pair<MultiPlayerAlgorithm, Heuristic>> strategies) {
            this.mapOfPlayerStrategies = new HashMap<>(strategies);
            return this;
        }

        public GameAttributesBuilder setPlayerColors(List<Color> playerColors) {
            this.playerColors = new ArrayList<>(playerColors);
            return this;
        }

        public GameAttributes build() {
            return new GameAttributes(this);
        }
    }
}
