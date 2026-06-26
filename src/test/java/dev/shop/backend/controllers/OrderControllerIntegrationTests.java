package dev.shop.backend.controllers;

import dev.shop.backend.TestDataUtilities;
import dev.shop.backend.domain.dto.OrderDTO;
import dev.shop.backend.domain.entities.OrderEntity;
import dev.shop.backend.domain.entities.ProductEntity;
import dev.shop.backend.domain.entities.UserEntity;
import dev.shop.backend.service.OrderService;
import dev.shop.backend.service.ProductService;
import dev.shop.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class OrderControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public OrderControllerIntegrationTests(MockMvc mockMvc, OrderService orderService, UserService userService, ProductService productService){
        this.mockMvc = mockMvc;
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateOrderReturnsHttpStatusCreated() throws Exception{

        OrderEntity orderA = TestDataUtilities.createOrderEntityA(null, null);

        String orderJSON = objectMapper.writeValueAsString(orderA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateOrderReturnsCreatedOrder() throws Exception{

        OrderEntity order = TestDataUtilities.createOrderEntityA(null, null);

        String orderJSON = objectMapper.writeValueAsString(order);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity").value(order.getQuantity())
        );
    }

    @Test
    public void testThatListAllReturnsHttpStatusOK() throws Exception{

        OrderEntity order = TestDataUtilities.createOrderEntityA(null, null);
        OrderEntity savedOrder = orderService.save(order);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAllReturnListOfOrders() throws Exception{

        OrderEntity order = TestDataUtilities.createOrderEntityA(null, null);
        OrderEntity savedOrder = orderService.save(order);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].quantity").value(order.getQuantity())
        );
    }

    @Test
    public void testThatListOrdersByUserReturnsHttpStatusOK() throws Exception{
        UserEntity userA = TestDataUtilities.createTestUserEntityA();
        UserEntity savedUser = userService.save(userA);

        UserEntity user = TestDataUtilities.createTestUserEntityAForRequests(savedUser.getEmail());

        ProductEntity productEntity = TestDataUtilities.createProductEntityA(user);
        ProductEntity savedProduct = productService.save(productEntity);

        ProductEntity product = TestDataUtilities.createProductEntityAForRequests(user.getEmail(), savedProduct.getId());

        OrderEntity order = TestDataUtilities.createOrderEntityA(user, product);
        OrderEntity savedOrder = orderService.save(order);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders?email=" + user.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListOrdersByUserReturnsListOfOrders() throws Exception{
        UserEntity userA = TestDataUtilities.createTestUserEntityA();
        UserEntity savedUser = userService.save(userA);

        UserEntity user = TestDataUtilities.createTestUserEntityAForRequests(savedUser.getEmail());

        ProductEntity productEntity = TestDataUtilities.createProductEntityA(user);
        ProductEntity savedProduct = productService.save(productEntity);

        ProductEntity product = TestDataUtilities.createProductEntityAForRequests(user.getEmail(), savedProduct.getId());

        OrderEntity order = TestDataUtilities.createOrderEntityA(user, product);
        OrderEntity savedOrder = orderService.save(order);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders?email=" + user.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].quantity").value(savedOrder.getQuantity())
        );
    }

    @Test
    public void testThatListOrdersByProductReturnsHttpStatusOK() throws Exception{
        UserEntity userA = TestDataUtilities.createTestUserEntityA();
        UserEntity savedUser = userService.save(userA);

        UserEntity user = TestDataUtilities.createTestUserEntityAForRequests(savedUser.getEmail());


        ProductEntity productEntity = TestDataUtilities.createProductEntityA(user);
        ProductEntity savedProduct = productService.save(productEntity);

        ProductEntity product = TestDataUtilities.createProductEntityAForRequests(user.getEmail(), savedProduct.getId());

        OrderEntity order = TestDataUtilities.createOrderEntityA(user, product);
        OrderEntity savedOrder = orderService.save(order);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders?id=" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListOrdersOfProductReturnsListOfOrders() throws Exception{
        UserEntity userA = TestDataUtilities.createTestUserEntityA();
        UserEntity savedUser = userService.save(userA);

        UserEntity user = TestDataUtilities.createTestUserEntityAForRequests(savedUser.getEmail());


        ProductEntity productEntity = TestDataUtilities.createProductEntityA(user);
        ProductEntity savedProduct = productService.save(productEntity);

        ProductEntity product = TestDataUtilities.createProductEntityAForRequests(user.getEmail(), savedProduct.getId());

        productEntity.setId(savedProduct.getId());

        OrderEntity order = TestDataUtilities.createOrderEntityA(user, product);
        OrderEntity savedOrder = orderService.save(order);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders?product_id=" + productEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].quantity").value(savedOrder.getQuantity())
        );
    }

    @Test
    public void testThatGetOrderReturnsHttpStatusOKWhenOrderExists() throws Exception{

        OrderEntity order = TestDataUtilities.createOrderEntityA(null, null);
        OrderEntity savedOrders = orderService.save(order);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetOrderReturnsHttpStatusNotFoundWhenOrderDoesNotExist() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/2")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetOrderReturnsFoundOrder() throws Exception{

        OrderEntity order = TestDataUtilities.createOrderEntityA(null, null);
        OrderEntity savedOrder = orderService.save(order);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/" + order.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity").value(order.getQuantity())
        );
    }

    @Test
    public void testThatPartialUpdateOrderReturnsHttpStatusOKWhenOrderExists() throws Exception{

        OrderEntity order = TestDataUtilities.createOrderEntityA(null, null);
        OrderEntity savedOrder = orderService.save(order);

        OrderDTO orderDTO = TestDataUtilities.createOrderDTOA(null, null);
        orderDTO.setQuantity(123L);

        String orderJSON = objectMapper.writeValueAsString(orderDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/orders/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateOrderReturnsHttpStatusNotFoundWhenOrderDoesNotExist() throws Exception{

        OrderDTO orderDTO = TestDataUtilities.createOrderDTOA(null, null);
        orderDTO.setQuantity(123L);

        String orderJSON = objectMapper.writeValueAsString(orderDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/orders/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateOrderReturnsUpdatedOrder() throws Exception{

        OrderEntity order = TestDataUtilities.createOrderEntityA(null, null);
        OrderEntity savedOrder = orderService.save(order);

        OrderDTO orderDTO = TestDataUtilities.createOrderDTOA(null, null);
        orderDTO.setQuantity(123L);

        String orderJSON = objectMapper.writeValueAsString(orderDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/orders/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity").value(123L)
        );
    }

    @Test
    public void testThatDeleteOrderReturnsHttpStatusNoContentWhenOrderExists() throws Exception{

        OrderEntity order = TestDataUtilities.createOrderEntityA(null, null);
        OrderEntity savedOrder = orderService.save(order);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/orders/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteOrdersReturnsHttpStatusNoContentWhenOrderDoesNotExist() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/orders/2")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
