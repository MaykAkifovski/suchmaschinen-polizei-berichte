package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.services;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.PoliceReportTransformed;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PoliceReportTransformerTest {

    @Test
    public void extractDateLocationTest() {
        PoliceReportTransformer transformer = new PoliceReportTransformer();
        PoliceReportTransformed report = new PoliceReportTransformed("id", "originID", new ArrayList<String>(), 1111, null, null);
        System.out.println("report before = " + report);
        System.out.println();

        String header = "Polizeimeldung vom 29.12.2014\n" +
                "Pankow\n" +
                "Nr. 3086";

        transformer.extractDateLocation(header, report);
        assertEquals("1419807600000", String.valueOf(report.getDate()));

//        List<String> locations = Arrays.asList("Mitte", "Friedrichshain", "Kreuzberg", "Pankow", "Charlottenburg", "Wilmersdorf", "Spandau", "Steglitz", "Zehlendorf", "Tempelhof", "Schöneberg", "Neukölln", "Treptow", "Köpenick", "Marzahn", "Hellersdorf", "Lichtenberg", "Reinickendorf", "bezirksübergreifend", "berlinweit", "bundesweit");
//
//        List<String> locationsFound = new ArrayList<>();
//        String s = "Pankow";
//        System.out.println(locations.contains(s));
//        locationsFound.add("Pankow");
//
//        report.setLocation(locationsFound);

//        assertEquals("Pankow", report.getLocation().get(0));
        System.out.println();
        System.out.println("report after = " + report);
    }
}