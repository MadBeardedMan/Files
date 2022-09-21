import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Basket {
    Map<Integer, Product> basket = new HashMap<>();

    public Basket(Product[] basket) {
        for (int i = 0; i < basket.length; i++) {
            this.basket.put(i, basket[i]);
        }
    }

    public Basket(HashMap<Integer, Product> basket) {
        this.basket.putAll(basket);
    }

    static Basket loadFromTxtFile(File textFile) {
        ArrayList<Product> prod = new ArrayList<>();
        try (FileReader reader = new FileReader(textFile)) {
            Scanner scanner = new Scanner(reader);
            while (scanner.hasNextLine()) {
                String[] buf = scanner.nextLine().split(" ");
                prod.add(new Product(Integer.parseInt(buf[3]), buf[2], Integer.parseInt(buf[1])));
            }
            scanner.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        Product[] prd = new Product[prod.size()];
        prd = prod.toArray(prd);
        return new Basket(prd);
    }

    static Basket loadFromBinFile(File file) {
        HashMap<Integer, Product> basket = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            basket = (HashMap<Integer, Product>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return new Basket(basket);
    }

    public void addToCart(int productNum, int amount) {
        Product prod = basket.get(productNum);
        prod.addCount(amount);
        basket.replace(productNum, prod);
    }

    public void printCart() {
        int summary = 0;
        System.out.println("Ваша корзина:");
        for (int i = 0; i < basket.size(); i++) {
            if (basket.get(i).getCount() > 0) {
                System.out.println((i + 1) + ". - " + basket.get(i).getName() + " " +
                        basket.get(i).getCount() + " шт." + " - по цене "
                        + basket.get(i).getPrice() + " руб."
                        + " на сумму: " + basket.get(i).getCount() * basket.get(i).getPrice() + " руб.");
                summary = summary + (basket.get(i).getCount() * basket.get(i).getPrice());
            }
        }
        System.out.println("Общая сумма товаров в вашей корзине:" + summary + " руб.");
    }

    public String getProductName(int prod) {
        return basket.get(prod).getName();
    }

    public void saveTxt(File textFile) {
        try {
            FileWriter fw = new FileWriter(textFile);
            for (int i = 0; i < basket.size(); i++) {
                fw.append(Character.forDigit(i, 10));
                fw.append(" ");
                fw.write(basket.get(i).toString());
                fw.append("\n");
                fw.flush();
            }
            fw.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void saveBin(File file) {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(basket);
            oos.flush();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
