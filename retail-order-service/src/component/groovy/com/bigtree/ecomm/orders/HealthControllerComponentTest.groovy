package com.bigtree.ecomm.orders

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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HealthControllerComponentTest extends Specification {

    @Autowired
    MockMvc mockMvc

    def "GET /health"() {
        given: ''
        HttpHeaders headers = new HttpHeaders();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/health").headers(headers)

        when: 'GET call to health endpoint'
        ResultActions resultActions = mockMvc.perform(request)

        then: 'Expect health response 200 OK'
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath('$.status').value("UP"))
    }
}

