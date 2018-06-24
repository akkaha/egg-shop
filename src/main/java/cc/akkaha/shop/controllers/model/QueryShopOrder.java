package cc.akkaha.shop.controllers.model;

import cc.akkaha.shop.db.model.ShopOrder;

public class QueryShopOrder extends ShopOrder {

    private Integer current = 1;
    private Integer size = 10;
    private String start;
    private String end;

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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
