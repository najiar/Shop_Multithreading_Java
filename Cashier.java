import java.io.Serializable;

public class Cashier implements Serializable
{
    private long id;
    private String name;
    private double salary;

    public Cashier(long id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public String getName() {
        return name;
    }
}
