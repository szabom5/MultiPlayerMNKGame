package hu.multiplayermnkgame.game.algorithm;

import hu.multiplayermnkgame.game.gameplay.PlaySpace;
import hu.multiplayermnkgame.game.gamerepresentation.GameAttributes;
import hu.multiplayermnkgame.game.gamerepresentation.GameState;
import hu.multiplayermnkgame.game.gamerepresentation.Step;
import hu.multiplayermnkgame.game.heuristic.Heuristic;

import java.util.Arrays;

public class MaxN0 implements MultiPlayerAlgorithm {
    private GameAttributes attributes;

    private double[][] details;

    public void initialize(GameAttributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public Step offer(GameState state, PlaySpace ps, Heuristic h) {
        double max = Integer.MIN_VALUE;
        Step bestStep = null;

        initDetails();

        for (Step step : ps.steps) {
            if (step.applicable(state) && step.nextToMark(state)) {
                int supportedPlayer = state.player;

                double[] value = eval(step.apply(state), ps, h, 0);

                details[step.getX()][step.getY()] = value[supportedPlayer];

                if (max < value[supportedPlayer]) {
                    max = value[supportedPlayer];
                    bestStep = step;
                }
            }
        }

        return bestStep;
    }

    @Override
    public String name() {
        return "MaxN0 Algorithm";
    }

    private double[] eval(GameState state, PlaySpace ps, Heuristic heur, int limit) {
        if (state.isEnd() != 0 || limit == 0) {
            double[] result = new double[attributes.getNumberOfPlayers() + 1];

            for (int i = 1; i <= attributes.getNumberOfPlayers(); ++i) {
                result[i] += heur.heuristic(state, i);

                for (int j = 1; j <= attributes.getNumberOfPlayers(); ++j) {
                    if (j != i) {
                        result[j] -= result[i];
                    }
                }
            }

            return result;
        }

        double[] maxV = new double[attributes.getNumberOfPlayers() + 1];
        Arrays.fill(maxV, Integer.MIN_VALUE);

        for (Step step : ps.steps)
            if (step.applicable(state) && step.nextToMark(state)) {

                double[] value = eval(step.apply(state), ps, heur, limit - 1);

                if (maxV[state.player] < value[state.player]) {
                    maxV = value;
                }
            }

        return maxV;
    }

    private void initDetails() {
        details = new double[attributes.getM() + 1][attributes.getN() + 1];
        for (int i = 0; i < details.length; ++i) {
            for (int j = 0; j < details[0].length; ++j) {
                details[i][j] = 0.1;
            }
        }
    }

    @Override
    public double[][] getDetails() {
        return details;
    }
}