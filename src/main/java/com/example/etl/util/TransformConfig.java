package com.example.etl.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;

public class TransformConfig {
	
	private Map<String, String> rename;
    private Map<String, Map<String, String>> dateConvert;
    private List<Map<String, Object>> filters;
    private Map<String, Map<String, String>> computedFields;
    private int sourcePaginationSize;
	
    public Map<String, String> getRename() {
		return rename; 
	}
	public void setRename(Map<String, String> rename) {
		this.rename = rename;
	}
	public Map<String, Map<String, String>> getDateConvert() {
		return dateConvert;
	}
	public void setDateConvert(Map<String, Map<String, String>> dateConvert) {
		this.dateConvert = dateConvert;
	}
	public List<Map<String, Object>> getFilters() {
		return filters;
	}
	public void setFilters(List<Map<String, Object>> filters) {
		this.filters = filters;
	}
	public Map<String, Map<String, String>> getComputedFields() {
		return computedFields;
	}
	public void setComputedFields(Map<String, Map<String, String>> computedFields) {
		this.computedFields = computedFields;
	}
	public int getSourcePaginationSize() {
		return sourcePaginationSize;
	}
	public void setSourcePaginationSize(int sourcePaginationSize) {
		this.sourcePaginationSize = sourcePaginationSize;
	}

	 public static TransformConfig loadFromYaml(String path) {
	        Yaml yaml = new Yaml();
	        try (InputStream in = TransformConfig.class.getClassLoader().getResourceAsStream(path)) {
	            if (in == null) throw new RuntimeException("YAML not found: " + path);
	            return yaml.loadAs(in, TransformConfig.class);
	        } catch (Exception e) {
	            throw new RuntimeException("Failed to load transform config: " + e.getMessage(), e);
	        }
     }
}