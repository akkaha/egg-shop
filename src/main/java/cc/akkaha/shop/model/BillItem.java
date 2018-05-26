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

    public BillItem() {

    }

    public BillItem(String weight, String price, Integer user, Integer count, Integer level) {
        this.weight = weight;
        this.price = price;
        this.user = user;
        this.count = count;
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
}
