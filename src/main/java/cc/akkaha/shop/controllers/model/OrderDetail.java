package cc.akkaha.shop.controllers.model;

import cc.akkaha.shop.db.model.OrderItem;
import cc.akkaha.shop.db.model.ShopOrder;

import java.util.List;

public class OrderDetail {

    private ShopOrder order;
    private List<OrderItem> items;

    public OrderDetail(ShopOrder order, List<OrderItem> items) {
        this.order = order;
        this.items = items;
    }

    public ShopOrder getOrder() {
        return order;
    }

    public void setOrder(ShopOrder order) {
        this.order = order;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
