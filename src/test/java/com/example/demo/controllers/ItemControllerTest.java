package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        when(userRepository.findByUsername(eq("Ramin"))).thenReturn(createUser());
        when(itemRepository.findById(eq(1L))).thenReturn(Optional.of(createItem()));
        when(cartRepository.save(any())).thenReturn(createCart());
    }

    @Test
    public void add_to_cart_returns_ok_with_items_data() {
        ModifyCartRequest mcr = new ModifyCartRequest();
        mcr.setUsername("Ramin");
        mcr.setItemId(1L);
        mcr.setQuantity(1);
        final ResponseEntity<Cart> response = cartController.addTocart(mcr);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().getItems().size());
    }

    @Test
    public void add_to_cart_return_not_found_when_item_not_exists() {
        ModifyCartRequest mcr = new ModifyCartRequest();
        mcr.setUsername("Ramin");
        mcr.setItemId(2L);
        mcr.setQuantity(1);
        final ResponseEntity<Cart> response = cartController.addTocart(mcr);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_returns_ok_without_removed_items_data() {
        ModifyCartRequest mcr = new ModifyCartRequest();
        mcr.setUsername("Ramin");
        mcr.setItemId(1L);
        mcr.setQuantity(1);
        final ResponseEntity<Cart> response = cartController.removeFromcart(mcr);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().getItems().size());
    }

    @Test
    public void remove_from_cart_return_not_found_when_item_not_exists() {
        ModifyCartRequest mcr = new ModifyCartRequest();
        mcr.setUsername("Ramin");
        mcr.setItemId(2L);
        mcr.setQuantity(1);
        final ResponseEntity<Cart> response = cartController.removeFromcart(mcr);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_return_not_found_when_username_not_exists() {
        ModifyCartRequest mcr = new ModifyCartRequest();
        mcr.setUsername("Sara");
        mcr.setItemId(1L);
        mcr.setQuantity(1);
        final ResponseEntity<Cart> response = cartController.removeFromcart(mcr);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Ramin");
        user.setPassword("abcdefg");

        user.setCart(createCart());
        return user;
    }

    public static Cart createCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(createItem());
        return cart;
    }

    public static Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setDescription("Test item 1");
        item.setPrice(BigDecimal.valueOf(131.113));
        return item;
    }

    public static UserOrder createOrder() {
        return UserOrder.createFromCart(createCart());
    }

}
