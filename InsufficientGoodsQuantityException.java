public class InsufficientGoodsQuantityException extends Exception
{
    public InsufficientGoodsQuantityException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "InsufficientGoodsQuantityException [" + super.getMessage() + "]";
    }
}

