package com.example.etl.service;

import com.example.etl.model.OutputRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {

    private static final Logger logger = LoggerFactory.getLogger(DestinationService.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000)
    )
    public int bulkInsert(List<OutputRecord> list) {
        String sql = "INSERT INTO output_record " +
                     "(given_name, family_name, birth_date_str, age, full_name) " +
                     "VALUES (?, ?, ?, ?, ?)";

        logger.info("Attempting to insert batch of {} records into the database.", list.size());

        int[][] counts = jdbc.batchUpdate(sql, list, list.size(),
            (ps, rec) -> {
                ps.setString(1, rec.getGivenName());
                ps.setString(2, rec.getFamilyName());
                ps.setString(3, rec.getBirthDateStr());
                ps.setInt(4, rec.getAge());
                ps.setString(5, rec.getFullName());
            });

        int totalInserted = 0;
        for (int[] batch : counts) {
            for (int count : batch) {
                totalInserted += count;
            }
        }

        logger.info("Successfully inserted {} records into the database.", totalInserted);
        return totalInserted;
    }

    @Recover
    public int recover(Exception e, List<OutputRecord> list) {
        logger.error("Failed to insert batch after retries. Error: {}", e.getMessage(), e);
        return 0;
    }
}
