package cc.akkaha.shop.controllers.model;

public class QueryShopPrice {

    private String date;
    private Integer current = 1;
    private Integer size = 10;

    public Integer getCurrent() {
        if (null == this.current) {
            return 10;
        } else {
            return this.current;
        }
    }

    public Integer getSize() {
        if (null == this.size) {
            return 10;
        } else {
            return this.size;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
