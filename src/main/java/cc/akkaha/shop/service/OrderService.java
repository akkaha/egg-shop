package cc.akkaha.shop.service;

import cc.akkaha.common.exceptions.DatabaseOperationException;
import cc.akkaha.shop.controllers.model.OrderDetail;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    OrderDetail newOrder(Integer userId, List<String> sixWeights, List<String> sevenWeights) throws DatabaseOperationException;

    BigDecimal countById(Integer id);
}
