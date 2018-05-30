package cc.akkaha.shop.controllers;

import cc.akkaha.shop.controllers.model.OrderBillInsert;
import cc.akkaha.shop.controllers.model.PriceExtraItem;
import cc.akkaha.shop.db.model.Price;
import cc.akkaha.shop.db.model.PriceExtra;
import cc.akkaha.shop.db.service.PriceExtraService;
import cc.akkaha.shop.db.service.PriceService;
import cc.akkaha.shop.model.ApiCode;
import cc.akkaha.shop.model.ApiRes;
import cc.akkaha.shop.model.PriceItem;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shop/order-bill")
public class OrderBillController {

    @Autowired
    private PriceService priceService;
    @Autowired
    private PriceExtraService priceExtraService;

    @GetMapping("/query/{day}")
    public Object query(@PathVariable("day") String day) {
        ApiRes res = new ApiRes();
        EntityWrapper<Price> priceWrapper = new EntityWrapper<>();
        priceWrapper.eq(Price.DAY, day).orderBy(Price.LEVEL + "," + Price.WEIGHT, true);
        List<Price> prices = priceService.selectList(priceWrapper);
        EntityWrapper<PriceExtra> priceExtraMapper = new EntityWrapper<>();
        priceExtraMapper.eq(PriceExtra.DAY, day);
        PriceExtra priceExtra = priceExtraService.selectOne(priceExtraMapper);
        HashMap<String, Object> data = new HashMap<>();
        data.put("prices", prices.stream().map(item -> new PriceItem(
                item.getWeight().stripTrailingZeros().toPlainString(),
                item.getPrice().stripTrailingZeros().toPlainString(),
                item.getLevel()
        )).collect(Collectors.toList()));
        if (null != priceExtra) {
            data.put("priceExtra", new PriceExtraItem(
                    priceExtra.getId(),
                    priceExtra.getWeightAdjust().stripTrailingZeros().toPlainString()
            ));
        }
        res.setData(data);
        return res;
    }

    @PostMapping("/insert/{day}")
    public Object insert(@PathVariable("day") String day, @RequestBody OrderBillInsert insert) {
        ApiRes res = new ApiRes();
        List<PriceItem> items = insert.getItems();
        boolean ret = true;
        if (null != items && !items.isEmpty()) {
            ArrayList<Price> prices = new ArrayList<>();
            items.forEach(item -> {
                if (StringUtils.isNoneEmpty(item.getPrice(), item.getWeight())) {
                    Price price = new Price();
                    price.setDay(day);
                    price.setPrice(new BigDecimal(item.getPrice()));
                    price.setWeight(new BigDecimal(item.getWeight()));
                    price.setLevel(item.getLevel());
                    prices.add(price);
                }
            });
            if (!prices.isEmpty()) {
                EntityWrapper<Price> delWrapper = new EntityWrapper<>();
                delWrapper.eq(Price.DAY, day);
                ret = priceService.delete(delWrapper);
                if (ret) {
                    ret = priceService.insertBatch(prices);
                } else {
                    res.setMsg("插入价格区间失败");
                }
                if (!ret) {
                    res.setCode(ApiCode.ERROR);
                    res.setMsg("操作失败");
                }
            }
        } else {
            EntityWrapper<Price> delWrapper = new EntityWrapper<>();
            delWrapper.eq(Price.DAY, day);
            ret = priceService.delete(delWrapper);
            if (!ret) {
                res.setCode(ApiCode.ERROR);
                res.setMsg("操作失败");
            }
        }
        PriceExtraItem priceExtra = insert.getPriceExtra();
        if (null != priceExtra
                && StringUtils.isNotEmpty(priceExtra.getWeightAdjust())) {
            PriceExtra priceExtraDb = new PriceExtra();
            priceExtraDb.setId(priceExtra.getId());
            priceExtraDb.setDay(day);
            priceExtraDb.setWeightAdjust(new BigDecimal(priceExtra.getWeightAdjust()));
            ret = priceExtraService.insertOrUpdate(priceExtraDb);
            if (!ret) {
                res.setCode(ApiCode.ERROR);
                res.setMsg("保存价格额外信息失败");
            }
        }
        return res;
    }
}