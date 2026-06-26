package dev.shop.backend.controllers;

import dev.shop.backend.TestDataUtilities;
import dev.shop.backend.domain.dto.ProductDTO;
import dev.shop.backend.domain.entities.ProductEntity;
import dev.shop.backend.domain.entities.UserEntity;
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
public class ProductControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ProductControllerIntegrationTests(MockMvc mockMvc, ProductService productService, UserService userService){
        this.mockMvc = mockMvc;
        this.productService = productService;
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateProductReturnsHttpStatusCreated() throws Exception{

        ProductEntity productA = TestDataUtilities.createProductEntityA(null);

        String productJSON = objectMapper.writeValueAsString(productA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateProductReturnsProduct() throws Exception{


        ProductEntity productA = TestDataUtilities.createProductEntityA(null);

        String productJSON = objectMapper.writeValueAsString(productA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(productA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.type").value(productA.getType())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.state").value(productA.getState())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.quantity").value(productA.getQuantity())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(productA.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.price").value(productA.getPrice())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.rating").value(productA.getRating())
        );
    }

    @Test
    public void testThatListAllReturnsHttpStatusOK() throws Exception{

        ProductEntity productA = TestDataUtilities.createProductEntityA(null);
        ProductEntity savedProduct = productService.save(productA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }
    @Test
    public void testThatListAllReturnsListOfProducts() throws Exception{


        ProductEntity productA = TestDataUtilities.createProductEntityA(null);
        ProductEntity savedProduct = productService.save(productA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value(savedProduct.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].type").value(savedProduct.getType())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].description").value(savedProduct.getDescription())
        );
    }

    @Test
    public void testThatGetSellerProductsReturnsHttpStatusOK() throws Exception{
        UserEntity userA = TestDataUtilities.createTestUserEntityA();
        UserEntity savedUser = userService.save(userA);

        UserEntity user = TestDataUtilities.createTestUserEntityAForRequests(savedUser.getEmail());

        ProductEntity productA = TestDataUtilities.createProductEntityA(user);
        productService.save(productA);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/products?email=" + user.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindAllProductsByUserReturnsListOfProducts() throws Exception{
        UserEntity userA = TestDataUtilities.createTestUserEntityA();
        UserEntity savedUser = userService.save(userA);

        UserEntity user = TestDataUtilities.createTestUserEntityAForRequests(savedUser.getEmail());

        ProductEntity productA = TestDataUtilities.createProductEntityA(user);
        productService.save(productA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/products?email=" + user.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value(productA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].type").value(productA.getType())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].state").value(productA.getState())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].quantity").value(productA.getQuantity())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].description").value(productA.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].price").value(productA.getPrice())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].rating").value(productA.getRating())
        );
    }

    @Test
    public void testThatGetProductReturnsHttpStatusOKWhenProductExists() throws Exception{

        ProductEntity productA = TestDataUtilities.createProductEntityA(null);
        ProductEntity savedProduct = productService.save(productA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetProductReturnsHttpStatusNotFoundWhenProductDoesNotExist() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.get("/products/2")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetProductReturnsProduct() throws Exception{

        ProductEntity productA = TestDataUtilities.createProductEntityA(null);
        ProductEntity savedProduct = productService.save(productA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/products/" + savedProduct.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(savedProduct.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.type").value(savedProduct.getType())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(savedProduct.getDescription())
        );
    }

    @Test
    public void testThatPartialProductUpdateReturnsHttpStatusOKWhenProductExists() throws Exception{


        ProductEntity productA = TestDataUtilities.createProductEntityA(null);
        ProductEntity savedProduct = productService.save(productA);

        ProductDTO productDTO = TestDataUtilities.createProductDTOA(null);
        productDTO.setName("UPDATED_NAME");

        String productJSON = objectMapper.writeValueAsString(productDTO);


        mockMvc.perform(
                MockMvcRequestBuilders.patch("/products/" + productA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialProductUpdateReturnsHttpStatusNotFoundWhenProductDoesNotExist() throws Exception{

        ProductDTO productDTO = TestDataUtilities.createProductDTOA(null);
        productDTO.setName("UPDATED_NAME");

        String productJSON = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/products/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialProductUpdateReturnsUpdatedProduct() throws Exception{


        ProductEntity productA = TestDataUtilities.createProductEntityA(null);
        ProductEntity savedProduct = productService.save(productA);

        ProductDTO productDTO = TestDataUtilities.createProductDTOA(null);
        productDTO.setName("UPDATED_NAME");

        String productJSON = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedProduct.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED_NAME")
        );
    }

    @Test
    public void testThatDeleteProductReturnsHttpStatusNoContentWhenProductExists() throws Exception{

        ProductEntity productA = TestDataUtilities.createProductEntityA(null);
        ProductEntity savedProduct = productService.save(productA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/products/" + savedProduct.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteProductReturnsHttpStatusNoContentWhenProductDoesNotExist() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/products/2")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

}
