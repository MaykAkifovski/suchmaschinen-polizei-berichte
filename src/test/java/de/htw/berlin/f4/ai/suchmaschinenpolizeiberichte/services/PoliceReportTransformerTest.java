package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReportTransformed;
import org.junit.Test;

import static org.junit.Assert.*;

public class PoliceReportTransformerTest {

    @Test
    public void extractDateLocationTest() {
        PoliceReportTransformer transformer = new PoliceReportTransformer();
        PoliceReportTransformed report = new PoliceReportTransformed("id", "idOrigin", null, 1111, null, null);
        System.out.println("report = " + report);
        String header = "Polizeimeldung vom 29.12.2014\n" +
                "Pankow\n" +
                "Nr. 3086";

        transformer.extractDateLocation(header, report);
        assertEquals("1419807600000", String.valueOf(report.getDate()));
        System.out.println(report);
    }
}