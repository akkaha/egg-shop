package cc.akkaha.shop.controllers;

import cc.akkaha.common.util.DateUtils;
import cc.akkaha.common.util.JsonUtils;
import cc.akkaha.shop.constants.OrderStatus;
import cc.akkaha.shop.controllers.model.NewOrder;
import cc.akkaha.shop.controllers.model.OrderDetail;
import cc.akkaha.shop.controllers.model.QueryShopOrder;
import cc.akkaha.shop.controllers.model.ShopOrderItem;
import cc.akkaha.shop.db.model.OrderItem;
import cc.akkaha.shop.db.model.ShopOrder;
import cc.akkaha.shop.db.model.ShopUser;
import cc.akkaha.shop.db.service.OrderItemService;
import cc.akkaha.shop.db.service.ShopOrderService;
import cc.akkaha.shop.db.service.ShopUserService;
import cc.akkaha.shop.model.ApiRes;
import cc.akkaha.shop.model.OrderBill;
import cc.akkaha.shop.service.BillService;
import cc.akkaha.shop.service.OrderService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shop/order")
public class ShopOrderController {

    private static Logger logger = LoggerFactory.getLogger(ShopOrderController.class);
    @Autowired
    private ShopOrderService shopOrderService;
    @Autowired
    private BillService billService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShopUserService shopUserService;

    @PostMapping("/query")
    public Object query(@RequestBody QueryShopOrder query) {
        ApiRes res = new ApiRes();
        HashMap<String, Object> data = new HashMap<>();
        res.setData(data);
        Wrapper userOrderWrapper = new EntityWrapper<ShopOrder>();
        if (null != query.getUser()) {
            userOrderWrapper.eq(ShopOrder.USER, query.getUser());
        }
        if (null != query.getStatus()) {
            userOrderWrapper.eq(ShopOrder.STATUS, query.getStatus());
        }
        Page page = shopOrderService.selectPage(new Page<ShopOrder>(query.getCurrent(),
                        query.getSize(),
                        ShopOrder.CREATED_AT, false),
                userOrderWrapper);
        ArrayList<ShopOrderItem> shopOrderItems = new ArrayList<>();
        data.put("list", shopOrderItems);
        data.put("total", page.getTotal());
        List<ShopOrder> records = page.getRecords();
        if (null != records && !records.isEmpty()) {
            HashMap<Integer, List<ShopOrderItem>> userItemMap = new HashMap<>();
            HashMap<Integer, List<ShopOrderItem>> orderItemMap = new HashMap<>();
            ArrayList<Integer> orderIds = new ArrayList<>();
            List<Integer> userIds = records.stream()
                    .peek(order -> {
                        ShopOrderItem shopOrderItem = new ShopOrderItem();
                        shopOrderItems.add(shopOrderItem);
                        shopOrderItem.setId(order.getId());
                        shopOrderItem.setStatus(order.getStatus());
                        shopOrderItem.setCreatedAt(order.getCreatedAt());
                        Integer userId = order.getUser();
                        List<ShopOrderItem> items = userItemMap.getOrDefault(userId, new ArrayList<>());
                        if (items.isEmpty()) {
                            items.add(shopOrderItem);
                            userItemMap.put(userId, items);
                        } else {
                            items.add(shopOrderItem);
                        }
                        Integer orderId = order.getId();
                        orderIds.add(orderId);
                        items = userItemMap.getOrDefault(orderId, new ArrayList<>());
                        if (items.isEmpty()) {
                            items.add(shopOrderItem);
                            orderItemMap.put(orderId, items);
                        } else {
                            items.add(shopOrderItem);
                        }
                    })
                    .map(ShopOrder::getUser)
                    .collect(Collectors.toList());
            EntityWrapper<ShopUser> wrapper = new EntityWrapper<>();
            wrapper.in(ShopUser.ID, userIds);
            shopUserService.selectList(wrapper).forEach(user -> {
                userItemMap.get(user.getId()).forEach(item -> item.setUser(user.getName()));
            });
            EntityWrapper<OrderItem> orderItemWrapper = new EntityWrapper<>();
            orderItemWrapper
                    .setSqlSelect("`" + OrderItem.ORDER + "`,sum(" + OrderItem.COUNT + ") count")
                    .in("`" + OrderItem.ORDER + "`", orderIds)
                    .groupBy("`" + OrderItem.ORDER + "`");
            orderItemService.selectMaps(orderItemWrapper).forEach(map -> {
                Integer orderId = (Integer) map.get(OrderItem.ORDER);
                BigDecimal count = (BigDecimal) map.get("count");
                orderItemMap.get(orderId).forEach(item -> item.setCount(count));
            });
        }
        res.setData(data);
        return res;
    }

