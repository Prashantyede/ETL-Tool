package com.example.etl.service;

import com.example.etl.exception.EtlJobException;
import com.example.etl.model.InputRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class SourceService {

    private static final Logger logger = LoggerFactory.getLogger(SourceService.class);

    @Value("classpath:input-data.json")
    private Resource input;

    @Autowired
    private org.springframework.core.env.Environment env;

    private List<InputRecord> all;
    private int pointer;

    @PostConstruct
    public void init() {
        try {
            ObjectMapper om = new ObjectMapper();
            all = om.readValue(input.getInputStream(), new TypeReference<List<InputRecord>>() {});
            pointer = 0;
            logger.info("Loaded {} input records", all.size());
        } catch (IOException e) {
            throw new EtlJobException("Failed to read input data: " + e.getMessage(), e);
        }
    }

    public List<InputRecord> readNextPage() {
        int size = Integer.parseInt(env.getProperty("etl.source.paginationSize", "5"));
        int end = Math.min(pointer + size, all.size());
        List<InputRecord> slice = all.subList(pointer, end);
        pointer = end;
        logger.debug("Read next {} records", slice.size());
        return slice;
    }

    public boolean hasMore() {
        return pointer < all.size();
    }
}
