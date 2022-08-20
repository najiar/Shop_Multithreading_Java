import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static Receipt deserializeReceipt(String filePath)
    {
        Receipt Invoice = null;

        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream inputStream = new ObjectInputStream(fis);)
        {
            Invoice = (Receipt) inputStream.readObject();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return Invoice;
    }

    public static void main(String[] args) throws IOException {
        Goods goods_1 = new Goods(001, "Meet", 40, Category.FOOD, LocalDate.of(2023, 05, 28));

        Goods goods_2 = new Goods(002, "Chicken", 50, Category.FOOD, LocalDate.of(2023, 05, 30));
        Goods goods_3 = new Goods(003, "Sandwich", 60, Category.FOOD, LocalDate.of(2024, 04, 03));

        Goods goods_4 = new Goods(004, "Hand-Cream", 70, Category.NON_FOOD, LocalDate.of(2022, 05, 31));


        Shop shop = new Shop("BILA", 0.25, 0.20);
        shop.addGoodsToShop(goods_1, 100);
        shop.addGoodsToShop(goods_2, 100);
        shop.addGoodsToShop(goods_3, 100);
        shop.addGoodsToShop(goods_4, 80);

        Cashier cashier_1 = new Cashier(111, "Jahn", 700);
        Cashier cashier_2 = new Cashier(112, "Naje", 700);
        Cashier cashier_3 = new Cashier(112, "Ivan", 700);

        CashDesk cashDesk_1 = new CashDesk(cashier_1);
        CashDesk cashDesk_2 = new CashDesk(cashier_2);
        CashDesk cashDesk_3 = new CashDesk(cashier_3);

        shop.addCashDesk(cashDesk_1);
        shop.addCashDesk(cashDesk_2);
        shop.addCashDesk(cashDesk_3);

        System.out.println("Available goods in the shop: ");
        shop.showGoodsAvailable();
        System.out.println("");
        System.out.println("Available but expired: ");
        shop.showOutDatedFoods();
        System.out.println();

        /////////////////////////////////////////////////////////////////////////

        List<ShoppingBox> shoppingBox = new ArrayList<>();

        try {
            ShoppingBox item_1 = new ShoppingBox(shop, 40000);
            item_1.addItem(goods_1, 10);
            item_1.addItem(goods_2, 10);
            item_1.addItem(goods_3, 10);
            item_1.addItem(goods_4, 10);
            shoppingBox.add(item_1);

            ShoppingBox item_2 = new ShoppingBox(shop, 50000);
            item_2.addItem(goods_1, 30);
            item_2.addItem(goods_2, 30);
            item_2.addItem(goods_3, 30);
            item_2.addItem(goods_4, 30);
            shoppingBox.add(item_2);

            ShoppingBox item_3 = new ShoppingBox(shop, 40000);
            item_3.addItem(goods_1, 20);
            item_3.addItem(goods_2, 20);
            item_3.addItem(goods_3, 20);
            item_3.addItem(goods_4, 20);
            shoppingBox.add(item_3);

            ShoppingBox item_4 = new ShoppingBox(shop, 20000);
            item_4.addItem(goods_1, 20);
            item_4.addItem(goods_2, 20);
            item_4.addItem(goods_3, 20);
            item_4.addItem(goods_4, 20);
            shoppingBox.add(item_4);
        }
        catch (InsufficientGoodsQuantityException e)
        {
            e.printStackTrace();
        }

        /////////////////////////////////////////////////////////////////////////




        shop.openShop(shoppingBox);

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println();
        /////////////////////////////////////////////////////////////////////////

        System.out.println("Available goods in the shop: ");
        shop.showGoodsAvailable();
        System.out.println();

        System.out.println("Available but expired: ");
        shop.showOutDatedFoods();
        System.out.println();
        System.out.println();

        System.out.println("EXPENSES: " + shop.getTotalExpenses() + "\n");
        System.out.println("INCOME: " + shop.getTotalProfit()+ "\n");
        double profit = shop.getTotalProfit() - shop.getTotalExpenses();
        System.out.println("PROFIT: " + profit);

        System.out.println("");


        int count = new File("Shop_" + shop.getName()).list().length;

        int totalReceiptsTillNow = 0;
        int totalReceiptsForData = 0;

        for(int i = 1; i <= count; i++)
        {
            Path fileNamePath = Path.of("Shop_" + shop.getName() + "/Receipt_" + i + ".ser");
            String fileName = "Shop_" + shop.getName() + "/Receipt_" + i + ".ser";
            BasicFileAttributes crTm = null;
            try
            {
                crTm = Files.readAttributes(fileNamePath, BasicFileAttributes.class);
            }
            catch (IOException ex)
            {

            }

            FileTime fileTime = crTm.creationTime();
            LocalTime dateStart =  LocalTime.of(18, 8, 00);
            LocalDateTime addDateStart = dateStart.atDate(LocalDate.parse(String.valueOf(LocalDate.now())));
            Instant instantStart = addDateStart.atZone(ZoneId.systemDefault()).toInstant();

            LocalTime dateEnd =  LocalTime.of(18, 8, 59);
            LocalDateTime addDateEnd = dateEnd.atDate(LocalDate.parse(String.valueOf(LocalDate.now())));
            Instant instantEnd = addDateEnd.atZone(ZoneId.systemDefault()).toInstant();


            LocalDateTime addDateNow = LocalDateTime.now();
            Instant instantNow = addDateNow.atZone(ZoneId.systemDefault()).toInstant();

            if(fileTime.toInstant().isAfter(instantStart) && fileTime.toInstant().isBefore(instantNow))
            {
                System.out.println(deserializeReceipt(fileName));
                System.out.println();
                totalReceiptsForData++;
            }
            totalReceiptsTillNow++;

        }
        System.out.println("Total Receipts from this shop: "  + totalReceiptsTillNow);
        System.out.println("Total Receipts for the data specified: "  + totalReceiptsForData);
    }
}
