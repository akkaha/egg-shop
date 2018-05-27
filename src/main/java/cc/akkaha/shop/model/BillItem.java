package cc.akkaha.shop.model;

/**
 * for front end display
 */
public class BillItem {

    private String weight;
    private String price;
    private Integer user;
    private Integer count;
    private Integer level;
    private String totalWeight;
    private String totalPrice;

    public BillItem() {

    }

    public BillItem(String weight, String price, Integer user, Integer count, Integer level, String totalWeight, String totalPrice) {
        this.weight = weight;
        this.price = price;
        this.user = user;
        this.count = count;
        this.level = level;
        this.totalWeight = totalWeight;
        this.totalPrice = totalPrice;
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

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
