package ru.sql.proj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sql.proj.model.Order;
import ru.sql.proj.procedure.CustomerInsertProcedure;
import ru.sql.proj.procedure.OrderHeaderExportProcedure;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderHeaderExportProcedure orderHeaderExportProcedure;

    @Autowired
    CustomerInsertProcedure customerInsertProcedure;

    public Collection<Order> exportChanges() {
        Collection<Order.Row> orders = orderHeaderExportProcedure.execute("t");

        Collection<Order> returnOrders = new ArrayList<>();

        if (!orders.isEmpty()) {
            for (Order.Row order : orders) {
                returnOrders.add(order);
            }
        }
        return returnOrders;
    }

    public Collection<Order> setChanges(String customerName){
        return customerInsertProcedure.execute(customerName, new Date());
    }
}
