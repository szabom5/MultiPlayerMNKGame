package hu.multiplayermnkgame.game.statespacerepresentation;

import hu.multiplayermnkgame.game.gamerepresentation.GameAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State {
    private final GameAttributes attributes;

    public int[][] a;

    /**
     * Constructs the initial state of the table
     */
    public State(GameAttributes attributes) {
        this.attributes = attributes;
        // initial value 0 by default
        a = new int[attributes.getM() + 1][attributes.getN() + 1];
    }

    List<Integer> neighbours(int x, int y) {
        // taking advantage of the fact, that at the 0 indices the a array contains 0 values also.
        List<Integer> list = new ArrayList<>();
        list.add(a[x - 1][y - 1]);  //NorthWest
        list.add(a[x - 1][y]);    //North
        list.add(a[x][y - 1]);    //West
        if (y + 1 <= attributes.getN()) {
            list.add(a[x - 1][y + 1]);  //NorthEast
            list.add(a[x][y + 1]);    //East
        }
        if (x + 1 <= attributes.getM()) {
            list.add(a[x + 1][y - 1]);  //SouthWest
            list.add(a[x + 1][y]);    //South
        }
        if (x + 1 <= attributes.getM() && y + 1 <= attributes.getN()) {
            list.add(a[x + 1][y + 1]);
        }
        return list;
    }

    /**
     * Checks if the state is a goal state
     *
     * @param x the x coordinate of the last move
     * @param y the y coordinate of the last move
     * @return the result of the check: 1,2,...numberOfPlayers if somebody won by K marks; -1 if the table is full and nobody won; 0 otherwise
     */
    public int isEnd(int x, int y) {
        // only before first step
        if (x == -1 || y == -1) {
            return 0;
        }
        //if somebody won
        int win = isEndOnStraightLines(x, y);
        if (win != 0) {
            return win;
        } else {
            win = isEndOnDiagonals(x, y);
        }
        // the table can be full and somebody still win with the last step
        if (win != 0) {
            return win;
        } else if (countAllMarks() == attributes.getM() * attributes.getN()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Checks if K marks are in a row, two ways: E-W and M-S
     *
     * @param x the x coordinate of the last move
     * @param y the y coordinate of the last move
     * @return the result of the check: 1,2,...numberOfPlayers if K marks are found; 0 otherwise
     */
    private int isEndOnStraightLines(int x, int y) {
        int M = attributes.getM();
        int N = attributes.getN();
        int K = attributes.getK();

        int player = a[x][y];

        if (player != 0) {
            // the original cell at x;y count as one placed mark in the row to be checked
            int found = 1;
            //before the original cell
            if (x > 1) {
                for (int i = x - 1; i >= 1; i--) {
                    if (a[i][y] == player) {
                        found++;
                    } else {
                        break;
                    }
                }
                // could place IF already found K number of same marks
            }
            // after the original cell
            if (x < M) {
                for (int i = x + 1; i <= M; i++) {
                    if (a[i][y] == player) {
                        found++;
                    } else {
                        break;
                    }
                }
            }
            if (found >= K) {
                return player;
            } else {
                //reset for the next row
                found = 1;
            }

            if (y > 1) {
                for (int i = y - 1; i >= 1; i--) {
                    if (a[x][i] == player) {
                        found++;
                    } else {
                        break;
                    }
                }
                // could place IF already found K number of same marks
            }
            // after the original cell
            if (y < N) {
                for (int i = y + 1; i <= N; i++) {
                    if (a[x][i] == player) {
                        found++;
                    } else {
                        break;
                    }
                }
            }
            if (found >= K) {
                return player;
            }
        }
        return 0;
    }

    /**
     * Checks if K marks are in a diagonal, two ways: NE-SW and NW-SE
     *
     * @param x the x coordinate of the last move
     * @param y the y coordinate of the last move
     * @return the result of the check: 1,2,...numberOfPlayers if K marks are found; 0 otherwise
     */
    private int isEndOnDiagonals(int x, int y) {
        int M = attributes.getM();
        int N = attributes.getN();
        int K = attributes.getK();

        int player = a[x][y];

        if (player != 0) {
            // the original cell at x;y count as one placed mark in the row to be checked
            int found = 1;
            //before the original cell: to the 1;1 corner
            if (x > 1 && y > 1) {
                for (int i = x - 1, j = y - 1; i >= 1 && j >= 1; i--, j--) {
                    if (a[i][j] == player) {
                        found++;
                    } else {
                        break;
                    }
                }
                // could place IF already found K number of same marks
            }
            if (x < M && y < N) { // after the original cell : to the M;N corner
                for (int i = x + 1, j = y + 1; i <= M && j <= N; i++, j++) {
                    if (a[i][j] == player) {
                        found++;
                    } else {
                        break;
                    }
                }
            }
            if (found >= K) {
                return player;
            } else {
                //reset for the next diagonal
                found = 1;
            }
            //before the original cell: to the 1;N corner
            if (x > 1 && y < N) {
                for (int i = x - 1, j = y + 1; i >= 1 && j <= N; i--, j++) {
                    if (a[i][j] == player) {
                        found++;
                    } else {
                        break;
                    }
                }
                // could place IF already found K number of same marks
            }
            if (x < M && y > 1) { // after the original cell: to the M;1 corner
                for (int i = x + 1, j = y - 1; i <= M && j >= 1; i++, j--) {
                    if (a[i][j] == player) {
                        found++;
                    } else {
                        break;
                    }
                }
            }
            if (found >= K) {
                return player;
            }
        }
        return 0;
    }

    public int countAllMarks() {
        int count = 0;
        for (int i = 1; i <= attributes.getM(); i++)
            for (int j = 1; j <= attributes.getN(); j++)
                count += (a[i][j] != 0) ? 1 : 0;
        return count;
    }

    public boolean isNotDeal(int x, int y) {

        return isEnd(x, y) != -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        boolean equals = true;
        for(int i = 1; i <= attributes.getM(); ++i){
            for(int j = 1; j <= attributes.getN(); ++j){
                equals &= a[i][j] == state.a[i][j];
            }
        }
        return equals;

    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(a);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= attributes.getM(); i++) {
            for (int j = 1; j <= attributes.getN(); j++) {
                sb.append(' ').append(a[i][j]).append(' ');
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
