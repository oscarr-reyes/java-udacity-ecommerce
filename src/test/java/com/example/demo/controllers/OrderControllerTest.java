package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {
	private OrderController orderController;

	@Mock
	private UserRepository userRepository;

	@Mock
	private OrderRepository orderRepository;

	@Before
	public void setup() throws NoSuchFieldException {
		this.orderController = new OrderController();

		TestUtils.mockField(this.orderController, "userRepository", this.userRepository);
		TestUtils.mockField(this.orderController, "orderRepository", this.orderRepository);
	}

	@Test
	public void should_submit_order() {
		User userMock = new User();
		Cart cartMock = new Cart();

		cartMock.setItems(new ArrayList<>());
		userMock.setCart(cartMock);

		Mockito.when(this.userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(userMock);

		ResponseEntity<UserOrder> responseEntity = this.orderController.submit("foo");

		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void should_get_list_of_orders_for_user() {
		UserOrder orderMock = new UserOrder();

		Mockito.when(this.userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(new User());
		Mockito.when(this.orderRepository.findByUser(ArgumentMatchers.any())).thenReturn(Collections.singletonList(orderMock));

		ResponseEntity<List<UserOrder>> responseEntity = this.orderController.getOrdersForUser("foo");

		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Assert.assertEquals(1, responseEntity.getBody().size());
	}

	@Test
	public void should_return_NOTFOUND_when_user_not_found_on_submit() {
		Mockito.when(this.userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(null);

		ResponseEntity<UserOrder> responseEntity = this.orderController.submit("foo");

		Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	public void should_return_NOTFOUND_when_user_not_found_on_orders() {
		Mockito.when(this.userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(null);

		ResponseEntity<List<UserOrder>> responseEntity = this.orderController.getOrdersForUser("foo");

		Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
}
