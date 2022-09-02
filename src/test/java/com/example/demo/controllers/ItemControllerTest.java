package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
//        when(cartRepository.save(any())).thenReturn(createCart());
    }

    @Test
    public void get_items() {
        when(itemRepository.findAll()).thenReturn(createItems());

        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(true);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(5, response.getBody().size());
    }

    @Test
    public void get_item_by_id() {
        BigDecimal itemPrice = BigDecimal.valueOf(191.131);
        when(itemRepository.findById(eq(3L))).thenReturn(Optional.of(createOneItem(itemPrice)));

        final ResponseEntity<Item> response = itemController.getItemById(3L);

        assertNotNull(true);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(itemPrice, response.getBody().getPrice());
    }

    @Test
    public void get_items_by_name() {
        List<Item> itemsToReturn = createItems();
        when(itemRepository.findByName("sample item name 1")).thenReturn(itemsToReturn);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("sample item name 1");

        assertNotNull(true);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(5, response.getBody().size());
        assertEquals(itemsToReturn.get(4), response.getBody().get(4));
    }

    public static List<Item> createItems() {
        List<Item> items = new ArrayList<>();

        for (Long i = 1L; i <= 5; i++) {
            Item item = new Item();
            item.setId(i);
            item.setDescription("Test item " + i);
            item.setPrice(BigDecimal.valueOf(131.113 + (i * 3.5)));
            items.add(item);
        }
        return items;
    }

    public static Item createOneItem(BigDecimal price) {
        Item item = new Item();
        item.setId(1L);
        item.setDescription("Test item 1");
        item.setPrice(price);
        return item;
    }

}
