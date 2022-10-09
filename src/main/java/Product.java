public class Product {
    private final int price;
    private final String name;
    private int count = 0;

    public Product(int price, String name) {
        this.price = price;
        this.name = name;
    }

    public Product(int price, String name, int count) {
        this.price = price;
        this.name = name;
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void addCount(int count) {
        if (count == 0) {
            this.count = 0;
        } else {
            this.count = this.count + count;
        }
    }

    @Override
    public String toString() {
        String prod = this.count + " " + this.name + " " + this.price;
        return prod;
    }

    public int getCount() {
        return count;
    }
}
