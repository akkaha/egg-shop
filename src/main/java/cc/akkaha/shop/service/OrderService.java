package cc.akkaha.shop.service;

import cc.akkaha.common.exceptions.DatabaseOperationException;
import cc.akkaha.shop.controllers.model.OrderDetail;
import cc.akkaha.shop.db.model.OrderItem;

import java.util.List;

public interface OrderService {

    public OrderDetail newOrder(Integer userId, List<String> sixWeights, List<String> sevenWeights) throws DatabaseOperationException;
}
