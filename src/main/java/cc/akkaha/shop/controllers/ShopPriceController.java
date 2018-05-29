package cc.akkaha.shop.controllers;

import cc.akkaha.shop.controllers.model.QueryShopPrice;
import cc.akkaha.shop.db.model.Price;
import cc.akkaha.shop.db.model.PriceExtra;
import cc.akkaha.shop.db.service.PriceExtraService;
import cc.akkaha.shop.db.service.PriceService;
import cc.akkaha.shop.model.ApiRes;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;

@RestController
@RequestMapping("/shop/price")
public class ShopPriceController {

    @Autowired
    private PriceService priceService;
    @Autowired
    private PriceExtraService extraService;

    @PostMapping("/query")
    public Object query(@RequestBody QueryShopPrice query) {
        ApiRes res = new ApiRes();
        HashMap<String, Object> data = new HashMap<>();
        res.setData(data);
        Wrapper wrapper = new EntityWrapper<Price>();
        if (null != query.getDate()) {
            wrapper.like(Price.DAY, query.getDate());
        }
        Page<Price> page = priceService.selectPage(
                new Page<Price>(query.getCurrent(), query.getSize(), Price.CREATED_AT, false),
                wrapper);
        data.put("list", page.getRecords());
        if (!page.getRecords().isEmpty()) {
            HashSet<String> days = new HashSet<>();
            page.getRecords().forEach(p -> days.add(p.getDay()));
            EntityWrapper<PriceExtra> priceExtraWrapper = new EntityWrapper<>();
            priceExtraWrapper.in(PriceExtra.DAY, days);
            HashMap<String, String> extraMap = new HashMap<>();
            extraService.selectList(priceExtraWrapper).forEach(
                    extra -> extraMap.put(extra.getDay(), extra.getWeightAdjust().toPlainString()));
            data.put("extra", extraMap);
        }
        data.put("total", page.getTotal());
        return res;
    }

    @PostMapping("/save")
    public Object update(@RequestBody Price price) {
        ApiRes res = new ApiRes();
        boolean ret = price.insertOrUpdate();
        if (ret) {
            res.setData(price);
        } else {
            res.setMsg("更新失败!");
        }
        return res;
    }

    @PostMapping("/delete")
    public Object delete(@RequestBody Price price) {
        ApiRes res = new ApiRes();
        boolean ret = price.deleteById();
        if (ret) {
            res.setData(price);
        } else {
            res.setMsg("操作失败!");
        }
        return res;
    }

    @GetMapping("/detail/{id}")
    public Object detail(@PathVariable("id") String id) {
        ApiRes res = new ApiRes();
        Price user = priceService.selectById(id);
        res.setData(user);
        return res;
    }
}
