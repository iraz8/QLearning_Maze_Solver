import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class Bot {
    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;

    float[][][] Q;
    float gamma;
    float learningRate;
    int n, m;
    int nrIterations;
    Game game;
    int startingI, startingJ;
    float unknown;
    Random random = new Random();
    ArrayList<Float> scores = new ArrayList<Float>();

    Bot (Game game, float gamma, float learningRate, int startingI, int startingJ, int nrIterations) {
        this.game = game;
        this.gamma = gamma;
        this.learningRate = learningRate;
        this.nrIterations = nrIterations;
        this.n = game.getN();
        this.m = game.getM();
        this.startingI = startingI;
        this.startingJ = startingJ;
        this.Q = new float[this.n][this.m][4];

    }

    void initializeQ() {
        initializeQ(0.0f);
    }

    void initializeQ(float initializationValue) {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                for (int k = 0; k < 4; k++)
                    this.Q[i][j][k] = initializationValue;
        this.unknown = initializationValue;
    }

    int[] move(int currentI, int currentJ, int action) {
        if (action == LEFT)
            currentJ--;
        if (action == RIGHT)
            currentJ++;
        if (action == UP)
            currentI--;
        if (action == DOWN)
            currentI++;
        int[] nextState = new int[2];
        nextState[0] = currentI;
        nextState[1] = currentJ;
        return nextState;
    }

    float getQMaxNextState(int currentI, int currentJ, int action) {
        int[] nextState = move(currentI, currentJ, action);
        int nextI = nextState[0];
        int nextJ = nextState[1];
        ArrayList<Float> nearActions = new ArrayList<Float>();

        boolean[] possibleMoves = game.getPossibleActions(currentI, currentJ);

        for (int i = 0; i < 4; i++)
            if (possibleMoves[i])
                nearActions.add(Q[nextI][nextJ][i]);
        Collections.sort(nearActions);

        if (nearActions.size() == 0) {
            System.out.println("ERROR! getQMaxNextState()");
            return -1;
        }

        return nearActions.get(nearActions.size() - 1);
    }

    float play() {
        int currentI = startingI;
        int currentJ = startingJ;

        float score = 0.0f;

        while (!game.checkIfGameWon(currentI, currentJ)) {

            boolean randomMove = false;
            int chance = random.nextInt(100);
            if (chance >= 80)
                randomMove = true;

            boolean[] possibleActions = game.getPossibleActions(currentI, currentJ);

            int action = - 1;
            if (randomMove) {
                action = random.nextInt(4);
                while (!possibleActions[action])
                    action = random.nextInt(4);
            }
            else    {
                float maxNearQ = Integer.MIN_VALUE;
                for (int i = 0; i < 4;i++)  {
                    if (possibleActions[i] && Q[currentI][currentJ][i] > maxNearQ) {
                        maxNearQ = Q[currentI][currentJ][i];
                        action = i;
                    }
                }
            }

            if (action == -1)
                System.out.println("ERROR! play() ---> action == -1");

            float Rvalue = game.getRvalue(currentI, currentJ, action);    //calcul R(s,a)

            Q[currentI][currentJ][action] = Q[currentI][currentJ][action] + learningRate *
                    (Rvalue + gamma * getQMaxNextState(currentI, currentJ, action) - Q[currentI][currentJ][action]);

            int[] nextState = move(currentI, currentJ, action);
            int nextI = nextState[0];
            int nextJ = nextState[1];
            currentI = nextI;
            currentJ = nextJ;
            score += game.getScore(currentI, currentJ);
        }

      //  printQmatrix();

        return score;
    }

    void playNTimes() {
        for (int i = 0; i < nrIterations; i++) {
            float score = play();
            scores.add(score);
        }
    }

/*    void learn() {
        int currentI = startingI;
        int currentJ = startingJ;

        while (!game.checkIfGameWon(currentI, currentJ)) {
            boolean[] possibleActions = game.getPossibleActions(currentI, currentJ);
            int action = random.nextInt(4);
            while (!possibleActions[action])
                action = random.nextInt(4);

            float Rvalue = game.getRvalue(currentI, currentJ, action);    //calcul R(s,a)

            Q[currentI][currentJ][action] = Rvalue + gamma * getQMaxNextState(currentI, currentJ, action);

            int[] nextState = move(currentI, currentJ, action);
            int nextI = nextState[0];
            int nextJ = nextState[1];
            currentI = nextI;
            currentJ = nextJ;

        }

           printQmatrix();
    }

    float play() {
        float score = 0.0f;

        int currentI = startingI;
        int currentJ = startingJ;

        int tries = 0;
        while (!game.checkIfGameWon(currentI, currentJ) && tries < 100) {
            boolean[] possibleActions = game.getPossibleActions(currentI, currentJ);

            ArrayList<Float> nearCells = new ArrayList<Float>();
            for (int i = 0; i < 4; i++)
                if (possibleActions[i])
                    nearCells.add(Q[currentI][currentJ][i]);

            Collections.sort(nearCells);

            if (nearCells.size() == 0) {
                System.out.println("ERROR! play()");
                return 0.0f;
            }

            int[] nextMove;
            int action = -1;
            for (int i = nearCells.size() - 1; i >= 0; i--) {
                if (possibleActions[LEFT] && nearCells.get(i) == Q[currentI][currentJ][LEFT]) {
                    action = LEFT;
                    break;
                } else if (possibleActions[RIGHT] && nearCells.get(i) == Q[currentI][currentJ][RIGHT]) {
                    action = RIGHT;
                    break;
                } else if (possibleActions[UP] && nearCells.get(i) == Q[currentI][currentJ][UP]) {
                    action = UP;
                    break;
                } else if (possibleActions[DOWN] && nearCells.get(i) == Q[currentI][currentJ][DOWN]) {
                    action = DOWN;
                    break;
                }
            }

            if (action == - 1)
                System.out.println("ERROR! play() . Action not found!");

            //TODO
            if (Q[currentI][currentJ][action] == 0.0f) {
                while (!possibleActions[action])
                    action = random.nextInt(4);
            }

            nextMove = move(currentI, currentJ, action);

            System.out.println("###" + currentI + " " + currentJ + " " + game.checkIfGameWon(currentI,currentJ) + " " + score);
         //   System.out.println("Q " + Q[currentI][currentJ][LEFT] + " " + Q[currentI][currentJ][RIGHT] + " " +
         //           Q[currentI][currentJ][UP] + " " + Q[currentI][currentJ][DOWN]);
            currentI = nextMove[0];
            currentJ = nextMove[1];

            score += game.getScore(currentI, currentJ);
            tries++;
        }

        return score;
    }

    void exploreAndPlayNtimes() {
        for (int i = 0; i < nrIterations; i++) {
            learn();
            float score = play();
            scores.add(score);
        }
    }

    void exploreNTimesAndPlay(int nrExploreTimes) {
        for (int i = 0; i < nrExploreTimes; i++)
            learn();
        for (int i = 0; i < nrIterations; i++) {
            learn();
            if (i % 5 == 0) {
                float score = play();
                this.scores.add(score);
            }
        }
    }
*/
    ArrayList<Float> getScores() {
        return this.scores;
    }

    void printScores() {
        System.out.print("Scores :");
        for (int i = 0; i < scores.size(); i++)
            System.out.print(scores.get(i) + " ");
        System.out.println();
    }

    void printQmatrix() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) {
                System.out.print(i + " " + j + " : ");
                for (int k = 0; k < 4; k++) {
                    System.out.print(Q[i][j][k] + " ");
                }
                System.out.println();
            }
    }
}
