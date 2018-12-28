package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReportTransformed;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class PoliceReportTransformerTest {

    @Test
    public void extractDateLocation() {
        PoliceReportTransformer transformer = new PoliceReportTransformer();
        PoliceReportTransformed report = new PoliceReportTransformed("id", "originID", new ArrayList<String>(), 1111, null, null);
        System.out.println("report before = " + report);
        System.out.println();

        String header = "Polizeimeldung vom 29.12.2014 " +
                "Pankow\n" +
                "Nr. 3086";

        transformer.extractDateLocation(header, report);
        assertEquals("1419807600000", String.valueOf(report.getDate()));

        assertEquals("Pankow", report.getLocation().get(0));
        System.out.println();
        System.out.println("report after = " + report);
    }

    @Test
    public void extractSeveralLocations() {
        PoliceReportTransformer transformer = new PoliceReportTransformer();
        PoliceReportTransformed report = new PoliceReportTransformed("id", "originID", new ArrayList<String>(), 1111, null, null);
        System.out.println("report before = " + report);
        System.out.println();

        String header = "Polizeimeldung vom 31.12.2014\n" +
                "Charlottenburg - Wilmersdorf\n" +
                "Nr. 3097";

        transformer.extractDateLocation(header, report);
        assertEquals("1419980400000", String.valueOf(report.getDate()));

        Set<String> set = new HashSet<>(Arrays.asList("Charlottenburg", "Wilmersdorf"));
        assertEquals(set, new HashSet<>(report.getLocation()));
        System.out.println();
        System.out.println("report after = " + report);
    }
}