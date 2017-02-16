package hu.multiplayermnkgame.game.heuristic;

import hu.multiplayermnkgame.game.gamerepresentation.GameState;

/**
 * Interface of a general heuristic function for the multi-player m,n,k-game
 */
public interface Heuristic {

    /**
     * The actual heuristic function, which evaluates a given state of the game and calculates
     * a value for the given "supported player".
     * @param gameState the actual state of the game to evaluate
     * @param player the player whoose score is calculated
     * @return the calculated value
     */
    double heuristic(GameState gameState, int player);

    /**
     * Returns the name of the heuristic function, used in logging.
     * @return
     */
    String name();
}
