package dev.shop.backend.controllers;

import dev.shop.backend.TestDataUtilities;
import dev.shop.backend.domain.dto.OrderDTO;
import dev.shop.backend.domain.entities.OrderEntity;
import dev.shop.backend.domain.entities.ProductEntity;
import dev.shop.backend.domain.entities.UserEntity;
import dev.shop.backend.service.OrderService;
import dev.shop.backend.service.ProductService;
import dev.shop.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
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
@WithMockUser("test@email.com")
public class OrderControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;
    private UserDetails currentUser;

    @Autowired
    public OrderControllerIntegrationTests(MockMvc mockMvc, OrderService orderService, UserService userService, ProductService productService){
        this.mockMvc = mockMvc;
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void createUser(){
        UserDetails userDetails = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserDetails){
            userDetails = (UserDetails) principal;
        }

        UserEntity user = TestDataUtilities.createTestUserEntityAForRequests(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setUsername("testUser");
        user.setRole("ROLE_USER");

        UserEntity savedUser = userService.save(user);
        this.currentUser = userDetails;
    }


    @Test
    public void testThatCreateOrderReturnsHttpStatusCreated() throws Exception{

        ProductEntity product = TestDataUtilities.createProductEntityA(null);
        productService.save(product);

        OrderEntity orderA = TestDataUtilities.createOrderEntityA(null, product.getId());


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
    public void testThatCreateOrderReturnsHttpStatusBadRequestWhenProductDoesNotExist() throws Exception{


        OrderEntity orderA = TestDataUtilities.createOrderEntityA("test@test.com", 2L);

        String orderJSON = objectMapper.writeValueAsString(orderA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateOrderWithInvalidQuantityReturnsHttpStatusBadRequest() throws Exception{

        ProductEntity product = TestDataUtilities.createProductEntityA(null);
        productService.save(product);

        OrderEntity order = TestDataUtilities.createOrderEntityA(null, product.getId());
        order.setQuantity(100L);

        String orderJSON = objectMapper.writeValueAsString(order);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatCreateOrderReturnsCreatedOrder() throws Exception{
        ProductEntity product = TestDataUtilities.createProductEntityA(null);
        productService.save(product);

        OrderEntity order = TestDataUtilities.createOrderEntityA(null, product.getId());

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

        ProductEntity productEntity = TestDataUtilities.createProductEntityA(savedUser.getEmail());
        ProductEntity savedProduct = productService.save(productEntity);

        OrderEntity order = TestDataUtilities.createOrderEntityA(savedUser.getEmail(), savedProduct.getId());
        OrderEntity savedOrder = orderService.save(order);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders?email=" + savedUser.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListOrdersByUserReturnsListOfOrders() throws Exception{
        UserEntity userA = TestDataUtilities.createTestUserEntityA();
        UserEntity savedUser = userService.save(userA);

        ProductEntity productEntity = TestDataUtilities.createProductEntityA(savedUser.getEmail());
        ProductEntity savedProduct = productService.save(productEntity);

        OrderEntity order = TestDataUtilities.createOrderEntityA(savedUser.getEmail(), savedProduct.getId());
        OrderEntity savedOrder = orderService.save(order);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders?email=" + savedUser.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].quantity").value(savedOrder.getQuantity())
        );
    }

    @Test
    public void testThatListOrdersByProductReturnsHttpStatusOK() throws Exception{
        UserEntity userA = TestDataUtilities.createTestUserEntityA();
        UserEntity savedUser = userService.save(userA);


        ProductEntity productEntity = TestDataUtilities.createProductEntityA(savedUser.getEmail());
        ProductEntity savedProduct = productService.save(productEntity);

        OrderEntity order = TestDataUtilities.createOrderEntityA(savedUser.getEmail(), savedProduct.getId());
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


        ProductEntity productEntity = TestDataUtilities.createProductEntityA(savedUser.getEmail());
        ProductEntity savedProduct = productService.save(productEntity);

        productEntity.setId(savedProduct.getId());

        OrderEntity order = TestDataUtilities.createOrderEntityA(savedUser.getEmail(), savedProduct.getId());
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

        OrderEntity order = TestDataUtilities.createOrderEntityA(currentUser.getUsername(), null);
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

        OrderEntity order = TestDataUtilities.createOrderEntityA(currentUser.getUsername(), null);
        OrderEntity savedOrder = orderService.save(order);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/" + order.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity").value(order.getQuantity())
        );
    }

    @Test
    public void testThatPartialUpdateOrderReturnsHttpStatusOKWhenOrderExists() throws Exception{

        ProductEntity product = TestDataUtilities.createProductEntityA(null);
        productService.save(product);

        OrderEntity order = TestDataUtilities.createOrderEntityA(currentUser.getUsername(), product.getId());
        OrderEntity savedOrder = orderService.save(order);

        OrderDTO orderDTO = TestDataUtilities.createOrderDTOA(currentUser.getUsername(), product.getId());
        orderDTO.setQuantity(1L);

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

        OrderDTO orderDTO = TestDataUtilities.createOrderDTOA(currentUser.getUsername(), null);
        orderDTO.setQuantity(1L);

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
    public void testThatPartialUpdateOrderReturnsHttpStatusUnauthorizedWhenOrderOwnerDoesNotMatchCurrentUser() throws Exception{

        UserEntity user = TestDataUtilities.createTestUserEntityA();
        String email = userService.save(user).getEmail();

        OrderEntity order = TestDataUtilities.createOrderEntityA(email, null);
        OrderEntity savedOrder = orderService.save(order);

        OrderDTO orderDTO = TestDataUtilities.createOrderDTOA(currentUser.getUsername(), null);
        orderDTO.setQuantity(123L);

        String orderJSON = objectMapper.writeValueAsString(orderDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/orders/" + savedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
        );
    }



    @Test
    public void testThatPartialUpdateOrderReturnsUpdatedOrder() throws Exception{

        ProductEntity product = TestDataUtilities.createProductEntityA(null);
        productService.save(product);

        OrderEntity order = TestDataUtilities.createOrderEntityA(currentUser.getUsername(), product.getId());
        OrderEntity savedOrder = orderService.save(order);

        OrderDTO orderDTO = TestDataUtilities.createOrderDTOA(currentUser.getUsername(), product.getId());
        orderDTO.setQuantity(1L);

        String orderJSON = objectMapper.writeValueAsString(orderDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/orders/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity").value(1L)
        );
    }

    @Test
    public void testThatDeleteOrderReturnsHttpStatusNoContentWhenOrderExists() throws Exception{
        ProductEntity product = TestDataUtilities.createProductEntityA(null);
        productService.save(product);

        OrderEntity order = TestDataUtilities.createOrderEntityA(currentUser.getUsername(), product.getId());
        OrderEntity savedOrder = orderService.save(order);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/orders/" + savedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteOrderReturnsHttpStatusUnauthorizedWhenOrderOwnerDoesNotMatchCurrentUser() throws Exception{

        UserEntity user = TestDataUtilities.createTestUserEntityA();
        String email = userService.save(user).getEmail();

        OrderEntity order = TestDataUtilities.createOrderEntityA(email, null);
        OrderEntity savedOrder = orderService.save(order);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/orders/" + savedOrder.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
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
