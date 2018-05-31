package cc.akkaha.shop.controllers;

import cc.akkaha.shop.controllers.model.StatisticsQuery;
import cc.akkaha.shop.controllers.model.StatisticsResponse;
import cc.akkaha.shop.db.model.OrderItem;
import cc.akkaha.shop.db.model.Price;
import cc.akkaha.shop.db.service.OrderItemService;
import cc.akkaha.shop.model.ApiRes;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/shop/statistics")
public class StatisticsController {

    private Logger logger = LoggerFactory.getLogger(StatisticsController.class);
    @Autowired
    private OrderItemService itemService;

    @PostMapping("/count")
    public Object query(@RequestBody StatisticsQuery query) {
        ApiRes res = new ApiRes();
        StatisticsResponse response = new StatisticsResponse();
        res.setData(response);
        Date start = null;
        Date end = null;
        try {
            if (null != query.getStart() && null != query.getEnd()) {
                start = DateUtils.parseDate(query.getStart(), "yyyy-MM-dd");
                Date tmp = DateUtils.parseDate(query.getEnd(), "yyyy-MM-dd");
                if (null != tmp) {
                    end = DateUtils.addDays(tmp, 1);
                }
            } else {
                Date now = new Date();
                end = DateUtils.addDays(DateUtils.truncate(now, Calendar.DAY_OF_MONTH), 1);
                if (query.getIsHome()) {
                    start = DateUtils.addDays(end, -15);
                } else {
                    start = DateUtils.addDays(end, -1);
                }
            }
        } catch (Throwable t) {
            res.markError("日期格式错误");
            logger.warn(ExceptionUtils.getStackTrace(t));
            return res;
        }
        Integer user = query.getUser();
        if (query.getIsHome()) {
            Wrapper dateWrapper = new EntityWrapper<Price>();
            dateWrapper.setSqlSelect("sum(" + OrderItem.COUNT + ") count, DATE_FORMAT(" + OrderItem.CREATED_AT + ",'%Y%m%d') day")
                    .groupBy("day");
            dateWrapper.ge(OrderItem.CREATED_AT, start);
            dateWrapper.lt(OrderItem.CREATED_AT, end);
            response.setByDate(itemService.selectMaps(dateWrapper));
        } else {
            Wrapper levelWrapper = new EntityWrapper<Price>();
            levelWrapper.setSqlSelect("sum(" + OrderItem.COUNT + ") count, `level`")
                    .groupBy("`" + OrderItem.LEVEL + "`");
            levelWrapper.ge(OrderItem.CREATED_AT, start);
            levelWrapper.lt(OrderItem.CREATED_AT, end);
            Wrapper weightWrapper = new EntityWrapper<Price>();
            weightWrapper.setSqlSelect("sum(" + OrderItem.COUNT + ") count, `weight`")
                    .groupBy("`" + OrderItem.WEIGHT + "`");
            weightWrapper.ge(OrderItem.CREATED_AT, start);
            weightWrapper.lt(OrderItem.CREATED_AT, end);
            if (null != user) {
                levelWrapper.eq(OrderItem.USER, user);
                weightWrapper.eq(OrderItem.USER, user);
            }
            response.setByLevel(itemService.selectMaps(levelWrapper));
            response.setByWeight(itemService.selectMaps(weightWrapper));
        }
        return res;
    }
}
