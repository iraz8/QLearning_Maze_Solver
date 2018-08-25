import java.util.Random;

class Map {
    private float[][] map;
    private int n, m;
    private Rewards rewards;
    private int startingI, startingJ;
    int nrBlocks;
    int nrPenalityZones;
    int nrWinZones;

    Random random = new Random();

    Map(Rewards rewards) {
        this.rewards = rewards;
    }

    Map(int n, int m, int startingI, int startingJ, Rewards rewards) {
        this.n = n;
        this.m = m;
        this.startingI = startingI;
        this.startingJ = startingJ;
        this.rewards = rewards;
    }

    Map(int n, int m, int nrBlocks,int nrPenalityZones,int nrWinZones, Rewards rewards) {
        this.n = n;
        this.m = m;
        this.rewards = rewards;
        this.nrBlocks = nrBlocks;
        this.nrPenalityZones = nrPenalityZones;
        this.nrWinZones = nrWinZones;
    }

    void generateSimpleMap() {
        float[][] tmpMap = new float[3][4];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                tmpMap[i][j] = rewards.getStepCost();

        tmpMap[0][3] = rewards.getWin();
        tmpMap[1][1] = rewards.getBlock();
        tmpMap[1][3] = rewards.getPenalty();

        this.n = 3;
        this.m = 4;
        this.startingI = 2;
        this.startingJ = 0;
        this.map = tmpMap;
    }

    void generateRandomMap() {
        float[][] tmpMap = new float[n][m];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                tmpMap[i][j] = rewards.getStepCost();

            for (int i = 0; i < nrBlocks; i++) {
                int randomI = random.nextInt(n);
                int randomJ = random.nextInt(m);
                tmpMap[randomI][randomJ] = rewards.getBlock();
            }

            for (int i = 0; i < nrPenalityZones; i++) {
                int randomI = random.nextInt(n);
                int randomJ = random.nextInt(m);
                tmpMap[randomI][randomJ] = rewards.getPenalty();
            }

            for (int i = 0; i < nrWinZones; i++) {
                int randomI = random.nextInt(n);
                int randomJ = random.nextInt(m);
                tmpMap[randomI][randomJ] = rewards.getWin();
            }

            if (nrWinZones <= 0) {
                int randomI = random.nextInt(n);
                int randomJ = random.nextInt(m);
                tmpMap[randomI][randomJ] = rewards.getWin();
            }

        this.startingI = random.nextInt(n);
        this.startingJ = random.nextInt(m);
        this.map = tmpMap;

    }
    int getN() {
        return this.n;
    }

    int getM() {
        return this.m;
    }

    float[][] getMapMatrix() {
        return this.map;
    }

    Rewards getRewards() {
        return this.rewards;
    }

    void printMap() {
        System.out.println("F == finish \n" +
                "S == start \n" +
                "P == penalty \n");

        System.out.println("Map :");
        String valueToPrint;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == startingI && j == startingJ)
                    valueToPrint = "\u25a3";
                else if (map[i][j] == rewards.getStepCost())
                    valueToPrint = "\u25a1";
                else if (map[i][j] == rewards.getBlock())
                    valueToPrint = "\u25a0";
                else if (map[i][j] == rewards.getWin())
                    valueToPrint = "F";
                else if (map[i][j] == rewards.getPenalty())
                    valueToPrint = "P";
                else
                    valueToPrint = "E"; // E == error

                System.out.print(valueToPrint + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    int[] getFinishPositions() {
        int[] finishPosition = new int[2];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (map[i][j] == rewards.getWin()) {
                    finishPosition[0] = i;
                    finishPosition[1] = j;
                    return finishPosition;
                }

        //ERROR
        System.out.println("ERROR! getFinishPositions()");
        finishPosition[0] = -1;
        finishPosition[1] = -1;
        return finishPosition;
    }

    void printFinishPositions() {
        int[] finishPosition = getFinishPositions();
        System.out.println("Finish positions : " + finishPosition[0] + " " + finishPosition[1]);
    }
}
