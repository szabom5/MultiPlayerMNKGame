package hu.multiplayermnkgame.game.statespacerepresentation;

import hu.multiplayermnkgame.game.gamerepresentation.GameAttributes;

import java.util.List;

public class Operator {
    private final GameAttributes attributes;

    // the position of the cell on the table to place a mark on
    public int x, y;
    // the mark which is placed on the table by this operator
    public int mark;

    /**
     * Constructs a specific operator.
     *
     * @param x          the x position of the cell on the table to place a mark on
     * @param y          the y position of the cell on the table to place a mark on
     * @param mark       the mark which is placed on the table by this operator
     * @param attributes the attributes that specify the game
     */
    public Operator(int x, int y, int mark, GameAttributes attributes) {
        this.x = x;
        this.y = y;
        this.mark = mark;
        this.attributes = attributes;
    }

    public boolean applicable(State state) {
        int numAllMarks = state.countAllMarks();

        return (numAllMarks % attributes.getNumberOfPlayers() == mark - 1) && (state.a[x][y] == 0);
    }

    public boolean isNextToMark(State state) {
        List<Integer> list = state.neighbours(x, y);
        Integer sum = 0;
        for (Integer i : list) {
            sum += i;
        }

        return sum != 0;
    }

    public State apply(State oldState) {
        State newState = new State(attributes);
        for (int i = 1; i <= attributes.getN(); i++)
            for (int j = 1; j <= attributes.getM(); j++)
                newState.a[i][j] = (i == x && j == y) ? mark : oldState.a[i][j];
        return newState;
    }

    @Override
    public String toString() {
        return x + "," + y + " ";
    }

}
