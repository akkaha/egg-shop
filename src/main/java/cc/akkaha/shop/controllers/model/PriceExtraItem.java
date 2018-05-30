package cc.akkaha.shop.controllers.model;

import cc.akkaha.shop.model.PriceExtra;

public class PriceExtraItem extends PriceExtra {

    private Integer id;

    public PriceExtraItem() {

    }

    public PriceExtraItem(Integer id, String weightAdjust) {
        this.id = id;
        this.setWeightAdjust(weightAdjust);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
