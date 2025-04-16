package model;

public class CoinAcceptor extends PayModel {
    private int amount;


    public CoinAcceptor(int sum) {
        super(sum);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
