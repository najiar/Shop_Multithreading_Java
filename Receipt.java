import java.io.*;
import java.time.LocalDate;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Receipt implements Serializable
{
    private long id = 0;
    private Cashier cashier;
    private LocalDate dateAndTime;
    private Hashtable<Goods, Integer> goodsSold;
    private double soldGoodPrice;
    static  int countFile  = new File("Shop_" + Shop.shopName).list().length;;
    private static final AtomicInteger count = new AtomicInteger(countFile);
    private Shop shop;

    public Receipt(){}

    public Receipt(Shop shop, Cashier cashier, LocalDate dateAndTime, Hashtable<Goods, Integer> goodsSold, double soldGoodPrice)
    {

        this.cashier = cashier;
        this.dateAndTime = dateAndTime;
        this.goodsSold = goodsSold;
        this.soldGoodPrice = soldGoodPrice;
        this.shop = shop;
        this.id = count.incrementAndGet();
    }


    public  long getId() {
        return id;
    }

    public void serializeReceipt(String filePath)
    {
        Receipt rec = this;
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream outputStream = new ObjectOutputStream(fos))
        {
            outputStream.writeObject(rec);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public LocalDate getDateAndTime() {
        return dateAndTime;
    }

    @Override
    public String toString() {
        Set<Goods> keys = goodsSold.keySet();

        String outputGoods = "";
        double priceForGood = 0;

        for (Goods key : keys) {

            if(key.getCategory() == Category.FOOD)
            {
                priceForGood = key.getPrice() * shop.getFoodMarkUp();
                priceForGood += key.getPrice();
                outputGoods +=  "* " + key.getName() + " " + "Quantity: " + goodsSold.get(key) + ", Price for one: " + priceForGood + ", Total price for this good: " + priceForGood * goodsSold.get(key)  + "\n";

            }
            else
            {
                priceForGood = key.getPrice() * shop.getNonFoodMarkUp();
                priceForGood += key.getPrice();
                outputGoods +=  "* " + key.getName() + " " + "Quantity: " + goodsSold.get(key) + ", Price for one: " + priceForGood + ", Total price for this good: " + priceForGood * goodsSold.get(key)  + "\n";

            }

        }

        return "Receipt{" +
                "Cashier: " + cashier.getName() +
                ", Date of issue: " + dateAndTime + "}" + "\n" +
                "Goods:  "+ "\n" + outputGoods +
                "Total price for Receipt: " + soldGoodPrice;
    }
}
