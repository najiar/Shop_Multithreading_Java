import java.time.LocalDate;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static java.time.temporal.ChronoUnit.DAYS;

public class ShoppingBox
{
    private Hashtable<Goods, Integer> box;

    private double buyersBudget;

    private Shop shop;

    private static long id = 0;


    public double getBuyersBudget()
    {
        return buyersBudget;
    }

    public ShoppingBox(Shop shop, double buyersBudget)
    {
        this.buyersBudget = buyersBudget;
        this.box = new Hashtable<>();
        this.shop = shop;
        this.id++;

    }

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        ShoppingBox.id = id;
    }

    public Hashtable<Goods, Integer> getBox() {
        return box;
    }

    public void showGoods()
    {
        Set<Goods> keys = this.box.keySet();

        for(Goods key: keys)
        {
            System.out.println( "|| Good: "+ key.getName()  + " || Quantity: " + box.get(key) + " || Price pr.1: || " + key.getPrice() + " ||");
        }
    }


    public void addItem(Goods goods, Integer quantity) throws InsufficientGoodsQuantityException {
        if((DAYS.between(LocalDate.now(), goods.getExpireData()) <= -1))
        {
            return;
        }
        else
        {
            if(shop.getGoods().get(goods) >= quantity)
            {
                this.box.put(goods, quantity);
                this.shop.removeSoldGoods(goods, quantity);
            }
            else
            {
                throw new InsufficientGoodsQuantityException("Insufficient " + goods.getName() + " in Shop. only: ||" + shop.getGoods().get(goods) + "|| is available,  ShoppingBox with ID: ||" + this.id + "||" );
            }

        }

    }



}
