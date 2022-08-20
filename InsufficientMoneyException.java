public class InsufficientMoneyException extends Exception
{
    public InsufficientMoneyException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "InsufficientMoneyException [" + super.getMessage() + "]";
    }
}


