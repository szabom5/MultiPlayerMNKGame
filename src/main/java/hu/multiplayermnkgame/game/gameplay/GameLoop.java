package hu.multiplayermnkgame.game.gameplay;

import hu.multiplayermnkgame.game.algorithm.*;
import hu.multiplayermnkgame.game.gamerepresentation.*;
import hu.multiplayermnkgame.game.heuristic.*;
import javafx.util.Pair;
import hu.multiplayermnkgame.game.statespacerepresentation.Operator;

import java.util.*;

public class GameLoop {
    // M >= 2
    public static int N;

    // N >= 2
    public static int M;

    // 2 <= K <= min(M,N)
    public static int K;

    public static int numberOfPlayers;

    private Map<Integer, Pair<MultiPlayerAlgorithm, Heuristic>> mapOfPlayerStrategies;

    public static boolean logging;

    private GameLoop(GameLoopBuilder builder){
        numberOfPlayers = builder.numOfPlayers;
        N = builder.N;
        M = builder.M;
        K = builder.K;

        this.mapOfPlayerStrategies = new HashMap<>(builder.mapOfPlayerStrategies);
        initializeHeuristics();
        logging = builder.logging;
    }

    private void initializeHeuristics() {
        for(Map.Entry entry : mapOfPlayerStrategies.entrySet()){
            if(entry.getValue() instanceof RulesHeuristic){
                ((RulesHeuristic) entry.getValue()).initialize();
            }
        }
    }

    public void loop(){

        PlaySpace space = new PlaySpace();
        GameState gameState = space.start();

        int numberOfMarks = 1;
        int player;

        Scanner sc = new Scanner(System.in);

        while ( !gameState.isEnd() ) {

            System.out.println(gameState);
            player = (numberOfMarks-1) % numberOfPlayers + 1;

            final long startTime = System.currentTimeMillis();

            Step bestStep;

            if(numberOfMarks == 1){
                bestStep = new Step(new Operator(N/2+1, M/2+1, 1), 1);
            }else{
                bestStep = mapOfPlayerStrategies.get(player).getKey().offer(gameState, space, mapOfPlayerStrategies.get(player).getValue());
            }

            System.out.println("player = "+player);

            if( isPlayerNotAI(player)){
                ArrayList<Step> ops = new ArrayList<>();
                for ( Step op : space.steps ) {
                    if (op.applicable(gameState)) {
                        ops.add(op);
                    }
                }
                for (int i= 0; i<ops.size(); i++) {
                    System.out.print(i);
                    System.out.print(ops.get(i)==bestStep?" -> ": "    ");
                    System.out.println(ops.get(i));
                }
                int next = sc.nextInt();
                while ( next < 0 || next >= ops.size() ) {
                    next = sc.nextInt();
                }
                System.out.println(ops.get(next));
                gameState = ops.get(next).apply(gameState);
            }else{
                System.out.println(bestStep);
                gameState = bestStep.apply(gameState);
            }

            System.out.println("*****************\n");

            final long duration = System.currentTimeMillis() - startTime;
            System.out.println("duration: "+duration);

            numberOfMarks++;


        }
        //System.out.println("playerend="+playState.numberOfPlayers);
        System.out.println(gameState);
    }

    private boolean isPlayerNotAI(int player) {
        Heuristic heuristic = mapOfPlayerStrategies.get(player).getValue();

        return heuristic == null;

        /*return heuristic instanceof Heuristic &&
                !(heuristic instanceof PatternHeur) && !(heuristic instanceof RulesHeur) && !(heuristic instanceof TerminalNodeHeuristic);*/

    }

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public int getK() {
        return K;
    }

    public int getNumOfPlayers() {
        return numberOfPlayers;
    }

    public Map<Integer, Pair<MultiPlayerAlgorithm, Heuristic>> getMapOfPlayerStrategies() {
        return mapOfPlayerStrategies;
    }

    public boolean isLogging() {
        return logging;
    }

    public static class GameLoopBuilder {
        private  int N;
        private  int M;
        private  int K;
        private int numOfPlayers;
        private  Map<Integer, Pair<MultiPlayerAlgorithm, Heuristic>> mapOfPlayerStrategies;
        private  boolean logging;

        public GameLoopBuilder(){
        }

        public GameLoopBuilder setNumberOfPlayers(int player){
            this.numOfPlayers = player;
            return this;
        }

        public GameLoopBuilder setBoardParameters(int n, int m){
            this.N = n;
            this.M = m;
            return this;
        }

        public GameLoopBuilder setWinningNumber(int k){
            this.K = k;
            return this;
        }

        public GameLoopBuilder setLogging(boolean logging){
            this.logging = logging;
            return this;
        }

        public GameLoopBuilder setMapOfPlayerStrategies(Map<Integer, Pair<MultiPlayerAlgorithm, Heuristic>> strategies){
            this.mapOfPlayerStrategies = new HashMap<>(strategies);
            return this;
        }

        public GameLoop build(){
            return new GameLoop(this);
        }

    }

}
