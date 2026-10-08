package com.learningplatform.paths;

import java.util.List;

public record Milestone(String title , List<String> objectives) {
    public Milestone{
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is mandatory");
        }
        if (objectives == null || objectives.isEmpty()) {
            throw new IllegalArgumentException("Objectives is mandatory");
        }

        if (objectives.stream().anyMatch(p -> p == null || p.isBlank())) {
            throw new IllegalArgumentException("Objectives cannot contain null or blank values");
        }

        objectives = List.copyOf(objectives);
    }
}
