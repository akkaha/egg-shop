package cc.akkaha.shop.db.service.impl;

import cc.akkaha.shop.db.model.ShopOrder;
import cc.akkaha.shop.db.client.ShopOrderMapper;
import cc.akkaha.shop.db.service.ShopOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShopOrderServiceImpl extends ServiceImpl<ShopOrderMapper, ShopOrder> implements ShopOrderService {
    
}
