import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Product[] products = {new Product(40, "Хлеб"), new Product(70, "Молоко"),
                new Product(150, "Сыр"), new Product(400, "Колбаса"),
                new Product(170, "Печенье")};
        //File myFile = new File("basket.bin");
        File log = new File("log.csv");
        File jb = new File("basket.json");
        Basket basket = Basket.loadJson(jb);
        
      /* if (!myFile.exists()) {
            try {
                myFile.createNewFile();
                basket = new Basket(products);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }*/
        if (!jb.exists()) {
            try {
                jb.createNewFile();
                basket = new Basket(products);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        System.out.println("Добро пожаловать магазин! Обратите внимание на текущий ассортимент товаров:");
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i].getName() + " по цене: " + products[i].getPrice() + " руб.");
        }

        while (true) {
            try {
                System.out.println("Введите продукт который хотите положить в корзину в формате: номер_продукта количество. " +
                        "Для выхода наберите end.");
                String input = scanner.nextLine();
                if ("end".equals(input)) {
                    System.out.println("Программа завершена!");
                    scanner.close();
                    break;
                }
                String[] userChoice = input.split(" ");
                if (userChoice.length != 2) {
                    System.out.println("Ошибка в формате выбора продукта");
                    continue;
                }
                int prod = Integer.parseInt(userChoice[0]) - 1;
                int count = Integer.parseInt(userChoice[1]);
                if (prod >= products.length || prod < 0) {
                    System.out.println("Такого продукта нет в ассортименте");
                    continue;
                }
                if (count < 0) {
                    System.out.println("Вы уменьшили количество товара " + basket.getProductName(prod)
                            + " на " + -count);
                } else if (count == 0) {
                    System.out.println("Вы удалили товар " + basket.getProductName(prod) + " из корзины");
                    basket.addToCart(prod, count);
                }
                basket.addToCart(prod, count);
                ClientLog.logProd(prod, count);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода, вы ввели не число");
            }
        }
        basket.printCart();
        ClientLog.exportAsCSV(log);
        basket.saveJson(jb);
        scanner.close();
    }
}

