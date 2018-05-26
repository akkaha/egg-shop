package cc.akkaha.shop.service;

import cc.akkaha.shop.model.OrderBill;

public interface BillService {

    OrderBill payShopOrder(Integer id, String date);
}
