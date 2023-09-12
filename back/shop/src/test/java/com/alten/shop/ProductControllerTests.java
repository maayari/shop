package com.alten.shop;

import static org.mockito.ArgumentMatchers.any;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.alten.shop.enums.CatogoryEnum;
import com.alten.shop.enums.InventoryStatus;
import com.alten.shop.model.Product;
import com.alten.shop.service.ImageService;
import com.alten.shop.service.ProductService;
import com.alten.shop.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author ayarima
 *
 */
@WebMvcTest
public class ProductControllerTests {

	@Autowired
    private MockMvc mockMvc;

	@MockBean
    private ProductService productService;

	@MockBean
    private ImageService imageService;

	@Autowired
    private ObjectMapper objectMapper;

	/** JUnit test for create product REST API
	 * @throws Exception
	 */
	@Test
    public void createProductTest() throws Exception{
        // given - precondition or setup
		Product product = Product.builder()
                .code("xd2f2f").name("name").description("description").category(CatogoryEnum.Accessories)
                .inventoryStatus(InventoryStatus.INSTOCK).price(11).quantity(100).rating(5).build();
        given(productService.saveProduct(any(Product.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(product)));
        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.code",
                        is(product.getCode())))
                .andExpect(jsonPath("$.name",
                        is(product.getName())))
                .andExpect(jsonPath("$.description",
                        is(product.getDescription())));

    }

    /** JUnit test for Get All listOfProducts REST API
     * @throws Exception
     */
    @Test
    public void givenListOfProductsTest() throws Exception{
        // given - precondition or setup
        List<Product> listOfProducts = new ArrayList<>();
        listOfProducts.add(Product.builder()
                .code("xd2f2f").name("name").description("description").category(CatogoryEnum.Accessories).inventoryStatus(InventoryStatus.INSTOCK)
                .price(11).quantity(100).rating(5).build());
        listOfProducts.add(Product.builder()
                .code("wldlds").name("name").description("description").category(CatogoryEnum.Fitness).inventoryStatus(InventoryStatus.INSTOCK)
                .price(11).quantity(100).rating(5).build());
        given(productService.findAllProduct()).willReturn(listOfProducts);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/products"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfProducts.size())));

    }

    /** JUnit test for GET Product by id REST API
     * @throws Exception
     */
    @Test
    public void getProductTest() throws Exception{
        // given - precondition or setup
        long productId = 1L;
        Product product = Product.builder().id(productId)
                .code("xd2f2f").name("name").description("description").category(CatogoryEnum.Accessories).inventoryStatus(InventoryStatus.INSTOCK)
                .price(11).quantity(100).rating(5).build();
        given(productService.getProduct(productId)).willReturn(product);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/products/{id}", productId));

        response.andDo(print()).
        andExpect(status().isOk())
        .andExpect(jsonPath("$.code",
                is(product.getCode())))
        .andExpect(jsonPath("$.name",
                is(product.getName())))
        .andExpect(jsonPath("$.description",
                is(product.getDescription())));

    }
    
    /** JUnit test for GET Product by id REST API , product not found.
     * @throws Exception
     */
    @Test
    public void getProductNotFountTest() throws Exception{
    	// given - precondition or setup
        long productId = 1L;
        given(productService.getProduct(productId)).willReturn(null);
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/products/{id}", productId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print()).andExpect(jsonPath("$.message",
                        is(Constants.RESOURCE_NOT_FOUNT_MSG)));
    }
 
    /** JUnit test for DELETE Product by id REST API
     * @throws Exception
     */
    @Test
    public void deleteProductTest() throws Exception{
        // given - precondition or setup
    	long productId = 1L;
        willDoNothing().given(productService).deleteProductById(productId);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/products/{id}", productId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
