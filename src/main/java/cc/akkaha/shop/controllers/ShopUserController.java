package cc.akkaha.shop.controllers;

import cc.akkaha.shop.db.model.ShopUser;
import cc.akkaha.shop.db.service.ShopUserService;
import cc.akkaha.shop.model.ApiRes;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/shop/user")
public class ShopUserController {

    @Autowired
    private ShopUserService shopUserService;

    @PostMapping("/query")
    public Object query(@RequestBody ShopUser query) {
        ApiRes res = new ApiRes();
        HashMap<String, Object> data = new HashMap<>();
        res.setData(data);
        Wrapper userWrapper = new EntityWrapper<ShopUser>();
        if (null != query.getName()) {
            userWrapper.like(ShopUser.NAME, query.getName());
        }
        res.setData(query.selectList(userWrapper));
        return res;
    }

    @PostMapping("/update")
    public Object update(@RequestBody ShopUser user) {
        ApiRes res = new ApiRes();
        boolean ret = user.updateById();
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
