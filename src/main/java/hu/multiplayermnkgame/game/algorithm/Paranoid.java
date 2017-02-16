package hu.multiplayermnkgame.game.algorithm;

import hu.multiplayermnkgame.game.gameplay.PlaySpace;
import hu.multiplayermnkgame.game.gamerepresentation.GameState;
import hu.multiplayermnkgame.game.gamerepresentation.Step;
import hu.multiplayermnkgame.game.heuristic.Heuristic;

public class Paranoid implements MultiPlayerAlgorithm{

    @Override
    public Step offer(GameState state, PlaySpace ps, Heuristic h) {
        double max = Integer.MIN_VALUE;
        Step bestStep = null;

        for ( Step step : ps.steps ){
            if ( step.applicable(state) ) {
                int supportedPlayer = state.player;
                double value = eval( step.apply(state), ps, h,supportedPlayer, 2);
                if ( max < value ) {
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

    private static double eval( GameState state, PlaySpace ps, Heuristic heur, int supportedPlayer, int limit ) {
        if ( state.isEnd() || limit == 0 ){
            return heur.heuristic(state,supportedPlayer);
        }

        if(supportedPlayer == state.player) {
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
        }else{
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
}
