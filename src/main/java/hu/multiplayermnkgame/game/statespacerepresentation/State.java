package hu.multiplayermnkgame.game.statespacerepresentation;

import java.util.ArrayList;
import java.util.List;

import static hu.multiplayermnkgame.game.gameplay.GameLoop.*;

public class State {

    public int[][] a;

    /**
     * Constructs the initial state of the table
     */
    public State(){
        // initial value 0 by default
        a = new int[N+1][M+1];
    }

    public State(int[][] a){
        this.a = a;
    }

    public List<Integer> neighbours(int x, int y){
        // taking advantage of the fact, that at the 0 indecies the a array contains 0 values also.
        List<Integer> list = new ArrayList();
        list.add(a[x-1][y-1]);  //NorthWest
        list.add(a[x-1][y]);    //North
        list.add(a[x][y-1]);    //West
        if(y+1 <= M){
            list.add(a[x-1][y+1]);  //NorthEast
            list.add(a[x][y+1]);    //East
        }
        if(x+1 <= N){
            list.add(a[x+1][y-1]);  //SouthWest
            list.add(a[x+1][y]);    //South
        }
        if(x+1 <= N && y+1 <= M){
            list.add(a[x+1][y+1]);
        }
        return list;
    }
    /**
     * Checks if the state is a goal state
     * @param x the x coordinate of the last move
     * @param y the y coordinate of the last move
     * @return the result of the check: 1,2,...numberOfPlayers if somebody won by K marks; -1 if the table is full and nobody won; 0 otherwise
     */
    public int isEnd(int x, int y){
        // only before first step
        if(x == -1 || y == -1){
            return 0;
        }
        //if somebody won
        int win = isEndOnStraightLines(x, y);
        if(win != 0){
            return win;
        }else{
            win = isEndOnDiagonals(x, y);
        }
        // the table can be full and somebody still win with the last step
        if(win != 0){
            return win;
        }else if(countAllMarks() == N*M ){
            return -1;
        }else{
            return 0;
        }
    }

    /**
     * Checks if K marks are in a row, two ways: E-W and M-S
     * @param x the x coordinate of the last move
     * @param y the y coordinate of the last move
     * @return the result of the check: 1,2,...numberOfPlayers if K marks are found; 0 otherwise
     */
    private int isEndOnStraightLines(int x, int y) {
        int player = a[x][y];
        if(player != 0){
            // the original cell at x;y count as one placed mark in the row to be checked
            int found = 1;
            //before the original cell
            if(x>1){
                for(int i = x-1; i >= 1;i--){
                    if(a[i][y] == player){
                        found++;
                    }else{
                        break;
                    }
                }
                // could place IF already found K number of same marks
            }
            // after the original cell
            if(x<N){
                for(int i = x+1;i <= N;i++){
                    if(a[i][y] == player){
                        found++;
                    }else{
                        break;
                    }
                }
            }
            if(found >= K){
                return player;
            }else{
                //reset for the next row
                found = 1;
            }

            if(y>1){
                for(int i = y-1; i >= 1;i--){
                    if(a[x][i] == player){
                        found++;
                    }else{
                        break;
                    }
                }
                // could place IF already found K number of same marks
            }
            // after the original cell
            if(y<M){
                for(int i = y+1;i <= M;i++){
                    if(a[x][i] == player){
                        found++;
                    }else{
                        break;
                    }
                }
            }
            if(found >= K){
                return player;
            }
        }
        return 0;
    }

