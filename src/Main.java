import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Rewards rewards = new Rewards(-10.0f, 100.0f, Float.MIN_VALUE, -100.0f);
        /*Map map = new Map(rewards);
        map.generateSimpleMap();*/
        Map map = new Map(10,10,3,3,1, rewards);
        map.generateRandomMap();
        map.printMap();
        map.printFinishPositions();
        rewards.printRewards();

        Game game = new Game(map);
        game.initializeRmatrix();
     //   game.printRmatrix();
     //   game.testPossibleActions(1,2);

        Bot bot = new Bot(game, 0.8f,0.8f, 2, 0, 250);
        bot.initializeQ(-15.0f);
        bot.playNTimes();
       // bot.exploreAndPlayNtimes();
       // bot.exploreNTimesAndPlay(1);
      //  bot.exploreAndPlayNtimes(1);
        //  bot.exploreAndPlayNtimes();
        bot.printScores();

        System.out.println("\n" + "Execution finished!");
        System.out.println("\n" + "Generating chart...");

        MakeChart chart = new MakeChart();
        ArrayList<Float> scores = new ArrayList<>(bot.getScores());
       // Collections.sort(scores);
        chart.make(scores);
        System.out.println("\nExiting...");
    }
}
