package hu.multiplayermnkgame.game.gamerepresentation;

import hu.multiplayermnkgame.game.algorithm.MultiPlayerAlgorithm;
import hu.multiplayermnkgame.game.heuristic.Heuristic;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class GameAttributes {
    private final int N;

    private final int M;

    private final int K;

    private final int numberOfPlayers;

    private final Map<Integer, Pair<MultiPlayerAlgorithm, Heuristic>> mapOfPlayerStrategies;

    private final boolean logging;

    private GameAttributes(GameAttributesBuilder builder) {
        numberOfPlayers = builder.numberOfPlayers;
        N = builder.N;
        M = builder.M;
        K = builder.K;

        this.mapOfPlayerStrategies = new HashMap<>(builder.mapOfPlayerStrategies);
        logging = builder.logging;
    }

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
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

    public boolean isLogging() {
        return logging;
    }

    public static class GameAttributesBuilder {
        private int N;
        private int M;
        private int K;
        private int numberOfPlayers;
        private Map<Integer, Pair<MultiPlayerAlgorithm, Heuristic>> mapOfPlayerStrategies;
        private boolean logging;

        public GameAttributesBuilder() {
        }

        public GameAttributesBuilder setNumberOfPlayers(int player) {
            this.numberOfPlayers = player;
            return this;
        }

        public GameAttributesBuilder setBoardParameters(int n, int m) {
            this.N = n;
            this.M = m;
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

        public GameAttributes build() {
            return new GameAttributes(this);
        }
    }
}
