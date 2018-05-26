package cc.akkaha.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

public class OrderBillSummary {

    private Integer totalCount = 0;
    private String totalWeight;
    private String meanWeight;
    private String totalPrice;
    private String meanPrice;

    @JsonIgnore
    public BigDecimal calcTotalWeight = BigDecimal.ZERO;
    @JsonIgnore
    public BigDecimal calcTotalPrice = BigDecimal.ZERO;

    public void addCount(Integer count) {
        this.totalCount = this.totalCount + count;
    }

    public void summarize() {
        BigDecimal divisor = new BigDecimal(this.totalCount);
        this.setTotalWeight(this.calcTotalWeight.stripTrailingZeros().toPlainString());
        this.setMeanWeight(this.calcTotalWeight.divide(divisor, 2, BigDecimal.ROUND_HALF_UP)
                .stripTrailingZeros().toPlainString()
        );
        this.setTotalPrice(this.calcTotalPrice.stripTrailingZeros().toPlainString());
        this.setMeanPrice(
                this.calcTotalPrice.divide(divisor, 2, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros().toPlainString()
        );
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getMeanWeight() {
        return meanWeight;
    }

    public void setMeanWeight(String meanWeight) {
        this.meanWeight = meanWeight;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getMeanPrice() {
        return meanPrice;
    }

    public void setMeanPrice(String meanPrice) {
        this.meanPrice = meanPrice;
    }
}
