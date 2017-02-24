package hu.multiplayermnkgame.game.algorithm;

import hu.multiplayermnkgame.game.gameplay.PlaySpace;
import hu.multiplayermnkgame.game.gamerepresentation.GameAttributes;
import hu.multiplayermnkgame.game.gamerepresentation.GameState;
import hu.multiplayermnkgame.game.gamerepresentation.Step;
import hu.multiplayermnkgame.game.heuristic.Heuristic;

import java.util.Arrays;

public class MaxN implements MultiPlayerAlgorithm {
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
        return "MaxN Algorithm";
    }

    private double[] eval(GameState state, PlaySpace ps, Heuristic heur, int limit) {
        if (state.isEnd() != 0 || limit == 0) {
            double[] result = new double[attributes.getNumberOfPlayers() + 1];
            for (int i = 1; i <= attributes.getNumberOfPlayers(); ++i) {
                result[i] = heur.heuristic(state, i);
            }

            return result;
        }

        double[] maxV = new double[attributes.getNumberOfPlayers() + 1];
        Arrays.fill(maxV, Integer.MIN_VALUE);

        for (Step step : ps.steps)
            if (step.applicable(state) && step.nextToMark(state)) {

                double[] value = eval(step.apply(state), ps, heur, limit - 1);

                /*
                //only for the optimized PatternHeuristic
                if(heur instanceof PatternHeur && limit < numberOfPlayers){
                    int[] newValues = heur.heuristic(state);
                    value[state.player] = newValues[state.player];
                   // System.out.println("new value: "+newValues[state.player]);
                }*/

                if (maxV[state.player] < value[state.player]) {
                    maxV = value;
                }
            }

        return maxV;
    }

    @Override
    public double[][] getDetails() {
        return details;
    }
}