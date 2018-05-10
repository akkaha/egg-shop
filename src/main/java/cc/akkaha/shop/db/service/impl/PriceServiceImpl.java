package cc.akkaha.shop.db.service.impl;

import cc.akkaha.shop.db.model.Price;
import cc.akkaha.shop.db.client.PriceMapper;
import cc.akkaha.shop.db.service.PriceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl extends ServiceImpl<PriceMapper, Price> implements PriceService {
    
}
