package cc.akkaha.shop.db.service.impl;

import cc.akkaha.shop.db.model.OrderItem;
import cc.akkaha.shop.db.client.OrderItemMapper;
import cc.akkaha.shop.db.service.OrderItemService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {
    
}
