package com.mygdx.game;
import java.util.Random;

/**
 * Holds the logic for simulating the stock market fluctuation
 */
public class Stock {

    private String tickerName;  //Name of the stock
    private int price;          //Stock Buy Price per QTY
    private String description; //Description outlining the risks and reward
    private double priceChange; //Price Change since last round as a %
    private double divPayChange;//Dividend Pay Change as a %
    private double divPay;      //For how much to pay out the dividends as a %

    //For the "risk" of the stock
    private double minChangeGrowth;
    private double minChangeDecline;
    private double maxChangeGrowth;
    private double maxChangeDecline;
    private int risk;
    private int divRisk;
    private Random rand;

    /**
     * Constructor for preset stocks
     */
    public Stock(String name, int price, String description,
                 double mincg, double mincd, double maxchg, double maxcd, double divPay, int risk, int divRisk) {
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
        this.divRisk = divRisk;
        this.rand = new Random();
    }

    /**
     * Private no-arg constructor for serialization.
     */
    private Stock() {}

    /**
     * @return dividend payout every 5 rounds. Also rounds the number up
     */
    public int dividendPay() {return (int) Math.ceil(this.price * (this.divPay/100));}

    /**
     * @return the stock's current price
     */
    public int getPrice(){return this.price;}

    /**
     * @return returns dividend payout information in %
     */
    public double getDivPay() {return divPay;}

    /**
     * @return Description about the stock
     */
    public String getDescription() {return this.description;}

    /**
     * @return The stock ticker Name
     */
    public String getTickerName(){return this.tickerName;}

    /**
     * @return Returns the price change as a % since last update
     * Capital gain or loss
     */
    public double getPriceChange(){return this.priceChange;}

    /**
     * @return Returns the dividend pay change as a % since
     * last update
     */
    public double getDivPayChange(){return this.divPayChange;}

    /**
     * Updates the stock price every round
     * risk is determined from a number from 1 to 9;
     * with 1 representing a 10% risk for stock to decline
     * up to a max risk of 9 having a 90% chance to decline
     *
     * For Dividend pay
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