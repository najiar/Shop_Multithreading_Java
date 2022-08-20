import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Hashtable;
import java.util.Set;

public class CashDeskThread implements Runnable, Serializable
{
    private CashDesk cashDesk;
    private Shop shop;
    private Cashier cashier;
    private Hashtable<Goods, Integer> goodsToBeSold;
    private double customerBudget;
    private int shoppingBoxID;
    private Receipt receipt;
    public static boolean isReceipt = false;
    public static double PRICEE;

    public CashDeskThread(Shop shop, CashDesk cashDesk, double customerBudget, Cashier cashier, Hashtable<Goods, Integer> goodsToBeSold, int shoppingBoxID)
    {

        this.cashDesk = cashDesk;
        this.cashier = cashier;
        this.goodsToBeSold = new Hashtable<>(goodsToBeSold);
        this.customerBudget = customerBudget;
        this.shop = shop;
        this.shoppingBoxID = shoppingBoxID;
    }

    @Override
    public void run()
    {
        Set<Goods> keys = goodsToBeSold.keySet();
        double priceForReceipt = 0;
        double priceToShowInPrint = 0;

        for(Goods key: keys)
        {

            if(key.getCategory() == Category.NON_FOOD)
            {
                priceForReceipt += (goodsToBeSold.get(key) * key.getPrice());
                priceForReceipt += priceForReceipt * shop.getNonFoodMarkUp();
                priceToShowInPrint = key.getPrice() +  (key.getPrice() * shop.getNonFoodMarkUp());
                System.out.println( "|| Cashier name: || " + this.cashier.getName() + " || sold the Good: "+ key.getName() + " || Quantity: " + goodsToBeSold.get(key) + " || " + "At Price: || " + priceToShowInPrint + " ||");


            }
            else
            {
                priceForReceipt += (goodsToBeSold.get(key) * key.getPrice());
                priceForReceipt += priceForReceipt * shop.getFoodMarkUp();
                priceToShowInPrint = key.getPrice() +  (key.getPrice() * shop.getFoodMarkUp());
                System.out.println( "|| Cashier name: || " + this.cashier.getName() + " || sold the Good: "+ key.getName() + " || Quantity: " + goodsToBeSold.get(key) + " || " + "At Price: || " + priceToShowInPrint + " ||");
            }
        }


        if(customerBudget >= priceForReceipt)
        {
            String file = "Shop_" + shop.getName();
            File filesDirectory = new File(file);
            filesDirectory.mkdir();
            String filePath = "";

            receipt = new Receipt(shop, cashier, LocalDate.now(), goodsToBeSold, priceForReceipt);

            filePath = file +"/Receipt_"+ receipt.getId() +".ser";

            receipt.serializeReceipt(filePath);

            shop.setTotalProfit(shop.getTotalProfit() + priceForReceipt);
        }
        else
        {
            try {
                throw new InsufficientMoneyException("Customer with shoppingBox ID: " + shoppingBoxID + " Does not have enough money!!");
            } catch (InsufficientMoneyException e) {
                e.printStackTrace();
            }

            for(Goods key: keys)
            {
                shop.returnReserved(key, goodsToBeSold.get(key));
            }

        }
    }
}
