package org.openfact.reports;

import java.util.HashMap;
import java.util.Map;

public class ReportTemplateConfiguration {

    private final String themeName;
    private final Map<String, Object> attributes;

    private ReportTemplateConfiguration(Builder builder) {
        this.themeName = builder.themeName;
        this.attributes = builder.attributes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getThemeName() {
        return themeName;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public static class Builder {

        private String themeName;
        private Map<String, Object> attributes = new HashMap<>();

        public Builder themeName(String themeName) {
            this.themeName = themeName;
            return this;
        }

        public Builder addAttribute(String name, Object value) {
            this.attributes.put(name, value);
            return this;
        }

        public Builder addAllAttributes(Map<String, Object> attributes) {
            this.attributes.putAll(attributes);
            return this;
        }

        public ReportTemplateConfiguration build() {
            return new ReportTemplateConfiguration(this);
        }

    }
}