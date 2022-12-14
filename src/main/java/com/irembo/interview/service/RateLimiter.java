package com.irembo.interview.service;

import com.irembo.interview.repository.Client;
import com.irembo.interview.repository.ClientRepository;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.TokensInheritanceStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class RateLimiter {

    private final ClientRepository clientRepository;
    private final ProxyManager buckets;

    @Autowired
    public RateLimiter(final ClientRepository clientRepository, ProxyManager buckets){
        this.clientRepository = clientRepository;
        this.buckets = buckets;
    }

    public Bucket resolveBucket(String key) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplierForClient(key);

        return buckets.builder().build(key, configSupplier);
    }

    public Supplier<BucketConfiguration> getConfigSupplierForClient(String username) {
        Optional<Client> clientOptional = clientRepository.findByUsername(username);

        if(!clientOptional.isPresent()){
            throw new RuntimeException("Failed to fetch subscription data");
        }

        final Client client = clientOptional.get();
        final Long monthLimit = client.getTpm();
        final Long burstLimit = client.getTps();


        Refill refill = Refill.intervally(monthLimit,Duration.ofHours(24 * 30));
        Bandwidth limit = Bandwidth.classic(monthLimit, refill);


        return () -> (BucketConfiguration.builder()
                .addLimit(limit)
                .addLimit(Bandwidth.simple(burstLimit,Duration.ofSeconds(1)))
                .build());
    }

    @Transactional
    public void buyTPS(String username,Long newTps) {
        Optional<Client> clientOptional  = clientRepository.findByUsername(username);

        if(clientOptional.isPresent()){
            Client client = clientOptional.get();
            client.setTps(newTps);

            clientRepository.save(client);

            final Long monthLimit = client.getTpm();
            final Long burstLimit = client.getTps();

            Refill refill = Refill.intervally(monthLimit, Duration.of(1, ChronoUnit.MONTHS));
            Bandwidth limit = Bandwidth.classic(monthLimit, refill);


            BucketConfiguration bc = BucketConfiguration.builder()
                    .addLimit(limit)
                    .addLimit(Bandwidth.simple(burstLimit,Duration.ofSeconds(1)))
                    .build();

           resolveBucket(username).replaceConfiguration(bc, TokensInheritanceStrategy.ADDITIVE);

        }else{
            throw new RuntimeException("Failed to find client with username "+username);
        }
    }
}
