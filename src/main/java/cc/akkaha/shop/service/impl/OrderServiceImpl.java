package cc.akkaha.shop.service.impl;

import cc.akkaha.common.exceptions.DatabaseOperationException;
import cc.akkaha.common.util.JsonUtils;
import cc.akkaha.shop.constants.OrderStatus;
import cc.akkaha.shop.controllers.model.OrderDetail;
import cc.akkaha.shop.controllers.model.UserExt;
import cc.akkaha.shop.db.model.OrderItem;
import cc.akkaha.shop.db.model.ShopOrder;
import cc.akkaha.shop.db.model.ShopUser;
import cc.akkaha.shop.db.service.OrderItemService;
import cc.akkaha.shop.db.service.ShopOrderService;
import cc.akkaha.shop.db.service.ShopUserService;
import cc.akkaha.shop.service.OrderService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private ShopOrderService shopOrderService;
    @Autowired
    private OrderItemService orderItemService;

    @Override
    public OrderDetail newOrder(
            Integer userId,
            List<String> sixWeights,
            List<String> sevenWeights
    ) throws DatabaseOperationException {
        UserExt userExt = new UserExt();
        userExt.setSixWeights(sixWeights);
        userExt.setSevenWeights(sevenWeights);
        ShopUser shopUser = new ShopUser();
        shopUser.setId(userId);
        shopUser.setExt(JsonUtils.stringfy(userExt));
        shopUserService.updateById(shopUser);
        Date now = new Date();
        Date end = DateUtils.addDays(DateUtils.truncate(now, Calendar.DAY_OF_MONTH), 1);
        Date start = DateUtils.addDays(end, -1);
        EntityWrapper<ShopOrder> shopOrderEntityWrapper = new EntityWrapper<>();
        shopOrderEntityWrapper.ge(ShopOrder.CREATED_AT, start);
        shopOrderEntityWrapper.lt(ShopOrder.CREATED_AT, end);
        shopOrderEntityWrapper.orderBy(ShopOrder.DAY_ORDER, false);
        Page<ShopOrder> pages = shopOrderService.selectPage(new Page<>(0, 1), shopOrderEntityWrapper);
        ShopOrder shopOrder = new ShopOrder();
        if (null != pages && !pages.getRecords().isEmpty()) {
            ShopOrder countOrder = pages.getRecords().get(0);
            shopOrder.setDayOrder(countOrder.getDayOrder() + 1);
        } else {
            shopOrder.setDayOrder(1);
        }
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
        if (null != map) {
            return (BigDecimal) map.getOrDefault("count", 0);
        } else {
            return BigDecimal.ZERO;
        }
    }
}
