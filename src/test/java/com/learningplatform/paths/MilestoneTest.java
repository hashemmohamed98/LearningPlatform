package com.learningplatform.paths;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MilestoneTest {
    @Test
    void constructorDefensivelyCopiesObjectives() {
        List<String> objectives = new ArrayList<>();
        objectives.add("learn");
        Milestone milestone = new Milestone("Milestone", objectives);
        objectives.add("practice");
        assertEquals(1, milestone.objectives().size());
    }

    @Test
    void nullTitleIsRejected() {
        List<String> objectives = new ArrayList<>();
        objectives.add("learn");
        assertThrows(IllegalArgumentException.class, () -> new Milestone(null, objectives));
    }

    @Test
    void blankTitleIsRejected() {
        List<String> objectives = new ArrayList<>();
        objectives.add("learn");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Milestone("   ", objectives)
        );
    }

    @Test
    void nullObjectivesIsRejected() {
        String title = "Milestone";
        assertThrows(IllegalArgumentException.class, () -> new Milestone(title, null));
    }

    @Test
    void emptyObjectivesAreRejected() {
        String title = "Milestone";
        assertThrows(IllegalArgumentException.class, () -> new Milestone(title, new ArrayList<>()));
    }

    @Test
    void objectivesCannotBeModifiedAfterConstruction() {
        List<String> objectives = new ArrayList<>();
        objectives.add("learn");
        objectives.add("practice");
        Milestone milestone = new Milestone("Milestone", objectives);
        assertThrows(UnsupportedOperationException.class, () -> milestone.objectives().add("review"));
    }
    @Test
    void nullObjectivesAreRejected() {
        List<String> objectives = new ArrayList<>();
        objectives.add(null);
        assertThrows(NullPointerException.class, () -> new Milestone("Milestone", objectives));
    }

    @Test
    void nullObjectiveIsRejected() {
        List<String> objectives = new ArrayList<>();
        objectives.add(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> new Milestone("Milestone", objectives)
        );
    }

    @Test
    void blankObjectiveIsRejected() {
        List<String> objectives = new ArrayList<>();
        objectives.add("   ");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Milestone("Milestone", objectives)
        );
    }
}