    @PostMapping("/insert")
    public Object insert(@RequestBody NewOrder order) {
        ApiRes res = new ApiRes();
        ShopUser user = order.getUser();
        if (null != user && StringUtils.isNotEmpty(user.getName())) {
            try {
                if (null != user.getId() && user.getId() > 0) { // old user
                    OrderDetail detail = orderService.newOrder(user.getId(), order.getSixWeights(), order.getSevenWeights());
                    res.setData(detail);
                } else { // new user
                    boolean bInsert = user.insert();
                    if (bInsert) {
                        OrderDetail detail = orderService.newOrder(user.getId(), order.getSixWeights(), order.getSevenWeights());
                        res.setData(detail);
                    } else {
                        res.markError("新建用户失败");
                    }
                }
            } catch (Throwable t) {
                String stackTrace = ExceptionUtils.getStackTrace(t);
                logger.warn(stackTrace);
                res.markInvalid(stackTrace);
            }
        } else {
            res.markInvalid("用户不能为空");
        }
        return res;
    }

    @PostMapping("/update")
    public Object update(@RequestBody ShopOrder order) {
        ApiRes res = new ApiRes();
        boolean ret = order.updateById();
        if (ret) {
            res.setData(order);
        } else {
            res.setMsg("更新失败!");
        }
        return res;
    }

    @PostMapping("/delete")
    public Object delete(@RequestBody ShopOrder order) {
        ApiRes res = new ApiRes();
        boolean ret = order.deleteById();
        if (ret) {
            res.setData(order);
        } else {
            res.setMsg("操作失败!");
        }
        return res;
    }

    @GetMapping("/detail/{id}")
    public Object detail(@PathVariable("id") String id) {
        ApiRes res = new ApiRes();
        ShopOrder order = shopOrderService.selectById(id);
        HashMap<String, Object> data = new HashMap<>();
        data.put("order", order);
        OrderItem orderItem = new OrderItem();
        EntityWrapper<OrderItem> wrapper = new EntityWrapper<>();
        wrapper.eq(OrderItem.USER, id).orderBy(OrderItem.CREATED_AT, true);
        List items = orderItem.selectList(wrapper);
        data.put("items", items);
        res.setData(data);
        return res;
    }

    @GetMapping("/pay/{id}")
    public Object pay(@PathVariable("id") String id,
                      @RequestParam(value = "date", required = false) String date) {
        ApiRes res = new ApiRes();
        ShopOrder order = shopOrderService.selectById(id);
        HashMap<String, Object> data = new HashMap<>();
        data.put("order", order);
        if (OrderStatus.STATUS_FINISHED.equals(order.getStatus())) {
            data.put("bill", JsonUtils.parse(order.getBill(), OrderBill.class));
        } else {
            if (StringUtils.isEmpty(date)) {
                date = DateUtils.currentDate();
            }
            OrderBill orderBill = billService.payUserOrder(order.getId(), date);
            data.put("bill", orderBill);
            data.put("priceExtra", orderBill.getInner().priceExtra);
        }
        res.setData(data);
        return res;
    }
}
