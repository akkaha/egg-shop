package cc.akkaha.shop.controllers.model;

import cc.akkaha.shop.model.PriceItem;

import java.util.List;

public class OrderBillInsert {

    List<PriceItem> items;
    PriceExtraItem priceExtra;

    public List<PriceItem> getItems() {
        return items;
    }

    public void setItems(List<PriceItem> items) {
        this.items = items;
    }

    public PriceExtraItem getPriceExtra() {
        return priceExtra;
    }

    public void setPriceExtra(PriceExtraItem priceExtra) {
        this.priceExtra = priceExtra;
    }
}
