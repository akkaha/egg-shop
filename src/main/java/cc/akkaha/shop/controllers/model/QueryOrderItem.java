package cc.akkaha.shop.controllers.model;

import cc.akkaha.shop.db.model.OrderItem;

public class QueryOrderItem extends OrderItem {

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
}
