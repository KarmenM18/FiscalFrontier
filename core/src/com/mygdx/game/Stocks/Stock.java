package com.mygdx.game.Stocks;

import java.util.Random;

/**
 * A safe growth stock option. With low risk low reward
 */
public class Stock {

    private String tickerName;  //Name of the stock
    private double price;       //Stock Buy Price per QTY
    private String description; //Description outlining the risks and reward
    private double priceChange; //Price Change since last round as a %
    private double divPay;      //For how much to pay out the dividends as a %

    //For the "risk" of the stock
    private double minChangeGrowth;
    private double minChangeDecline;
    private double maxChangeGrowth;
    private double maxChangeDecline;
    private double risk;

    /**
     * Constructor with preset stock price of $50.00
     */
    public Stock(String name, double price, String description,
                 double mincg, double mincd, double maxchg, double maxcd, double divPay, double risk) {
        this.tickerName = name;
        this.price = price;
        this.priceChange = 0;
        this.description = description;
        this.minChangeGrowth = mincg;
        this.minChangeDecline = mincd;
        this.maxChangeGrowth = maxchg;
        this.maxChangeDecline = maxcd;
        this.divPay = divPay;
        this.risk = risk;
    }

    /**
     * @return dividen payout every 5 rounds
     */
    public double dividenPay() {return this.price * (this.divPay/100);}

    /**
     * @return the stock's current price
     */
    public double getPrice(){return this.price;}

    /**
     * @return Description about the stock
     */
    public String getDescription() {return this.description;}

    /**
     * @return The stock ticker Name
     */
    public String getTickerName(){return this.tickerName;}

    /**
     * Updates the stock price every round
     * risk is determined from a number from 1 to 9;
     * with 1 representing a 10% risk for stock to decline
     * up to a max risk of 9 having a 90% chance to decline
     */
    public void updatePrice(){
        Random rand = new Random();
        int prob = rand.nextInt(10) + 1; //Picks a number from 1 to 10

        if (prob > this.risk) { //Growth
            this.priceChange = rand.nextDouble(this.maxChangeGrowth) + this.minChangeGrowth; //0.5% to 1.5%
            this.price += this.price * (this.priceChange/100);
        } else { //Decreasing
            this.priceChange = -rand.nextDouble(this.maxChangeDecline) + this.minChangeDecline; //0.1% to 1%
            this.price += this.price *(this.priceChange/100);
        }
    }
}