package hu.multiplayermnkgame.game.gamerepresentation;

import hu.multiplayermnkgame.game.statespacerepresentation.Operator;

public class Step {
    private final GameAttributes attributes;
    // the operator to use, which contains the numberOfPlayers already
    public Operator operator;
    // the players who is placing a mark on the table
    public int player;

    public Step(GameAttributes attributes) {
        this.attributes = attributes;
        operator = new Operator(-1, -1, 0, attributes);
        player = 0;
    }

    public Step(Operator operator, int player, GameAttributes attributes) {
        this.attributes = attributes;
        this.operator = operator;
        this.player = player;
    }

    public boolean applicable(GameState gameState) {
        return operator.applicable(gameState.state);
    }

    public boolean nextToMark(GameState gameState) {
        return operator.isNextToMark(gameState.state);
    }

    public GameState apply(GameState oldState) {
        int nextPlayer = (this.player == attributes.getNumberOfPlayers()) ? 1 : (this.player + 1);
        return new GameState(operator.apply(oldState.state), nextPlayer, this);
    }

    public int getX() {
        return operator.x;
    }

    public int getY() {
        return operator.y;
    }

    @Override
    public String toString() {
        return "step= (" + operator.toString() + ")";
    }
}
