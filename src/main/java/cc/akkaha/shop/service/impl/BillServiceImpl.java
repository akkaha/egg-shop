package cc.akkaha.shop.service.impl;

import cc.akkaha.shop.db.model.OrderItem;
import cc.akkaha.shop.db.model.Price;
import cc.akkaha.shop.db.service.OrderItemService;
import cc.akkaha.shop.db.service.PriceExtraService;
import cc.akkaha.shop.db.service.PriceService;
import cc.akkaha.shop.model.OrderBill;
import cc.akkaha.shop.model.PriceRange;
import cc.akkaha.shop.service.BillService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    BigDecimal six = new BigDecimal("6");
    BigDecimal seven = new BigDecimal("7");
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private PriceExtraService priceExtraService;

    @Override
    public OrderBill payShopOrder(Integer id, String date) {
        PriceRange priceRange = getPriceRange(date);
        EntityWrapper<OrderItem> orderItemWrapper = new EntityWrapper<>();
        orderItemWrapper.eq("`" + OrderItem.ORDER + "`", id)
                .orderBy(OrderItem.LEVEL + "," + OrderItem.WEIGHT, true);
        List<OrderItem> orderItems = orderItemService.selectList(orderItemWrapper);
        EntityWrapper<cc.akkaha.shop.db.model.PriceExtra> priceExtraWrapper =
                new EntityWrapper<>();
        priceExtraWrapper.eq(cc.akkaha.shop.db.model.PriceExtra.DAY, date);
        cc.akkaha.shop.db.model.PriceExtra priceExtra =
                priceExtraService.selectOne(priceExtraWrapper);
        return OrderBill.parse(date, orderItems, priceRange, priceExtra);
    }

    /**
     * 如果是七层, 价格除以6再乘以7
     */
    private PriceRange getPriceRange(String date) {
        PriceRange priceRange = new PriceRange();
        EntityWrapper<Price> priceWrapper = new EntityWrapper<>();
        priceWrapper.eq(Price.DAY, date);
        priceService.selectList(priceWrapper).forEach(price -> {
            if (6 == price.getLevel()) {
                priceRange.addSixLevel(price.getWeight(), price.getPrice());
            } else if (7 == price.getLevel()) {
                priceRange.addOriginSevenLevel(price.getWeight(), price.getPrice());
                priceRange.addSevenLevel(
                        price.getWeight(),
                        price.getPrice().divide(six, 5, BigDecimal.ROUND_HALF_UP).multiply(seven)
                );
            }
        });
        return priceRange;
    }
}
