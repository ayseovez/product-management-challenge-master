package com.iyzico.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.payload.request.ProductPaymentRequest;
import com.iyzico.challenge.payload.response.ProductPaymentResponse;
import com.iyzico.challenge.service.ProductPaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PaymentControllerIntegrationTest {

    @MockBean
    private ProductPaymentService productPaymentService;

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void productPay() throws Exception {
        ProductPaymentResponse response = new ProductPaymentResponse();
        response.setReturnCode("200");
        response.setReturnMessage("Your payment has been successfully completed.");

        when(productPaymentService.productPay(any(ProductPaymentRequest.class))).thenReturn(response);

        mockMvc.perform(post("http://localhost:8080/api/iyzicoPay/payment")
                .content(mapper.writeValueAsString(response))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returnCode", is("200")))
                .andExpect(jsonPath("$.returnMessage", is("Your payment has been successfully completed.")));

        verify(productPaymentService, times(1)).productPay(any(ProductPaymentRequest.class));
    }
}