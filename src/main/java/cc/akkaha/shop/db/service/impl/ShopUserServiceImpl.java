package cc.akkaha.shop.db.service.impl;

import cc.akkaha.shop.db.model.ShopUser;
import cc.akkaha.shop.db.client.ShopUserMapper;
import cc.akkaha.shop.db.service.ShopUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShopUserServiceImpl extends ServiceImpl<ShopUserMapper, ShopUser> implements ShopUserService {
    
}
