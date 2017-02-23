package hu.multiplayermnkgame.game.gameplay;

import hu.multiplayermnkgame.game.gamerepresentation.GameAttributes;
import hu.multiplayermnkgame.game.gamerepresentation.Step;
import hu.multiplayermnkgame.game.heuristic.RulesHeuristic;
import hu.multiplayermnkgame.game.statespacerepresentation.Operator;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaySpace {
    private final GameAttributes gameAttributes;

    public final List<Step> steps;

    public PlaySpace(GameAttributes gameAttributes) {
        this.gameAttributes = gameAttributes;

        steps = new ArrayList<>();

        initializeSteps();

        initializeHeuristics();
    }

    private void initializeHeuristics() {
        for (Map.Entry entry : gameAttributes.getMapOfPlayerStrategies().entrySet()) {
            Pair pair = (Pair) entry.getValue();
            if (pair.getValue() instanceof RulesHeuristic) {
                System.out.println("********** attributes:" + gameAttributes.getNumberOfPlayers());
                ((RulesHeuristic) pair.getValue()).initialize(gameAttributes);
            }
        }
    }

    private void initializeSteps() {
        for (int i = 1; i <= gameAttributes.getN(); i++) {
            for (int j = 1; j <= gameAttributes.getM(); j++) {
                for (int p = 1; p <= gameAttributes.getNumberOfPlayers(); p++)
                    steps.add(new Step(new Operator(i, j, p, gameAttributes), p, gameAttributes));
            }
        }
    }
}
