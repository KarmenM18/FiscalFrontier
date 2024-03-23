package com.mygdx.game.Stocks;

/**
 * A safe growth stock option. With low risk low reward
 */
public class SafeGrowth {

    private double price;
    private String description;

    /**
     * Constructor with preset stock price of $50.00
     */
    public void SafeDividiens() {
        this.price = 50.00;
        this.description = "A Safe Stock with low risk low reward.\n" +
                "Low dividend pay of 2% payout every 5 turns.\n" +
                "Expected growth of 0.5% to 1% every turn or a low probably of 0.1% to 1% decline";
    }

    /**
     * @return dividen payout every 5 turns
     */
    public double dividenPay() {return this.price * 0.02;}
}
