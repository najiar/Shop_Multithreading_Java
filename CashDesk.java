import java.io.Serializable;

public class CashDesk implements Serializable
{
    private Cashier cashier;


    public CashDesk(Cashier cashier)
    {
        this.cashier = cashier;
    }


    public Cashier getCashier()
    {
        return this.cashier;
    }



}
