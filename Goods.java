import java.io.Serializable;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class Goods implements Serializable
{
    private long id;
    private String name;
    private double price;
    private Category category;
    private LocalDate expireData;


    public Goods(long id, String name, int price, Category category, LocalDate expireData)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.expireData = expireData;
    }

    public LocalDate getExpireData() {
        return expireData;
    }

    public double getPrice()
    {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public String getName()
    {
        return name;
    }

    public void fixPrice(int expireDays, double prcnt)
    {
        if (DAYS.between(LocalDate.now(), this.expireData) <= expireDays)
        {
            this.price = price - (price * (prcnt / 100));
        }
    }
}

