package hu.multiplayermnkgame.game.algorithm;

import hu.multiplayermnkgame.game.gameplay.PlaySpace;
import hu.multiplayermnkgame.game.gamerepresentation.GameState;
import hu.multiplayermnkgame.game.gamerepresentation.Step;
import hu.multiplayermnkgame.game.heuristic.Heuristic;

public interface MultiPlayerAlgorithm {

    Step offer(GameState state, PlaySpace ps, Heuristic h);

    double[][] getDetails();

    String name();
}
