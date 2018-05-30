package cc.akkaha.shop.controllers;

import cc.akkaha.shop.db.model.OrderItem;
import cc.akkaha.shop.db.model.Price;
import cc.akkaha.shop.db.service.OrderItemService;
import cc.akkaha.shop.model.ApiRes;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shop/statistics")
public class StatisticsController {

    @Autowired
    private OrderItemService itemService;

    @PostMapping("/count")
    public Object query() {
        ApiRes res = new ApiRes();
        Wrapper wrapper = new EntityWrapper<Price>();
        wrapper.setSqlSelect("sum(" + OrderItem.COUNT + ") count, `level`")
                .groupBy("`" + OrderItem.LEVEL + "`");
        List list = itemService.selectMaps(wrapper);
        res.setData(list);
        return res;
    }
}
