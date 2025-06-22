package com.example.etl.service;

import com.example.etl.model.InputRecord;
import com.example.etl.model.OutputRecord;
import com.example.etl.util.TransformConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.*;

@Service
public class TransformService {
    private static final Logger logger = LoggerFactory.getLogger(TransformService.class);

    @Autowired
    private TransformConfig cfg;

    public List<OutputRecord> transform(List<InputRecord> input) {
        List<OutputRecord> outs = new ArrayList<>();
        logger.info("Starting transformation of {} records", input.size());

        for (InputRecord in : input) {
            if (!passesFilters(in)) continue;

            OutputRecord out = new OutputRecord();
            out.setGivenName(in.getFirstName());
            out.setFamilyName(in.getLastName());

            Map<String, Map<String, String>> dc = cfg.getDateConvert();
            if (dc != null && dc.containsKey("birthDate")) {
                try {
                    Map<String, String> birthDateMap = dc.get("birthDate");
                    DateTimeFormatter from = DateTimeFormatter.ofPattern(birthDateMap.get("from"));
                    DateTimeFormatter to = DateTimeFormatter.ofPattern(birthDateMap.get("to"));
                    LocalDate d = LocalDate.parse(in.getBirthDate(), from);
                    out.setBirthDateStr(d.format(to));
                } catch (Exception e) {
                    logger.error("Date conversion failed: {}", e.getMessage());
                    out.setBirthDateStr(in.getBirthDate());
                }
            } else {
                logger.warn("Missing dateConvert.birthDate config");
                out.setBirthDateStr(in.getBirthDate());
            }

            out.setAge(in.getAge());
            out.setFullName(out.getGivenName() + " " + out.getFamilyName());
            outs.add(out);
        }

        logger.info("Transformation complete: {} records transformed", outs.size());
        return outs;
    }

    private boolean passesFilters(InputRecord in) {
        List<Map<String, Object>> filters = cfg.getFilters();
        if (filters == null) return true;

        for (Map<String, Object> f : filters) {
            if ("age".equals(f.get("field"))) {
                Object value = f.get("value");
                if (value instanceof Integer) {
                    int val = (Integer) value;
                    if (!(in.getAge() > val)) {
                        logger.debug("Record filtered out: age {} <= {}", in.getAge(), val);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
