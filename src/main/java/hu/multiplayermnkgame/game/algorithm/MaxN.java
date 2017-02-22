package hu.multiplayermnkgame.game.algorithm;

import hu.multiplayermnkgame.game.gameplay.PlaySpace;
import hu.multiplayermnkgame.game.gamerepresentation.GameState;
import hu.multiplayermnkgame.game.gamerepresentation.Step;
import hu.multiplayermnkgame.game.heuristic.Heuristic;

import java.util.Arrays;

import static hu.multiplayermnkgame.game.gameplay.GameLoop.*;

public class MaxN implements MultiPlayerAlgorithm{

    @Override
    public Step offer(GameState state, PlaySpace ps, Heuristic h) {
        double max = Integer.MIN_VALUE;
        Step bestStep = null;

        for ( Step step : ps.steps ){
            if ( step.applicable(state) && step.nextToMark(state)) {

               // System.out.println("step : "+step.toString());

                int supportedPlayer = state.player;
                double[] value = eval( step.apply(state), ps, h, 3);

                if ( max < value[supportedPlayer] ) {
                    max = value[supportedPlayer];
                    bestStep = step;
                }
                if(logging){
                    System.out.println("Offered step: ");
                    System.out.println(step.apply(state).toString());
                    System.out.println("result["+value[supportedPlayer]+"]");
                }
            }
        }

        return bestStep;
    }

    @Override
    public String name() {
        return "MaxN Algorithm";
    }

    private static double[] eval( GameState state, PlaySpace ps, Heuristic heur, int limit ) {
        if ( state.isEnd() || limit == 0 ){
            double[] result = new double[numberOfPlayers +1];
            for(int i = 1; i <= numberOfPlayers; ++i){
                result[i] = heur.heuristic(state,i);
            }

            return result;
        }

        double[] maxV = new double[numberOfPlayers +1];
        Arrays.fill(maxV,Integer.MIN_VALUE);

        for ( Step step : ps.steps )
            if ( step.applicable(state)  && step.nextToMark(state)) {

                double[] value = eval( step.apply(state), ps, heur, limit-1 );

                /*
                //only for the optimized PatternHeuristic
                if(heur instanceof PatternHeur && limit < numberOfPlayers){
                    int[] newValues = heur.heuristic(state);
                    value[state.player] = newValues[state.player];
                   // System.out.println("new value: "+newValues[state.player]);
                }*/

                if ( maxV[state.player] < value[state.player] ) {
                    maxV = value;
                }
            }

        return maxV;
    }
}