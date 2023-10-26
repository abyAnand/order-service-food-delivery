package com.delivery.orderservice.service;

import com.delivery.orderservice.dto.OrderDTO;
import com.delivery.orderservice.dto.OrderDTOFromFE;
import com.delivery.orderservice.dto.UserDTO;
import com.delivery.orderservice.entity.Order;
import com.delivery.orderservice.mapper.OrderMapper;
import com.delivery.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    SequenceGenerator sequenceGenerator;


    @Autowired
    RestTemplate restTemplate;


    public OrderDTO saveOrder(OrderDTOFromFE orderDTOFromFE) {
        Integer newOrderId = sequenceGenerator.generateNextOrderId();
        UserDTO userDTO = fetchUserDetailsFromUserId(orderDTOFromFE.getUserId());
        Order orderToBeSaved = new Order(newOrderId, orderDTOFromFE.getFoodItemsList(), orderDTOFromFE.getRestaurant(), userDTO);
        orderRepository.save(orderToBeSaved);
        return OrderMapper.INSTANCE.mapOrderToOrderDTO(orderToBeSaved);
    }

    private UserDTO fetchUserDetailsFromUserId(Integer userId) {
        return restTemplate.getForObject("http://USER-SERVICE/user/fetchUserById/"+userId, UserDTO.class);
    }
}
