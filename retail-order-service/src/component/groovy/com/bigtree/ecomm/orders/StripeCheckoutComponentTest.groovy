package com.bigtree.ecomm.orders

import org.json.JSONObject
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
class StripeCheckoutComponentTest extends Specification {

    @Autowired
    MockMvc mockMvc

    def 'Generate Stripe Payment Intent'() {
        given:
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json")
        JSONObject body = new JSONObject().put("data", LocalDate.now());

        String content = Fixtures.getFixture("payment-intent-request.json");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/create-payment-intent").headers(headers).content(content)

        when: 'POST call to /create-payment-intent endpoint'
        ResultActions resultActions = mockMvc.perform(request)

        then: 'Expect response 200 OK'
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath('$.id').isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath('$.liveMode').value(false))
                .andExpect(MockMvcResultMatchers.jsonPath('$.chargesUrl').isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath('$.clientSecret').isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath('$.object').value("payment_intent"))
                .andExpect(MockMvcResultMatchers.jsonPath('$.paymentMethod').value("card"))
    }

}
