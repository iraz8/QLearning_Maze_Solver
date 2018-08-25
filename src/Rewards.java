public class Rewards {
    float stepCost;
    float win;
    float block;
    float penalty;

    Rewards(float stepCost, float win, float block, float penalty) {
        this.stepCost = stepCost;
        this.win = win;
        this.block = block;
        this.penalty = penalty;
    }

    float getWin() {
        return this.win;
    }

    float getBlock() {
        return this.block;
    }

    float getPenalty() {
        return this.penalty;
    }

    float getStepCost() {
        return this.stepCost;
    }

    void printRewards() {
        System.out.println("\nRewards:");
        System.out.println ("StepCost: " + getStepCost() + "\nWin: " + getWin() + "\nBlock" + getBlock() + "\nPenalty" + getPenalty());
    }
}
