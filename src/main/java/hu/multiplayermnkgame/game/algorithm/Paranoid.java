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

        this.details = new double[attributes.getM() + 1][attributes.getN() + 1];

        for (Step step : ps.steps) {
            if (step.applicable(state)) {
                int supportedPlayer = state.player;
                double value = eval(step.apply(state), ps, h, supportedPlayer, 3);

                details[step.getX()][step.getY()] = value;

                if (max < value) {
                    max = value;
                    bestStep = step;
                }
            }
        }
        return bestStep;
    }

    @Override
    public String name() {
        return "Paranoid Algorithm";
    }

    private double eval(GameState state, PlaySpace ps, Heuristic heur, int supportedPlayer, int limit) {
        if (state.isEnd() != 0 || limit == 0) {
            return heur.heuristic(state, supportedPlayer);
        }

        if (supportedPlayer == state.player) {
            double max = Integer.MIN_VALUE;
            for (Step step : ps.steps) {
                if (step.applicable(state)) {
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
                if (step.applicable(state)) {
                    double value = eval(step.apply(state), ps, heur, supportedPlayer, limit - 1);
                    if (min > value) {
                        min = value;
                    }
                }
            }
            return min;
        }
    }

    @Override
    public double[][] getDetails() {
        return details;
    }
}
