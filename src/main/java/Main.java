import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException,
            IOException, SAXException {
        boolean load = false;
        String loadFormat = null;
        boolean save = false;
        String saveFormat = null;
        boolean logging = false;
        File loadFile = null;
        File saveFile = null;
        File logFile = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("shop.xml"));
        Node root = doc.getDocumentElement();
        System.out.println(root.getNodeName());
        if (root.getNodeName().equals("config")) {
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node_ = nodeList.item(i);
                System.out.println(node_.getNodeName());
                if (node_.getNodeName().equals("load")) {
                    Element element = (Element) node_;
                    NamedNodeMap map = element.getAttributes();
                    for (int j = 0; j < map.getLength(); j++) {
                        System.out.println(map.item(j).getNodeName() + " " + map.item(j).getNodeValue());
                        if (map.item(j).getNodeName().equals("enabled")) {
                            load = map.item(j).getNodeValue().equals("false");
                        }
                        if (map.item(j).getNodeName().equals("fileName")) {
                            loadFile = new File(map.item(j).getNodeValue());
                        }
                        if (map.item(j).getNodeName().equals("format")) {
                            loadFormat = map.item(j).getNodeValue();
                        }
                    }
                }
                if (node_.getNodeName().equals("save")) {
                    Element element = (Element) node_;
                    NamedNodeMap map = element.getAttributes();
                    for (int j = 0; j < map.getLength(); j++) {
                        if (map.item(j).getNodeName().equals("enabled")) {
                            save = map.item(j).getNodeValue().equals("false");
                        }
                        if (map.item(j).getNodeName().equals("fileName")) {
                            saveFile = new File(map.item(j).getNodeValue());
                        }
                        if (map.item(j).getNodeName().equals("format")) {
                            saveFormat = map.item(j).getNodeValue();
                        }
                    }

                }
                if (node_.getNodeName().equals("log")) {
                    Element element = (Element) node_;
                    NamedNodeMap map = element.getAttributes();
                    for (int j = 0; j < map.getLength(); j++) {
                        if (map.item(j).getNodeName().equals("enabled")) {
                            logging = map.item(j).getNodeValue().equals("false");

                        }
                        if (map.item(j).getNodeName().equals("fileName")) {
                            logFile = new File(map.item(j).getNodeValue());
                        }
                    }
                }


            }
        } else {
            System.out.println("Неправильный формат конфигурационного файла");
        }

        Scanner scanner = new Scanner(System.in);
        Product[] products = {new Product(40, "Хлеб"), new Product(70, "Молоко"),
                new Product(150, "Сыр"), new Product(400, "Колбаса"),
                new Product(170, "Печенье")};
        Basket basket = null;

        if (load) {
            try {
                if (loadFormat.equals("json")) {
                    loadFile.createNewFile();
                    basket = Basket.loadJson(loadFile);
                }
                if (loadFormat.equals("text")) {
                    loadFile.createNewFile();
                    basket = Basket.loadFromTxtFile(loadFile);
                } else {
                    loadFile.createNewFile();
                    basket = new Basket(products);
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            basket = new Basket(products);
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
                if (logging) {
                    ClientLog.logProd(prod, count);
                }

            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода, вы ввели не число");
            }
        }
        basket.printCart();
        if (logging) {
            ClientLog.exportAsCSV(logFile);
        }
        if (save) {
            if (saveFormat.equals("json")) {
                basket.saveJson(saveFile);
            }
            if (saveFormat.equals("text")) {
                basket.saveTxt(saveFile);
            }
            scanner.close();
        }
    }
}

