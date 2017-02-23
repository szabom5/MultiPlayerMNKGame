package hu.multiplayermnkgame.game.gameplay;

import hu.multiplayermnkgame.game.gamerepresentation.GameAttributes;
import hu.multiplayermnkgame.game.gamerepresentation.GameState;
import hu.multiplayermnkgame.game.gamerepresentation.Step;
import hu.multiplayermnkgame.game.heuristic.Heuristic;
import hu.multiplayermnkgame.game.statespacerepresentation.Operator;

import java.util.ArrayList;

public class GameLoop {

    private final GameAttributes attributes;

    public GameLoop(GameAttributes gameAttributes) {
        this.attributes = gameAttributes;
    }

    public void loop() {
        PlaySpace playSpace = new PlaySpace(attributes);
        GameState gameState = GameState.startGameState(attributes);

        int numberOfMarks = 1;
        int player;

        while (!gameState.isEnd()) {

            System.out.println(gameState);
            player = (numberOfMarks - 1) % attributes.getNumberOfPlayers() + 1;

            final long startTime = System.currentTimeMillis();

            Step bestStep;

            if (numberOfMarks == 1) {
                bestStep = new Step(new Operator(attributes.getN() / 2 + 1, attributes.getM() / 2 + 1, 1, attributes), 1, attributes);
            } else {
                bestStep = attributes.getMapOfPlayerStrategies().get(player).getKey()
                        .offer(gameState, playSpace, attributes.getMapOfPlayerStrategies().get(player).getValue());
            }

            System.out.println("player = " + player);

            if (isPlayerNotAI(player)) {
                ArrayList<Step> ops = new ArrayList<>();
                for (Step op : playSpace.steps) {
                    if (op.applicable(gameState)) {
                        ops.add(op);
                    }
                }

                int next = 0;

                gameState = ops.get(next).apply(gameState);
            } else {
                System.out.println(bestStep);
                gameState = bestStep.apply(gameState);
            }

            System.out.println("*****************\n");

            final long duration = System.currentTimeMillis() - startTime;
            System.out.println("duration: " + duration);

            numberOfMarks++;
        }

        System.out.println(gameState);
    }

    private boolean isPlayerNotAI(int player) {
        Heuristic heuristic = attributes.getMapOfPlayerStrategies().get(player).getValue();

        return heuristic == null;
    }
}
