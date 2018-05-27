package cc.akkaha.shop.controllers;

import cc.akkaha.shop.controllers.model.NewOrderItem;
import cc.akkaha.shop.controllers.model.QueryOrderItem;
import cc.akkaha.shop.db.model.OrderItem;
import cc.akkaha.shop.db.service.OrderItemService;
import cc.akkaha.shop.model.ApiRes;
import cc.akkaha.shop.service.OrderService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
@RequestMapping("/shop/order-item")
public class OrderItemController {

    @Autowired
    private OrderItemService itemService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/query")
    public Object query(@RequestBody QueryOrderItem query) {
        ApiRes res = new ApiRes();
        Wrapper wrapper = new EntityWrapper<OrderItem>();
        if (null != query.getUser()) {
            wrapper.eq(OrderItem.USER, query.getUser());
        }
        if (null != query.getUser()) {
            wrapper.eq(OrderItem.USER, query.getUser());
        }
        Page page = itemService.selectPage(
                new Page<OrderItem>(
                        query.getCurrent(),
                        query.getSize(),
                        OrderItem.CREATED_AT,
                        false
                ),
                wrapper);
        res.setData(page);
        return res;
    }

    @PostMapping("/insert")
    public Object insert(@RequestBody NewOrderItem item) {
        ApiRes res = new ApiRes();
        if (null == item.getLevel()
                || item.getLevel() > 7
                || item.getLevel() < 6
                || null == item.getOrder()
                || null == item.getUser()
                || StringUtils.isEmpty(item.getWeight())
                ) {
            res.markInvalid("数据格式不对");
        } else {
            OrderItem orderItem = new OrderItem();
            orderItem.setWeight(new BigDecimal(item.getWeight()));
            orderItem.setLevel(item.getLevel());
            orderItem.setUser(item.getUser());
            orderItem.setOrder(item.getOrder());
            boolean ret = orderItem.insert();
            if (ret) {
                res.setData(orderItem);
            } else {
                res.setMsg("创建失败!");
            }
        }
        return res;
    }

    @PostMapping("/delete")
    public Object delete(@RequestBody OrderItem item) {
        ApiRes res = new ApiRes();
        boolean ret = item.deleteById();
        if (ret) {
            res.setData(item);
        } else {
            res.setMsg("删除失败!");
        }
        return res;
    }

    @PostMapping("/dec")
    public Object dec(@RequestBody OrderItem item) {
        ApiRes res = new ApiRes();
        if (null == item.getCount() || item.getCount() - 1 < 0) {
            res.markInvalid("不能少于0");
        } else {
            OrderItem toUpdate = new OrderItem();
            toUpdate.setId(item.getId());
            toUpdate.setCount(item.getCount() - 1);
            EntityWrapper wrapper = new EntityWrapper<OrderItem>();
            wrapper.eq(OrderItem.ID, item.getId()).eq(OrderItem.COUNT, item.getCount());
            boolean ret = toUpdate.update(wrapper);
            if (ret) {
                HashMap<String, Object> data = new HashMap<>();
                res.setData(data);
                data.put("item", toUpdate.getCount());
                data.put("total", orderService.countById(item.getOrder()));
            } else {
                res.markInvalid("更新失败!");
            }
        }
        return res;
    }

    @PostMapping("/inc")
    public Object inc(@RequestBody OrderItem item) {
        ApiRes res = new ApiRes();
        if (null == item.getCount()) {
            res.markInvalid("格式错误");
        } else {
            OrderItem toUpdate = new OrderItem();
            toUpdate.setId(item.getId());
            toUpdate.setCount(item.getCount() + 1);
            EntityWrapper wrapper = new EntityWrapper<OrderItem>();
            wrapper.eq(OrderItem.ID, item.getId()).eq(OrderItem.COUNT, item.getCount());
            boolean ret = toUpdate.update(wrapper);
            if (ret) {
                HashMap<String, Object> data = new HashMap<>();
                res.setData(data);
                data.put("item", toUpdate.getCount());
                data.put("total", orderService.countById(item.getOrder()));
            } else {
                res.markInvalid("更新失败!");
            }
        }
        return res;
    }
}
