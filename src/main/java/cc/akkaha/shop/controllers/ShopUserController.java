package cc.akkaha.shop.controllers;

import cc.akkaha.shop.controllers.model.QueryShopUser;
import cc.akkaha.shop.db.model.ShopUser;
import cc.akkaha.shop.db.service.ShopUserService;
import cc.akkaha.shop.model.ApiRes;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/shop/user")
public class ShopUserController {

    @Autowired
    private ShopUserService shopUserService;

    @PostMapping("/query")
    public Object query(@RequestBody QueryShopUser query) {
        ApiRes res = new ApiRes();
        HashMap<String, Object> data = new HashMap<>();
        res.setData(data);
        Wrapper userWrapper = new EntityWrapper<ShopUser>();
        if (null != query.getName()) {
            userWrapper.like(ShopUser.NAME, query.getName());
        }
        Page page = shopUserService.selectPage(new Page<ShopUser>(query.getCurrent(),
                        query.getSize(),
                        ShopUser.CREATED_AT, false),
                userWrapper);
        data.put("list", page.getRecords());
        data.put("total", page.getTotal());
        return res;
    }

    @PostMapping("/save")
    public Object update(@RequestBody ShopUser user) {
        ApiRes res = new ApiRes();
        boolean ret = user.insertOrUpdate();
        if (ret) {
            res.setData(user);
        } else {
            res.setMsg("更新失败!");
        }
        return res;
    }

    @PostMapping("/delete")
    public Object delete(@RequestBody ShopUser user) {
        ApiRes res = new ApiRes();
        boolean ret = user.deleteById();
        if (ret) {
            res.setData(user);
        } else {
            res.setMsg("操作失败!");
        }
        return res;
    }

    @GetMapping("/detail/{id}")
    public Object detail(@PathVariable("id") String id) {
        ApiRes res = new ApiRes();
        ShopUser user = shopUserService.selectById(id);
        res.setData(user);
        return res;
    }
}
