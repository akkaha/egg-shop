package cc.akkaha.shop.controllers.model;

import cc.akkaha.shop.db.model.ShopUser;

import java.util.List;

public class NewOrder {

    private ShopUser user;
    private List<String> sixWeights;
    private List<String> sevenWeights;

    public ShopUser getUser() {
        return user;
    }

    public void setUser(ShopUser user) {
        this.user = user;
    }

    public List<String> getSixWeights() {
        return sixWeights;
    }

    public void setSixWeights(List<String> sixWeights) {
        this.sixWeights = sixWeights;
    }

    public List<String> getSevenWeights() {
        return sevenWeights;
    }

    public void setSevenWeights(List<String> sevenWeights) {
        this.sevenWeights = sevenWeights;
    }
}
