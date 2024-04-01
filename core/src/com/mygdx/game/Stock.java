package com.mygdx.game;
import java.util.Random;

/**
 * Holds the logic for simulating the stock market fluctuation.
 *
 * @author Earl Castillo (ecastil3)
 */
public class Stock {

    /** Name of the stock. */
    private String tickerName;
    /** Stock's current buy price per quantity. */
    private int price;
    /** Description outlining the risks and reward of the stock. */
    private String description;
    /** Percentage price changed since last round. */
    private double priceChange;
    /** Percentage dividend pay changed since last round. */
    private double divPayChange;
    /** Dividend payout percentage. */
    private double divPay;

    /* For the "risk" and price change of the stock */

    /** Minimum growth change. */
    private double minChangeGrowth;
    /** Minimum decline change. */
    private double minChangeDecline;
    /** Maximum growth change. */
    private double maxChangeGrowth;
    /** Maximum decline change. */
    private double maxChangeDecline;
    /** Represents the stock's risk. */
    private int risk;
    /** Represents the dividend's risk. */
    private int divRisk;
    /** Used to generate randomized aspects of stock change. */
    private Random rand;


    /**
     * Constructor for preset stocks.
     *
     * @param name Stock's name
     * @param price Stock's current buy price per quantity
     * @param description Description outlining the risks and reward of the stock.
     * @param mincg Minimum growth change.
     * @param mincd Minimum decline change.
     * @param maxcg Maximum growth change.
     * @param maxcd Maximum decline change
     * @param divPay Dividend payout percentage
     * @param risk Representation of the stock's risk.
     * @param divRisk Representation of the dividend's risk.
     */
    public Stock(String name, int price, String description,
                 double mincg, double mincd, double maxcg, double maxcd, double divPay, int risk, int divRisk) {

        // Initialize stock's attributes
        this.tickerName = name;
        this.price = price;
        this.priceChange = 0;
        this.description = description;
        this.minChangeGrowth = mincg;
        this.minChangeDecline = mincd;
        this.maxChangeGrowth = maxcg;
        this.maxChangeDecline = maxcd;
        this.divPay = divPay;
        this.risk = risk;
        this.divRisk = divRisk;
        this.rand = new Random();
    }

    /**
     * Private no-arg constructor for serialization.
     */
    private Stock() {
        rand = new Random();
    }

    /**
     * Returns the dividend payout per 5 rounds. Fractional values are rounded up
     * @return Dividend payout per 5 rounds
     */
    public int dividendPay() {return (int) Math.ceil(this.price * (this.divPay/100));}

    /**
     * Returns the stock's current price
     * @return Stock's current price
     */
    public int getPrice(){return this.price;}

    /**
     * Returns the dividend payout percentage
     * @return Dividend payout percentage
     */
    public double getDivPay() {return divPay;}

    /**
     * Returns the stock's description. Outlines the risks and reward of the stock.
     * @return Description about the stock
     */
    public String getDescription() {return this.description;}

    /**
     * Returns the stock's ticker name.
     * @return Stock ticker name
     */
    public String getTickerName(){return this.tickerName;}

    /**
     * Returns the percentage price change of the stock since last update. Capital gain or loss.
     * @return Percentage price change of the stock since last update
     */
    public double getPriceChange(){return this.priceChange;}

    /**
     * Returns the percentage dividend pay change since last update.
     * @return Percentage dividend pay change since last update.
     */
    public double getDivPayChange(){return this.divPayChange;}

    /**
     * Updates the stock price and dividend pay every round.
     * <br><br>
     *
     * The stock price and dividend pay are updated as follows:
     * <br>
     *
     * <b>Stock Risk:</b>
     * Risk is determined from a number from 1 to 9, with 1 representing a 10% risk for stock to decline, up to a max
     * risk of 9 having a 90% chance to decline.
     * <br>
     *
     * <b>Dividend pay:</b>
     * divRisk 1 -> 10% chance to decrease divPay. 1% increase in divPay or 0.5% divPay decrease
     * divRisk 3 -> 30% chance to decrease divPay. 10% incrase in divPay or 2% divPay decrease
     * divRisk 5 -> 50% chance to decrease divPay. 30% increase in divPay or 50% divPay decrease
     */
    public void updatePrice(){
        int prob = rand.nextInt(10) + 1; //Picks a number from 1 to 10 for stock price growth
        int div = rand.nextInt(10) + 1; //Picks a number from 1 to 10 for dividend decline
        //For Stock Price change
        if (prob > this.risk) { //Growth
            this.priceChange = rand.nextDouble(this.maxChangeGrowth) + this.minChangeGrowth;
        } else { //Decreasing
            this.priceChange = -(rand.nextDouble(this.maxChangeDecline) + this.minChangeDecline);
        }
        this.price += (int) (this.price * (this.priceChange/100));

        if (this.price <= 0) this.price = 0; //Price of stock can't dip below 0

        //For Dividend changes if there is a potential to change dividend pay
        if (div > this.divRisk && this.divRisk != 0) { //Increasing dividend pay
            switch (this.divRisk) {
                case 1 : this.divPay += this.divPay*0.01; this.divPayChange = 1; break;
                case 3 : this.divPay += this.divPay*0.1; this.divPayChange = 10; break;
                case 5 : this.divPay += this.divPay*0.3; this.divPayChange = 30; break;
            }
        } else if (this.divRisk != 0){ //Decreasing dividend pay
            switch (this.divRisk) {
                case 1 : this.divPay -= this.divPay*0.005; this.divPayChange = -0.5; break;
                case 3 : this.divPay -= this.divPay*0.02; this.divPayChange = -2; break;
                case 5 : this.divPay -= this.divPay*0.5; this.divPayChange = -50; break;
            }
        }

        if (this.divPay <= 0) this.divPay = 0; //Stock dividend payout can't be less than 0
    }
}