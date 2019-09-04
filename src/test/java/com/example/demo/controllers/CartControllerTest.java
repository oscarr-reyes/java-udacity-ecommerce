package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
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

import java.math.BigDecimal;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {
	private CartController cartController;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private ItemRepository itemRepository;

	@Before
	public void setup() throws NoSuchFieldException {
		this.cartController = new CartController();

		TestUtils.mockField(this.cartController, "userRepository", this.userRepository);
		TestUtils.mockField(this.cartController, "cartRepository", this.cartRepository);
		TestUtils.mockField(this.cartController, "itemRepository", this.itemRepository);
	}

	@Test
	public void should_add_to_cart() {
		User userMock = new User();
		Item itemMock = new Item();
		Cart cartMock = Mockito.mock(Cart.class);
		ModifyCartRequest cartRequest = new ModifyCartRequest();

		cartRequest.setQuantity(1);
		userMock.setCart(cartMock);
		itemMock.setPrice(new BigDecimal(123));

		Mockito.when(this.userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(userMock);
		Mockito.when(this.itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(itemMock));

		ResponseEntity<Cart> responseEntity = this.cartController.addTocart(cartRequest);

		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Mockito.verify(cartMock, Mockito.times(1)).addItem(ArgumentMatchers.any());
	}

	@Test
	public void should_remove_from_cart() {
		User userMock = new User();
		Item itemMock = new Item();
		Cart cartMock = Mockito.mock(Cart.class);
		ModifyCartRequest cartRequest = new ModifyCartRequest();

		cartRequest.setQuantity(1);
		userMock.setCart(cartMock);
		itemMock.setPrice(new BigDecimal(123));

		Mockito.when(this.userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(userMock);
		Mockito.when(this.itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(itemMock));

		ResponseEntity<Cart> responseEntity = this.cartController.removeFromcart(cartRequest);

		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Mockito.verify(cartMock, Mockito.times(1)).removeItem(ArgumentMatchers.any());
	}

	@Test
	public void should_return_NOTFOUND_on_adding_cart_when_no_user_found() {
		ModifyCartRequest cartRequest = new ModifyCartRequest();

		Mockito.when(this.userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(null);

		ResponseEntity<Cart> responseEntity = this.cartController.addTocart(cartRequest);

		Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	public void should_return_NOTFOUND_on_adding_cart_when_no_item_found() {
		ModifyCartRequest cartRequest = new ModifyCartRequest();

		Mockito.when(this.userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(new User());
		Mockito.when(this.itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

		ResponseEntity<Cart> responseEntity = this.cartController.addTocart(cartRequest);

		Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	public void should_return_NOTFOUND_on_removing_cart_when_no_user_found() {
		ModifyCartRequest cartRequest = new ModifyCartRequest();

		Mockito.when(this.userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(null);

		ResponseEntity<Cart> responseEntity = this.cartController.removeFromcart(cartRequest);

		Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	@Test
	public void should_return_NOTFOUND_on_removing_cart_when_no_item_found() {
		ModifyCartRequest cartRequest = new ModifyCartRequest();

		Mockito.when(this.userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(new User());
		Mockito.when(this.itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

		ResponseEntity<Cart> responseEntity = this.cartController.removeFromcart(cartRequest);

		Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
}