    /**
     * Checks if K marks are in a diagonal, two ways: NE-SW and NW-SE
     * @param x the x coordinate of the last move
     * @param y the y coordinate of the last move
     * @return the result of the check: 1,2,...numberOfPlayers if K marks are found; 0 otherwise
     */
    private int isEndOnDiagonals(int x, int y) {
        int player = a[x][y];

        if(player!=0) {
            // the original cell at x;y count as one placed mark in the row to be checked
            int found = 1;
            //before the original cell: to the 1;1 corner
            if (x > 1 && y > 1) {
                for (int i = x-1, j= y-1; i >= 1 && j >= 1; i--, j--) {
                    if (a[i][j] == player) {
                        found++;
                    } else {
                        break;
                    }
                }
                // could place IF already found K number of same marks
            }
            if (x < N && y < M) { // after the original cell : to the M;N corner
                for (int i = x+1, j= y+1; i <= N && j <= M; i++, j++) {
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
            if (x > 1 && y < M) {
                for (int i = x-1, j= y+1; i >= 1 && j <= M; i--, j++) {
                    if (a[i][j] == player) {
                        found++;
                    } else {
                        break;
                    }
                }
                // could place IF already found K number of same marks
            }
            if (x < N && y > 1) { // after the original cell: to the M;1 corner
                for (int i = x+1, j= y-1; i <= N && j >= 1; i++, j--) {
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

    public int countAllMarks(){
        int count = 0;
        for (int i=1; i <= N; i++)
            for (int j=1; j <= M; j++)
                count += (a[i][j] != 0) ? 1: 0;
        return count;
    }

    public int countMarks(int player) {
        int count = 0;
        for (int i=1; i <= N; i++)
            for (int j=1; j <= M; j++)
                    count += (a[i][j] == player) ? 1: 0;
        return count;
    }

    public boolean isNotDeal(int x, int y) {

        return isEnd(x, y) != -1;
    }

    /**
     * Checks if somebody won the game represented by this State.
     * @return 1...numberOfPlayers if the numberOfPlayers won, 0 if it is a tie
     */
    public int somebodyWon(){
        int result = checkRows();
        result = result==0?checkColumns():result;
        result = result==0?checkDiagonals():result;
        return result;
    }

    private int checkRows() {
        int player = 0;
        int count = 0;
        for(int i = 1; i <= N;i++){
            for(int j = 1; j<= M; j++){
                if(a[i][j]!=0){
                    if(player==0){
                        player = a[i][j];
                        count++;
                    }else if(player == a[i][j]){
                        count++;
                    }else if(player != a[i][j]){
                        if(count >= K){
                            return player;
                        }else{
                            player = a[i][j];
                            count = 1;
                        }
                    }
                }
            }
            if(count >= K){
                return player;
            }else{
                count = 0;
            }
        }
        if(count >= K){
            return player;
        }else{
            return 0;
        }
    }

    private int checkColumns() {
        int player = 0;
        int count = 0;
        for(int j = 1; j<= M;j++){
            for(int i = 1; i <= N;i++){
                if(a[i][j]!=0){
                    if(player==0){
                        player = a[i][j];
                        count++;
                    }else if(player == a[i][j]){
                        count++;
                    }else if(player != a[i][j]){
                        if(count >= K){
                            return player;
                        }else{
                            player = a[i][j];
                            count = 1;
                        }
                    }
                }
            }
            if(count >= K){
                return player;
            }else{
                count = 0;
            }
        }
        if(count >= K){
            return player;
        }else{
            return 0;
        }
    }

    private int checkDiagonals() {
        int player = 0;
        int count = 0;
        // Left main diagonal line
        for(int i = 1; i <= Math.min(N, M); i++ ){
            if(a[i][i]!=0){
                if(player==0){
                    player = a[i][i];
                    count++;
                }else if(player == a[i][i]){
                    count++;
                }else if(player != a[i][i]){
                    if(count >= K){
                        return player;
                    }else{
                        player = a[i][i];
                        count = 1;
                    }
                }
            }
        }

        if(count >= K){
            return player;
        }else{
            count = 0;
        }

        //Under the left main diagonal line
        for(int i = 2; i <= N - K + 1; i++ ){
            for(int j = 1, k = i; j <= Math.min(M,N - i + 1); j++, k++){
                if(a[j][k]!=0){
                    if(player==0){
                        player = a[j][k];
                        count++;
                    }else if(player == a[j][k]){
                        count++;
                    }else if(player != a[j][k]){
                        if(count >= K){
                            return player;
                        }else{
                            player = a[j][k];
                            count = 1;
                        }
                    }
                }
            }
            if(count >= K){
                return player;
            }else{
                count = 0;
            }
        }

        //Above the left main diagonal line
        for(int j = 2; j <= M - K + 1; j++ ){
            for(int i = 1, k = j; i <= Math.min(N, M - j + 1); i++, k++){
                if(a[i][k]!=0){
                    if(player==0){
                        player = a[i][k];
                        count++;
                    }else if(player == a[i][k]){
                        count++;
                    }else if(player != a[i][k]){
                        if(count >= K){
                            return player;
                        }else{
                            player = a[i][k];
                            count = 1;
                        }
                    }
                }
            }
            if(count >= K){
                return player;
            }else{
                count = 0;
            }
        }

        //Right main diagonal line
        for(int i = 1, j; i <= Math.min(N,M); i++ ){
            j = M - i +1;
            if(a[i][j]!=0){
                if(player==0){
                    player = a[i][j];
                    count++;
                }else if(player == a[i][j]){
                    count++;
                }else if(player != a[i][j]){
                    if(count >= K){
                        return player;
                    }else{
                        player = a[i][j];
                        count = 1;
                    }
                }
            }
        }

        if(count >= K){
            return player;
        }else{
            count = 0;
        }

        //Under the right main diagonal line
        for(int i = 2; i <= N - K + 1; i++ ){
            for(int j = M, k = i; j <= Math.max(1,M - N + i); j--, k++){
                if(a[j][k]!=0){
                    if(player==0){
                        player = a[j][k];
                        count++;
                    }else if(player == a[j][k]){
                        count++;
                    }else if(player != a[j][k]){
                        if(count >= K){
                            return player;
                        }else{
                            player = a[j][k];
                            count = 1;
                        }
                    }
                }
            }
            if(count >= K){
                return player;
            }else{
                count = 0;
            }
        }

        //Above the right main diagonal line
        for(int j = M - 1; j <= K; j-- ){
            for(int i = 1, k = j; i <= j; i++, k--){
                if(a[i][k]!=0){
                    if(player==0){
                        player = a[i][k];
                        count++;
                    }else if(player == a[i][k]){
                        count++;
                    }else if(player != a[i][k]){
                        if(count >= K){
                            return player;
                        }else{
                            player = a[i][k];
                            count = 1;
                        }
                    }
                }
            }
            if(count >= K){
                return player;
            }else{
                count = 0;
            }
        }

        if(count >= K){
            return player;
        }else{
            return 0;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=1;i<=N;i++){
            for ( int j =1; j <= M; j++) {
                sb.append(' ').append(a[i][j]).append(' ');
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
