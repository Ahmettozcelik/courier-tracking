package com.example.courier_tracking.service;


import com.example.courier_tracking.entity.Store;
import com.example.courier_tracking.repository.StoreRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

@Service
public class StoreService {

    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @PostConstruct
    public void loadStoresFromJson() {
        try {
            if (storeRepository.count() == 0) {
                ObjectMapper mapper = new ObjectMapper();
                InputStream inputStream = new ClassPathResource("stores.json").getInputStream();
                List<Store> stores = mapper.readValue(inputStream, new TypeReference<>() {
                });
                storeRepository.saveAll(stores);
                logger.info("✅ Store data loaded from stores.json");
            }
        } catch (Exception e) {
            logger.info("❌ Failed to load store data: " + e.getMessage());
        }
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }
}

