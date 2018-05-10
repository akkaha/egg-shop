package cc.akkaha.shop.service;

import cc.akkaha.shop.model.OrderBill;

public interface BillService {

    OrderBill payUserOrder(Integer id, String date);
}
