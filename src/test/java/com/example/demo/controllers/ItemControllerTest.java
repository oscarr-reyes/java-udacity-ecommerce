package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
	private ItemController itemController;

	@Mock
	private ItemRepository itemRepository;

	@Before
	public void setup() throws NoSuchFieldException {
		this.itemController = new ItemController();

		TestUtils.mockField(this.itemController, "itemRepository", this.itemRepository);
	}

	@Test
	public void should_get_list_of_items() {
		List<Item> itemsMock  = Arrays.asList(
			new Item(),
			new Item(),
			new Item()
		);

		Mockito.when(this.itemRepository.findAll()).thenReturn(itemsMock);

		ResponseEntity<List<Item>> responseEntity = this.itemController.getItems();

		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Assert.assertEquals(itemsMock, responseEntity.getBody());
	}

	@Test
	public void should_find_item_by_id() {
		Item itemMock = new Item();

		itemMock.setName("foo");

		Mockito.when(this.itemRepository.findById(1L)).thenReturn(Optional.of(itemMock));

		ResponseEntity<Item> responseEntity = this.itemController.getItemById(1L);

		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Assert.assertEquals(itemMock, responseEntity.getBody());
	}

	@Test
	public void should_get_list_of_items_by_name() {
		String itemName = "foo";
		List<Item> itemsMock  = Arrays.asList(
			new Item(),
			new Item(),
			new Item()
		);

		Mockito.when(this.itemRepository.findByName(ArgumentMatchers.any())).thenReturn(itemsMock);

		ResponseEntity<List<Item>> responseEntity = this.itemController.getItemsByName(itemName);

		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Assert.assertEquals(itemsMock, responseEntity.getBody());
		Mockito.verify(this.itemRepository, Mockito.times(1)).findByName(itemName);
	}
}
