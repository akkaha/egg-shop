package cc.akkaha.shop.model;

public class PriceItem {

    private String weight;
    private String price;
    private Integer level;

    public PriceItem() {
    }

    public PriceItem(String weight, String price, Integer level) {
        this.weight = weight;
        this.price = price;
        this.level = level;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
