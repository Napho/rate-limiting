package com.irembo.interview.controller;

import com.irembo.interview.repository.Client;
import com.irembo.interview.repository.ClientRepository;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.BucketProxy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.distributed.proxy.RemoteBucketBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest(classes = RateLimitTestConfiguration.class)
@TestPropertySource(locations = {
        "classpath:application-test.properties"
})
public class RateLimitedControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Mock
    ProxyManager buckets;

    @Mock
    RemoteBucketBuilder remoteBucketBuilder;

    @Mock
    ClientRepository clientRepository;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
    }

    @Test
    public void testPerSecondTPSLimitation() throws Exception {
        String url = "/service/sendSMS";

        when(buckets.builder()).thenReturn(remoteBucketBuilder);
        when(remoteBucketBuilder.build(
                anyString(), ArgumentMatchers.<Supplier<BucketConfiguration>>any()))
                .thenReturn(mock(BucketProxy.class));
        when(clientRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(new Client(1l,"test1","test",10l,20l)));

        IntStream.rangeClosed(1, 13)
                .boxed()
                .sorted(Collections.reverseOrder())
                .forEach(counter -> {
                    successfulWebRequest(url);
                });

        blockedWebRequestDueToRateLimit(url);
    }

    private void successfulWebRequest(String url) {
        try {

            this.mockMvc
                    .perform(post(url).header("clientId","test1"))
                    .andExpect(content().string(containsString("{\"statusCode\":0,\"description\":\"Successfully Sent\"}")))
                    .andExpect(status().isOk());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private void blockedWebRequestDueToRateLimit(String url) throws Exception {
        this.mockMvc
                .perform(post(url).header("clientId","test1"))
                .andExpect(status().is(HttpStatus.TOO_MANY_REQUESTS.value()));
    }


}
