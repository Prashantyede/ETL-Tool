package com.example.etl.service;

import com.example.etl.exception.EtlJobException;
import com.example.etl.model.InputRecord;
import com.example.etl.model.OutputRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EtlJobService {

    private static final Logger logger = LoggerFactory.getLogger(EtlJobService.class);

    @Autowired private SourceService src;
    @Autowired private TransformService xf;
    @Autowired private DestinationService dst;

    private volatile boolean isRunning = false;

    public Map<String, Object> runJob() {
        if (isRunning) throw new EtlJobException("ETL Job is already running");
        isRunning = true;

        int extracted = 0, transformed = 0, loaded = 0;

        logger.info("ETL job started...");
        try {
            src.init();  // This may throw IOException
            while (src.hasMore()) {
                List<InputRecord> batch = src.readNextPage();
                extracted += batch.size();
                List<OutputRecord> out = xf.transform(batch);
                transformed += out.size();

                if (!out.isEmpty()) {
                    int inserted = dst.bulkInsert(out);
                    loaded += inserted;
                    logger.info("Loaded {} records", inserted);
                }
            }

            logger.info("ETL job completed. Extracted: {}, Transformed: {}, Loaded: {}", extracted, transformed, loaded);
            Map<String, Object> res = new HashMap<>();
            res.put("extracted", extracted);
            res.put("transformed", transformed);
            res.put("loaded", loaded);
            return res;

        } catch (Exception e) {
            logger.error("ETL job failed", e);
            throw new EtlJobException("ETL job failed: " + e.getMessage(), e);
        } finally {
            isRunning = false;
        }
    }

    @Scheduled(cron = "${etl.scheduler.cron}")
    public void scheduledRun() {
        logger.info("Running scheduled ETL job...");
        runJob();
    }

    public boolean isJobRunning() {
        return isRunning;
    }
}
