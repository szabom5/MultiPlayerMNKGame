package hu.multiplayermnkgame.game.gamerepresentation;

import hu.multiplayermnkgame.game.statespacerepresentation.State;

public class GameState {
    public State state;
    public int player;

    //extra information
    public Step lastStep;

    public GameState(State state, int player, Step lastStep) {
        this.state = state;
        this.player = player;
        this.lastStep = lastStep;
    }

    //the start of the game: empty table, the first player, (-1;-1) step
    public static GameState startGameState(GameAttributes attributes) {
        return new GameState(new State(attributes), 1, new Step(attributes));
    }

    public boolean isEnd() {
        return state.isEnd(lastStep.getX(), lastStep.getY()) != 0;
    }

    @Override
    public String toString() {
        if (lastStep != null && isEnd()) {
            String result = state.toString() + "\n End ";
            if (state.isNotDeal(lastStep.getX(), lastStep.getY()))
                result += player == 1 ? lastStep.player : (player - 1) + " win.";
            else
                result += "DEAL";
            return result;
        } else {
            return state.toString();
        }
    }
}
