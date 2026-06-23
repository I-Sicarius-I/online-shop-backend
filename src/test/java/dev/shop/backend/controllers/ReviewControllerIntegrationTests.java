package dev.shop.backend.controllers;

import dev.shop.backend.TestDataUtilities;
import dev.shop.backend.domain.dto.ReviewDTO;
import dev.shop.backend.domain.entities.ReviewEntity;
import dev.shop.backend.service.ReviewService;
import dev.shop.backend.service.impl.ReviewServiceImpl;
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
public class ReviewControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ReviewService reviewService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ReviewControllerIntegrationTests(MockMvc mockMvc, ReviewService reviewService){
        this.mockMvc = mockMvc;
        this.reviewService = reviewService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateReviewReturnsHttpStatusCreated() throws Exception{

        ReviewEntity reviewEntity = TestDataUtilities.createReviewEntityA(null, null);

        String reviewJSON = objectMapper.writeValueAsString(reviewEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateReviewReturnsCreatedReview() throws Exception{

        ReviewEntity reviewEntity = TestDataUtilities.createReviewEntityA(null, null);

        String reviewJSON = objectMapper.writeValueAsString(reviewEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.text").value(reviewEntity.getText())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.rating").value(reviewEntity.getRating())
        );
    }

    @Test
    public void testThatListAllReviewsReturnsHttpStausOK() throws Exception{

        ReviewEntity reviewEntity = TestDataUtilities.createReviewEntityA(null, null);
        reviewService.save(reviewEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAllReviewsReturnsListOfReviews() throws Exception{

        ReviewEntity reviewEntity = TestDataUtilities.createReviewEntityA(null, null);
        reviewService.save(reviewEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].text").value(reviewEntity.getText())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].rating").value(reviewEntity.getRating())
        );
    }

    @Test
    public void testThatGetReviewReturnsHttpStatusOKWhenReviewExists() throws Exception{

        ReviewEntity reviewEntity = TestDataUtilities.createReviewEntityA(null, null);
        ReviewEntity savedReview = reviewService.save(reviewEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/reviews/" + savedReview.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetReviewReturnsHttpStatusNotFoundWhenReviewDoesNotExist() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.get("/reviews/2")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetReviewReturnsFoundReview() throws Exception{

        ReviewEntity reviewEntity = TestDataUtilities.createReviewEntityA(null, null);
        ReviewEntity savedReview = reviewService.save(reviewEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/reviews/" + savedReview.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.text").value(reviewEntity.getText())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.rating").value(reviewEntity.getRating())
        );
    }

    @Test
    public void testThatPartialUpdateReviewReturnsHttpStatusOKWhenReviewExists() throws Exception{

        ReviewEntity reviewEntity = TestDataUtilities.createReviewEntityA(null, null);
        ReviewEntity savedEntity = reviewService.save(reviewEntity);

        ReviewDTO reviewDTO = TestDataUtilities.createReviewDTOA(null, null);
        reviewDTO.setText("UPDATED_TEXT");

        String reviewJSON = objectMapper.writeValueAsString(reviewDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/reviews/" + savedEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateReviewReturnsHttpStatusNotFoundWhenReviewDoesNotExist() throws Exception{

        ReviewDTO reviewDTO = TestDataUtilities.createReviewDTOA(null, null);
        reviewDTO.setText("UPDATED_TEXT");

        String reviewJSON = objectMapper.writeValueAsString(reviewDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/reviews/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateReviewReturnsUpdatedReview() throws Exception{

        ReviewEntity reviewEntity = TestDataUtilities.createReviewEntityA(null, null);
        ReviewEntity savedReview = reviewService.save(reviewEntity);

        ReviewDTO reviewDTO = TestDataUtilities.createReviewDTOA(null, null);
        reviewDTO.setText("UPDATED_TEXT");

        String reviewJSON = objectMapper.writeValueAsString(reviewDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/reviews/" + savedReview.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedReview.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.text").value("UPDATED_TEXT")
        );
    }

    @Test
    public void testThatDeleteReviewReturnsHttpStatusNoContentWhenReviewExists() throws Exception{

        ReviewEntity reviewEntity = TestDataUtilities.createReviewEntityA(null, null);
        ReviewEntity savedReview = reviewService.save(reviewEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/reviews/" + savedReview.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteReviewReturnsHttpStatusNoContentWhenReviewDoesNotExist() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
