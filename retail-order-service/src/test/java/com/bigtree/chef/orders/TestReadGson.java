package com.bigtree.chef.orders;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TestReadGson {

    @Test
    public void testReadNetwork() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("https://api.citybik.es/v2/networks/visa-frankfurt",
                String.class);
        System.out.println(response.getBody());
        try {
            JSONObject jsonObject = new JSONObject(response.getBody());
            JSONObject network = jsonObject.getJSONObject("network");
            //Assert if Network is not Null
            Assertions.assertNotNull(network);
            //Assert if Location is not Null
            JSONObject location = network.getJSONObject("location");
            Assertions.assertNotNull(location);
            Assertions.assertEquals(location.get("city"), "Frankfurt");
            Assertions.assertEquals(location.get("country"), "DE");
            System.out.println(location.get("latitude"));
            System.out.println(location.get("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
