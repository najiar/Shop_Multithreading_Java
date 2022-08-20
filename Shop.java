import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class Shop implements Serializable
{
    private String name;
    private Hashtable<Goods, Integer> allGoods;
    private Hashtable<Goods, Integer> outDatedGoods;
    private List<Cashier> cashiers;
    private List<CashDesk> cashDesks;
    private double totalExpenses = 0;
    private double totalProfit = 0;
    private double foodMarkUp;
    private double nonFoodMarkUp;
    public static String shopName = "";


    public int fileCount = 0;


    public Shop(String name, double foodMarkUp, double nonFoodMarkUp)
    {
        this.name = name;
        this.allGoods = new Hashtable<>();
        this.outDatedGoods = new Hashtable<>();
        this.cashiers = new ArrayList<>();
        this.cashDesks = new ArrayList<>();
        this.foodMarkUp = foodMarkUp;
        this.nonFoodMarkUp = nonFoodMarkUp;
        shopName = name;
    }

    public String getName() {
        return name;
    }

    public double getFoodMarkUp() {
        return foodMarkUp;
    }

    public double getNonFoodMarkUp() {
        return nonFoodMarkUp;
    }


    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }


    public Hashtable<Goods, Integer> getGoods() {
        return this.allGoods;
    }

    public void addGoodsToShop(Goods goods, Integer quantity)
    {

        if ((DAYS.between(LocalDate.now(), goods.getExpireData()) <= -1))
        {
            this.outDatedGoods.put(goods, quantity);
            this.totalExpenses += (goods.getPrice() * quantity);
            return;
        }
        else
        {
            this.allGoods.put(goods, quantity);
            this.totalExpenses += (goods.getPrice() * quantity);
        }
    }

    public void removeSoldGoods(Goods goods, Integer quantity)
    {
        Integer quantitySold = this.allGoods.get(goods) - quantity;
        this.allGoods.put(goods, quantitySold);
    }

    public void returnReserved(Goods goods, Integer quantity)
    {
        Integer quantityReserved = this.allGoods.get(goods) + quantity;
        this.allGoods.put(goods, quantityReserved);
    }


    public void showGoodsAvailable()
    {
        Set<Goods> keys = allGoods.keySet();
        double priceForGood = 0;

        for(Goods key: keys)
        {

            if(key.getCategory() == Category.FOOD)
            {
                priceForGood = key.getPrice() * this.getFoodMarkUp();
                priceForGood += key.getPrice();
                System.out.println( "|| Good: "+ key.getName()  + " || Quantity: " + allGoods.get(key) + " || Price for one: || " + priceForGood + " ||");

            }
            else
            {
                priceForGood = key.getPrice() * this.getNonFoodMarkUp();
                priceForGood += key.getPrice();
                System.out.println( "|| Good: "+ key.getName()  + " || Quantity: " + allGoods.get(key) + " || Price for one: || " + priceForGood + " ||");

            }
        }
    }

    public void showOutDatedFoods()
    {
        Set<Goods> keys = this.outDatedGoods.keySet();

        double priceForGood = 0;

        for(Goods key: keys)
        {

            if(key.getCategory() == Category.FOOD)
            {
                priceForGood = key.getPrice() * this.getFoodMarkUp();
                priceForGood += key.getPrice();
                System.out.println( "|| Good: "+ key.getName()  + " || Quantity: " + outDatedGoods.get(key) + " || Price for one: || " + priceForGood + " ||");

            }
            else
            {
                priceForGood = key.getPrice() * this.getNonFoodMarkUp();
                priceForGood += key.getPrice();
                System.out.println( "|| Good: "+ key.getName()  + " || Quantity: " + outDatedGoods.get(key) + " || Price for one: || " + priceForGood + " ||");

            }
        }
    }

    public void addCashDesk(CashDesk cashDesk)
    {
        this.cashDesks.add(cashDesk);
        totalExpenses += cashDesk.getCashier().getSalary();
    }

    public void openShop(List<ShoppingBox> shoppingBox)
    {
        int size = shoppingBox.size();
        int counter = 0;

        Set<Goods> keys = shoppingBox.get(counter).getBox().keySet();
        for(Goods key: keys)
        {
            key.fixPrice(7, 20);
        }

        while (counter < size)
        {
            for (CashDesk cashDesk : this.cashDesks)
            {
                if(counter == shoppingBox.size())
                {
                    break;
                }

                Thread thread = new Thread(new CashDeskThread(this, cashDesk, shoppingBox.get(counter).getBuyersBudget(), cashDesk.getCashier(), shoppingBox.get(counter).getBox(), counter));
                thread.start();
                counter++;
            }
            try
            {

                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }



        }
        System.out.println("Number of active threads from the given thread: " + Thread.activeCount());
    }
}



