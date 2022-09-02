package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit_order() {
        String username = "Ramin";
        when(userRepository.findByUsername(eq(username))).thenReturn(createUser());

        final ResponseEntity<UserOrder> response = orderController.submit(username);

        assertNotNull(true);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(405.339), response.getBody().getTotal());
        assertEquals(3, response.getBody().getItems().size());
    }

    @Test
    public void get_orders_of_user_by_username() {
        String username = "Ramin";
        User user = createUser();
        when(userRepository.findByUsername(eq(username))).thenReturn(user);
        when(orderRepository.findByUser(eq(user))).thenReturn(createOrders());

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);

        assertNotNull(true);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(7, response.getBody().size());
    }

    public static List<Item> createItems() {
        List<Item> items = new ArrayList<>();

        for (Long i = 1L; i <= 5; i++) {
            items.add(createItem(i));
        }
        return items;
    }

    public static Item createItem(Long itemId) {
        Item item = new Item();
        item.setId(itemId);
        item.setDescription("Test item " + itemId);
        item.setPrice(BigDecimal.valueOf(131.113 + (itemId * 2)));
        return item;
    }

    public static Cart createCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(createItem(1L));
        cart.addItem(createItem(2L));
        cart.addItem(createItem(3L));
        return cart;
    }

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Ramin");
        user.setPassword("abcdefg");

        user.setCart(createCart());
        return user;
    }

    public static UserOrder createOrder() {
        return UserOrder.createFromCart(createCart());
    }

    public static List<UserOrder> createOrders() {
        List<UserOrder> orderList = new ArrayList<>();

        for (Long i = 1L; i <= 7; i++) {
            orderList.add(createOrder());
        }

        return orderList;
    }
}
