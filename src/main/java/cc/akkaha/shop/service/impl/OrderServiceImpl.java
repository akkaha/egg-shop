package cc.akkaha.shop.service.impl;

import cc.akkaha.common.exceptions.DatabaseOperationException;
import cc.akkaha.shop.constants.OrderStatus;
import cc.akkaha.shop.controllers.model.OrderDetail;
import cc.akkaha.shop.db.model.OrderItem;
import cc.akkaha.shop.db.model.ShopOrder;
import cc.akkaha.shop.db.service.OrderItemService;
import cc.akkaha.shop.service.OrderService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderItemService orderItemService;

    @Override
    public OrderDetail newOrder(
            Integer userId,
            List<String> sixWeights,
            List<String> sevenWeights
    ) throws DatabaseOperationException {
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setUser(userId);
        shopOrder.setStatus(OrderStatus.STATUS_NEW);
        boolean bInsert = shopOrder.insert();
        if (bInsert) {
            ArrayList<OrderItem> orderItems = new ArrayList<>();
            if (null != sixWeights && !sixWeights.isEmpty()) {
                sixWeights.forEach(weight -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setUser(userId);
                    orderItem.setOrder(shopOrder.getId());
                    orderItem.setCount(0);
                    orderItem.setWeight(new BigDecimal(weight));
                    orderItem.setLevel(6);
                    orderItems.add(orderItem);
                });
            }
            if (null != sevenWeights && !sevenWeights.isEmpty()) {
                sevenWeights.forEach(weight -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setUser(userId);
                    orderItem.setOrder(shopOrder.getId());
                    orderItem.setCount(0);
                    orderItem.setWeight(new BigDecimal(weight));
                    orderItem.setLevel(7);
                    orderItems.add(orderItem);
                });
            }
            boolean b = orderItemService.insertBatch(orderItems);
            if (b) {
                return new OrderDetail(shopOrder, orderItems);
            } else {
                throw new DatabaseOperationException("创建订单记录项失败");
            }
        } else {
            throw new DatabaseOperationException("创建订单失败");
        }
    }

    @Override
    public BigDecimal countById(Integer id) {
        EntityWrapper<OrderItem> orderItemWrapper = new EntityWrapper<>();
        orderItemWrapper
                .setSqlSelect("sum(" + OrderItem.COUNT + ") count")
                .eq("`" + OrderItem.ORDER + "`", id);
        Map<String, Object> map = orderItemService.selectMap(orderItemWrapper);
        return (BigDecimal) map.getOrDefault("count", 0);
    }
}
