package hu.multiplayermnkgame.game.algorithm;

import hu.multiplayermnkgame.game.gameplay.PlaySpace;
import hu.multiplayermnkgame.game.gamerepresentation.GameAttributes;
import hu.multiplayermnkgame.game.gamerepresentation.GameState;
import hu.multiplayermnkgame.game.gamerepresentation.Step;
import hu.multiplayermnkgame.game.heuristic.Heuristic;

public class Paranoid implements MultiPlayerAlgorithm {
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

        double value = 0.0;

        for (Step step : ps.steps) {
            if (step.applicable(state) && step.nextToMark(state)) {
                int supportedPlayer = state.player;

                value = eval(step.apply(state), ps, h, supportedPlayer, 3);

                details[step.getX()][step.getY()] = value;

                if (max < value) {
                    max = value;
                    bestStep = step;
                }
            }
        }

        return bestStep;
    }

    private double eval(GameState state, PlaySpace ps, Heuristic heur, int supportedPlayer, int limit) {
        if (state.isEnd() != 0 || limit == 0) {
            return heur.heuristic(state, supportedPlayer);
        }

        if (supportedPlayer == state.player) {
            double max = Integer.MIN_VALUE;
            for (Step step : ps.steps) {
                if (step.applicable(state) && step.nextToMark(state)) {
                    double value = eval(step.apply(state), ps, heur, supportedPlayer, limit - 1);
                    if (max < value) {
                        max = value;
                    }
                }
            }
            return max;
        } else {
            double min = Integer.MAX_VALUE;
            for (Step step : ps.steps) {
                if (step.applicable(state) && step.nextToMark(state)) {
                    double value = eval(step.apply(state), ps, heur, supportedPlayer, limit - 1);
                    if (min > value) {
                        min = value;
                    }
                }
            }
            return min;
        }
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
    public String name() {
        return "Paranoid Algorithm";
    }

    @Override
    public double[][] getDetails() {
        return details;
    }
}
