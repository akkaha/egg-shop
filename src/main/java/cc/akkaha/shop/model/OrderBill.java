package cc.akkaha.shop.model;

import cc.akkaha.shop.db.model.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrderBill {

    private String date;
    private Integer totalCount;
    private String totalWeight;
    private String meanWeight;
    private String totalPrice;
    private String meanPrice;
    private PriceExtra priceExtra;
    private List<BillItem> items = new ArrayList<>();
    private OrderBillSummary sixSummary = new OrderBillSummary();
    private OrderBillSummary sevenSummary = new OrderBillSummary();
    // 转字符串前端读
    private TreeMap<String, String> sixPriceRange = new TreeMap<>();
    // 转字符串前端读
    private TreeMap<String, String> sevenPriceRange = new TreeMap<>();
    private String remark = StringUtils.EMPTY;

    // this be used by controller to return to front
    @JsonIgnore
    private OrderBillInner inner = new OrderBillInner();

    public static OrderBill parse(String date, List<OrderItem> orderItems,
                                  PriceRange priceRange,
                                  cc.akkaha.shop.db.model.PriceExtra priceExtra) {
        OrderBill bill = new OrderBill();
        bill.getInner().priceExtra = priceExtra;
        if (null != priceExtra) {
            bill.setPriceExtra(
                    new PriceExtra(priceExtra.getWeightAdjust().stripTrailingZeros().toPlainString())
            );
        }
        bill.setDate(date);
        if (null != priceRange) {
            TreeMap<String, String> billSixPriceRange = bill.getSixPriceRange();
            priceRange.getSixLevel().forEach((k, v) ->
                    billSixPriceRange.put(
                            k.stripTrailingZeros().toPlainString(),
                            v.stripTrailingZeros().toPlainString()
                    )
            );
            TreeMap<String, String> billSevenPriceRange = bill.getSevenPriceRange();
            priceRange.getOriginSevenLevel().forEach((k, v) ->
                    billSevenPriceRange.put(
                            k.stripTrailingZeros().toPlainString(),
                            v.stripTrailingZeros().toPlainString()
                    )
            );
        }
        for (OrderItem item : orderItems) {
            BigDecimal itemWeight = item.getWeight();
            BigDecimal itemPrice = null;
            BigDecimal itemCount = new BigDecimal(item.getCount());
            BigDecimal itemTotalWeight = itemWeight.multiply(itemCount);
            BigDecimal itemTotalPrice = BigDecimal.ZERO;
            if (6 == item.getLevel()) {
                bill.sixSummary.addCount(item.getCount());
                bill.sixSummary.calcTotalWeight =
                        bill.sixSummary.calcTotalWeight.add(itemTotalWeight);
                itemPrice = calcItemPrice(itemWeight, priceRange.getSixLevel(), priceExtra);
                if (null != itemPrice) {
                    itemTotalPrice = itemPrice.multiply(itemCount);
                    bill.sixSummary.calcTotalPrice = bill.sixSummary.calcTotalPrice.add(itemTotalPrice);
                }
            } else if (7 == item.getLevel()) {
                bill.sevenSummary.addCount(item.getCount());
                bill.sevenSummary.calcTotalWeight =
                        bill.sevenSummary.calcTotalWeight.add(itemTotalWeight);
                itemPrice = calcItemPrice(itemWeight, priceRange.getSevenLevel(), priceExtra);
                if (null != itemPrice) {
                    itemTotalPrice = itemPrice.multiply(itemCount);
                    bill.sevenSummary.calcTotalPrice =
                            bill.sevenSummary.calcTotalPrice.add(itemTotalPrice);
                }

            }
            if (null != itemPrice) {
                bill.items.add(new BillItem(
                        itemWeight.stripTrailingZeros().toPlainString(),
                        itemPrice.stripTrailingZeros().toPlainString(),
                        item.getUser(),
                        item.getCount(),
                        item.getLevel(),
                        itemTotalWeight.stripTrailingZeros().toPlainString(),
                        itemTotalPrice.stripTrailingZeros().toPlainString()
                ));
            } else {
                bill.items.add(new BillItem(
                        itemWeight.stripTrailingZeros().toPlainString(),
                        StringUtils.EMPTY,
                        item.getUser(),
                        item.getCount(),
                        item.getLevel(),
                        itemTotalWeight.stripTrailingZeros().toPlainString(),
                        itemTotalPrice.stripTrailingZeros().toPlainString()
                ));
            }
        }
        bill.sixSummary.summarize();
        bill.sevenSummary.summarize();
        bill.setTotalCount(bill.sixSummary.getTotalCount() + bill.sevenSummary.getTotalCount());
        BigDecimal totalWeight = bill.sixSummary.calcTotalWeight.add(bill.sevenSummary.calcTotalWeight);
        BigDecimal totalPrice = bill.sixSummary.calcTotalPrice.add(bill.sevenSummary.calcTotalPrice);
        bill.setTotalWeight(totalWeight.stripTrailingZeros().toPlainString());
        bill.setTotalPrice(totalPrice.stripTrailingZeros().toPlainString());
        if (orderItems.isEmpty()) {
            bill.setMeanWeight(BigDecimal.ZERO.stripTrailingZeros().toPlainString());
            bill.setMeanPrice(BigDecimal.ZERO.stripTrailingZeros().toPlainString());
        } else {
            BigDecimal divisor = new BigDecimal(bill.getTotalCount());
            bill.setMeanWeight(totalWeight.divide(divisor, 2, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString());
            bill.setMeanPrice(totalPrice.divide(divisor, 2, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString());
        }
        return bill;
    }

    /**
     * Pl: floor price, Pc: ceiling price, Wf: floor weight, Wc: ceiling weight, Wn: current weight
     * Pl + (Pc - Pl) / (Wc - Wl) * (Wn + Wa - Wf)
     */
    public static BigDecimal calcItemPrice(
            BigDecimal weight,
            TreeMap<BigDecimal, BigDecimal> priceRange,
            cc.akkaha.shop.db.model.PriceExtra priceExtra
    ) {
        if (null != priceRange) {
            BigDecimal adjustedWeight = weight;
            if (null != priceExtra && null != priceExtra.getWeightAdjust()) {
                adjustedWeight = weight.add(priceExtra.getWeightAdjust());
            }
            Map.Entry<BigDecimal, BigDecimal> ceilingEntry = priceRange.ceilingEntry
                    (adjustedWeight);
            Map.Entry<BigDecimal, BigDecimal> floorEntry = priceRange.floorEntry(adjustedWeight);
            if (null == ceilingEntry || null == floorEntry) {
                return null;
            } else {
                BigDecimal ceilingPrice = ceilingEntry.getValue();
                BigDecimal floorPrice = floorEntry.getValue();
                if (ceilingPrice.equals(floorPrice)) {
                    return ceilingPrice;
                }
                BigDecimal ceilingWeight = ceilingEntry.getKey();
                BigDecimal floorWeight = floorEntry.getKey();
                BigDecimal price = floorPrice.add(
                        ceilingPrice.subtract(floorPrice)
                                .divide(ceilingWeight.subtract(floorWeight),
                                        2,
                                        BigDecimal.ROUND_HALF_UP)
                                .multiply(adjustedWeight.subtract(floorWeight))
                );
                return price;
            }
        } else {
            return null;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public List<BillItem> getItems() {
        return items;
    }

    public void setItems(List<BillItem> items) {
        this.items = items;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public PriceExtra getPriceExtra() {
        return priceExtra;
    }

    public void setPriceExtra(PriceExtra priceExtra) {
        this.priceExtra = priceExtra;
    }

    public OrderBillInner getInner() {
        return inner;
    }

    public void setInner(OrderBillInner inner) {
        this.inner = inner;
    }

    public TreeMap<String, String> getSixPriceRange() {
        return sixPriceRange;
    }

    public void setSixPriceRange(TreeMap<String, String> sixPriceRange) {
        this.sixPriceRange = sixPriceRange;
    }

    public TreeMap<String, String> getSevenPriceRange() {
        return sevenPriceRange;
    }

    public void setSevenPriceRange(TreeMap<String, String> sevenPriceRange) {
        this.sevenPriceRange = sevenPriceRange;
    }

    public OrderBillSummary getSixSummary() {
        return sixSummary;
    }

    public void setSixSummary(OrderBillSummary sixSummary) {
        this.sixSummary = sixSummary;
    }

    public OrderBillSummary getSevenSummary() {
        return sevenSummary;
    }

    public void setSevenSummary(OrderBillSummary sevenSummary) {
        this.sevenSummary = sevenSummary;
    }
}
