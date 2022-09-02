package com.example.demo.controllers;

import java.util.List;

import com.example.demo.exceptions.ItemNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {
	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		itemRepository.findById(id).orElseThrow(() -> {
			log.info("metricClass=getItemById metric=Failure inputDataKey=itemId inputDataValue={} errorMessage=notExists", id);
			return new ItemNotFoundException("Item not found with ID: " + id);
		});

		log.info("metricClass=getItemById metric=Success inputDataKey=itemId inputDataValue={}", id);
		return ResponseEntity.of(itemRepository.findById(id));
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		if ( items == null || items.isEmpty() ) {
			log.info("metricClass=GetItemsByName metric=Failure inputDataKey=itemName inputDataValue={} errorMessage=notExists", name);
			return ResponseEntity.notFound().build();
		} else {
			log.info("metricClass=GetItemsByName metric=Success inputDataKey=itemName inputDataValue={}", name);
			return ResponseEntity.ok(items);
		}
	}
	
}
