package com.bigtree.chef.orders


import com.bigtree.chef.orders.repository.OrderRepository
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class OrderControllerComponentTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @SpringBean
    com.bigtree.chef.orders.service.OrderService orderService = Mock()

    @SpringBean
    OrderRepository orderRepository = Mock()

    def 'GET /orders without query params'() {
        given: ''
        HttpHeaders headers = new HttpHeaders();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/orders").headers(headers)

        when: 'GET call to orders endpoint'
        List<com.bigtree.chef.orders.entity.Order> orders = Lists.newArrayList()
        orders.add(getDummyOrder())
        orderRepository.findAll() >> orders
        ResultActions resultActions = mockMvc.perform(request)

        then: 'Expect orders response 200 OK'
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
//                .andExpect(MockMvcResultMatchers.jsonPath('$.orders').isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath('$.orders[0].items').isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath('$.orders[0].reference').isNotEmpty())
    }

    def 'GET /orders with query params'() {
        given: ''
        HttpHeaders headers = new HttpHeaders();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/orders")
                .headers(headers)
                .param("email", "customer@email.com")

        when: 'GET call to orders endpoint'
        orderService.findOrdersWithQuery(_) >> Arrays.asList(getDummyOrder())
        ResultActions resultActions = mockMvc.perform(request)

        then: 'Expect orders response 200 OK'
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
//                .andExpect(MockMvcResultMatchers.jsonPath('$.orders').isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath('$.orders[0].items').isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath('$.orders[0].reference').isNotEmpty())
    }

    private com.bigtree.chef.orders.entity.Order getDummyOrder() {
        com.bigtree.chef.orders.entity.Order order = com.bigtree.chef.orders.entity.Order.builder()
                .address(Address.builder().lineNumber1("lineNumber1").lineNumber2("lineNumber2").city("city")
                        .country("GB").postCode("ABC1234").build())
                .date(LocalDate.now())
                .email("customer@email.com")
                .reference("reference")
                .status(OrderStatus.CREATED)
                .subTotal(BigDecimal.valueOf(20))
                .saleTax(BigDecimal.valueOf(2))
                .shippingCost(BigDecimal.valueOf(3))
                .totalCost(BigDecimal.valueOf(25))
                .build();
        order.setId(1);
        com.bigtree.chef.orders.entity.OrderItem item = new com.bigtree.chef.orders.entity.OrderItem();
        item.setOrder(order);
        item.setPrice(BigDecimal.TEN);
        item.setProductId("123456");
        item.setProductName("Apple");
        item.setQuantity(2);
        item.setTotal(item.getPrice() * BigDecimal.valueOf(item.getQuantity()));
        order.setItems(Sets.newHashSet(item));
        return order
    }
}
