class Game {
    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;
    float[][] mapMatrix;
    int n, m;
    Rewards rewards;
    float[][][] R;
    int finishI, finishJ;

    Game(Map map) {
        this.mapMatrix = map.getMapMatrix();
        this.n = map.getN();
        this.m = map.getM();
        this.rewards = map.getRewards();

        //Initialize finishI and finishJ
        int[] tmpFinishPositions = map.getFinishPositions();
        this.finishI = tmpFinishPositions[0];
        this.finishJ = tmpFinishPositions[1];

        R = new float[map.getN()][map.getM()][4];
    }

    void initializeRmatrix() {

        //Initialize R Matrix
        float block = rewards.getBlock();

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) {
                if (j == 0 || mapMatrix[i][j - 1] == block)
                    R[i][j][LEFT] = block;
                else
                    R[i][j][LEFT] = mapMatrix[i][j - 1];

                if (j == m - 1 || mapMatrix[i][j + 1] == block)
                    R[i][j][RIGHT] = block;
                else
                    R[i][j][RIGHT] = mapMatrix[i][j + 1];

                if (i == 0 || mapMatrix[i - 1][j] == block)
                    R[i][j][UP] = block;
                else
                    R[i][j][UP] = mapMatrix[i - 1][j];

                if (i == n - 1 || mapMatrix[i + 1][j] == block)
                    R[i][j][DOWN] = block;
                else
                    R[i][j][DOWN] = mapMatrix[i + 1][j];
            }

    }

    void printRmatrix() {
        System.out.println("\nR Matrix : ");
        System.out.println("      L R U D");
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) {
                System.out.print(i + " " + j + " : ");
                String valueToPrint;
                for (int k = 0; k < 4; k++) {
                    if (R[i][j][k] == rewards.getStepCost())
                        valueToPrint = "\u25a1";
                    else if (R[i][j][k] == rewards.getBlock())
                        valueToPrint = "\u25a0";
                    else if (R[i][j][k] == rewards.getWin())
                        valueToPrint = "F";
                    else if (R[i][j][k] == rewards.getPenalty())
                        valueToPrint = "P";
                    else
                        valueToPrint = "E"; // E == error

                    System.out.print(valueToPrint + " ");
                }
                System.out.println();
            }
    }

    boolean[] getPossibleActions(int currentI, int currentJ) {
        boolean[] possibleActions = new boolean[4];
        float block = rewards.getBlock();

        if (R[currentI][currentJ][LEFT] == block)
            possibleActions[LEFT] = false;
        else
            possibleActions[LEFT] = true;

        if (R[currentI][currentJ][RIGHT] == block)
            possibleActions[RIGHT] = false;
        else
            possibleActions[RIGHT] = true;

        if (R[currentI][currentJ][UP] == block)
            possibleActions[UP] = false;
        else
            possibleActions[UP] = true;

        if (R[currentI][currentJ][DOWN] == block)
            possibleActions[DOWN] = false;
        else
            possibleActions[DOWN] = true;

        return possibleActions;
    }

    void testPossibleActions(int currentI, int currentJ) {
        boolean[] possibleActions = getPossibleActions(currentI,currentJ);
        System.out.println("Possible actions for : " + currentI + " " + currentJ +" LEFT RIGHT UP DOWN");
        System.out.println("                           " + possibleActions[0] + " " + possibleActions[1] + " " + possibleActions[2] + " " + possibleActions[3] );
    }
    boolean checkIfGameWon(int currentI, int currentJ) {
        if (currentI == this.finishI && currentJ == this.finishJ)
            return true;
        return false;
    }

    float getScore(int currentI, int currentJ) {
        return mapMatrix[currentI][currentJ];
    }

    float getRvalue(int currentI, int currentJ, int action) {
        return this.R[currentI][currentJ][action];
    }

    int getN() {
        return this.n;
    }

    int getM() {
        return this.m;
    }
}
