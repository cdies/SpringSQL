package ru.sql.proj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sql.proj.model.Order;
import ru.sql.proj.service.OrderService;
import java.util.Collection;


@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    Collection<Order> get() throws Exception {
        Collection<Order> customers = orderService.exportChanges();
        LOG.info("Testing logstash!!!!" + mapper.writeValueAsString(customers));
        return customers;
    }
}
