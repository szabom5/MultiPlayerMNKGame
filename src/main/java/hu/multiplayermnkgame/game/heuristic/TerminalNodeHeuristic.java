package hu.multiplayermnkgame.game.heuristic;

import hu.multiplayermnkgame.game.gamerepresentation.GameState;
import hu.multiplayermnkgame.game.statespacerepresentation.State;

/**
 * A simple implementation of a heuristic function.
 * Evaluates the terminal nodes in which the given player has won to 10,
 * but all other game states to 1.
 */
public class TerminalNodeHeuristic implements Heuristic {

    @Override
    public double heuristic(GameState gs, int player) {
        State state = gs.state;
        double result = 1;
        int winner = state.somebodyWon();

        if (winner == player) {
            result = 10;
        }

        return result;
    }

    @Override
    public String name() {
        return "TerminalNode Heuristic";
    }
}
