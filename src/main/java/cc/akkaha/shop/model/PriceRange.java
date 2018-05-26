package cc.akkaha.shop.model;

import java.math.BigDecimal;
import java.util.TreeMap;

public class PriceRange {

    private TreeMap<BigDecimal, BigDecimal> sixLevel = new TreeMap();
    private TreeMap<BigDecimal, BigDecimal> sevenLevel = new TreeMap();

    public void addSixLevel(BigDecimal weight, BigDecimal price) {
        sixLevel.put(weight, price);
    }

    public void addSevenLevel(BigDecimal weight, BigDecimal price) {
        sixLevel.put(weight, price);
    }

    public TreeMap<BigDecimal, BigDecimal> getSixLevel() {
        return sixLevel;
    }

    public void setSixLevel(TreeMap<BigDecimal, BigDecimal> sixLevel) {
        this.sixLevel = sixLevel;
    }

    public TreeMap<BigDecimal, BigDecimal> getSevenLevel() {
        return sevenLevel;
    }

    public void setSevenLevel(TreeMap<BigDecimal, BigDecimal> sevenLevel) {
        this.sevenLevel = sevenLevel;
    }
}
