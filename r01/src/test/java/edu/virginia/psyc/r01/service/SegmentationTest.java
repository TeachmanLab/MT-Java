package edu.virginia.psyc.r01.service;

import edu.virginia.psyc.r01.persistence.DASS21_AS;
import edu.virginia.psyc.r01.persistence.Demographics;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.mindtrails.domain.Conditions.RandomCondition;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Not so much a test as a sanity check that we will correctly segment uesrs.
 */
public class SegmentationTest {

    Map<Long, DASS21_AS> dassMap = loadDassFromCSV();
    Map<Long, Demographics> demMap = loadDemographicsFromCSV();
    R01ParticipantService service = new R01ParticipantService();
    Collection mutualIds = CollectionUtils.intersection(dassMap.keySet(), demMap.keySet());

    // To keep testing somewhat fast, we build queus in memory, rather than use the database.
    Queue<RandomCondition> maleHigh = new LinkedList<>();
    Queue<RandomCondition> maleMed = new LinkedList<>();
    Queue<RandomCondition> femaleHigh = new LinkedList<>();
    Queue<RandomCondition> femaleMed = new LinkedList<>();

    @Test
    public void testLoadDass() throws Exception {
        List<String> dassSegments = dassMap.values().stream().map(dass21_as -> dass21_as.calculateSegmentation()).collect(Collectors.toList());
        System.out.println(String.format("DASS - Total: %s,  High Count: %s (%s %%), Low Count: %s (%s %%)",
                dassMap.size(),
                Collections.frequency(dassSegments, "high"),
                100 * Collections.frequency(dassSegments, "high")/dassMap.size(),
                Collections.frequency(dassSegments, "med"),
                100 * Collections.frequency(dassSegments, "med")/dassMap.size()));
    }

    @Test
    public void testDemographics() throws Exception {
        List<String> demSegments = demMap.values().stream().map(d -> d.calculateSegmentation()).collect(Collectors.toList());
        System.out.println(String.format("DEM  - Total: %s,  Male: %s (%s%%), Female: %s (%s%%)",
                demMap.size(),
                Collections.frequency(demSegments, "male"),
                100 * Collections.frequency(demSegments, "male")/demMap.size(),
                Collections.frequency(demSegments, "female"),
                100 * Collections.frequency(demSegments, "female")/demMap.size()));
    }

    @Test
    public void testOverlapOfIdsBetweenDassAndDemographics() throws Exception {
        System.out.println(String.format("DEM Total: %s,  DASS Total: %s, Participant ID Overlap: %s",
                demMap.size(),
                dassMap.size(),
                mutualIds.size()));
    }

    @Test
    public void testSegmentationTotalCount() {
        List<String> segments = new ArrayList<>();
        for(Object ido : mutualIds) {
            Long id = (Long) ido;
            segments.add(service.getSegmentFromDassAndDemographics(dassMap.get(ido), demMap.get(ido)));
        }
        System.out.println(String.format("male_high: %s, male_med: %s, female_high:%s, female_med,%s",
                Collections.frequency(segments, "male_high"),
                Collections.frequency(segments, "male_med"),
                Collections.frequency(segments, "female_high"),
                Collections.frequency(segments, "female_med")));
    }

    @Test
    public void testFemaleHighConditions() {
        // Generate Random Assingments for Each Segment
        Map<String, Float> valuePercentages = new HashMap<>();
        valuePercentages.put("control", 25.0f);
        valuePercentages.put("training", 75.0f);
        List<String> conditions = new ArrayList<>();
        String segment;

        for(Object ido : mutualIds) {
            segment = service.getSegmentFromDassAndDemographics(dassMap.get(ido), demMap.get(ido));
            if(segment.equals("female_high")) {
                if(femaleHigh.size() == 0) {
                    femaleHigh.addAll(RandomCondition.createBlocks(valuePercentages, 50, "female_high"));
                }
                conditions.add(femaleHigh.poll().getValue());
            }
        }
        System.out.println(String.format("FEMALE HIGH Conditions: total: %s, training: %s (%s%%), control: %s (%s%%)",
                conditions.size(),
                Collections.frequency(conditions, "training"),
                100 * Collections.frequency(conditions, "training")/conditions.size(),
                Collections.frequency(conditions, "control"),
                100 * Collections.frequency(conditions, "control")/conditions.size()));

    }

    public RandomCondition getCondition(DASS21_AS dass, Demographics dem) {
        // Generate Random Assingments for Each Segment
        Map<String, Float> valuePercentages = new HashMap<>();
        valuePercentages.put("control", 25.0f);
        valuePercentages.put("training", 75.0f);
        String segment = service.getSegmentFromDassAndDemographics(dass, dem);
        switch (segment) {
            case("male_high"):
                if(maleHigh.size() == 0) {
                    maleHigh.addAll(RandomCondition.createBlocks(valuePercentages, 50, "male_high"));
                }
                return maleHigh.poll();
            case("male_med"):
                if(maleMed.size() == 0) {
                    maleMed.addAll(RandomCondition.createBlocks(valuePercentages, 50, "male_med"));
                }
                return maleMed.poll();
            case("female_high"):
                if(femaleHigh.size() == 0) {
                    femaleHigh.addAll(RandomCondition.createBlocks(valuePercentages, 50, "female_high"));
                }
                return femaleHigh.poll();
            case("female_med"):
                if(femaleMed.size() == 0) {
                    femaleMed.addAll(RandomCondition.createBlocks(valuePercentages, 50, "female_high"));
                }
                return femaleMed.poll();
        }
        throw new RuntimeException("unknown segment.");
    }



    private Map<Long, DASS21_AS> loadDassFromCSV() {
        ICsvListReader listReader = null;
        Map<Long, DASS21_AS> dass21Map = new HashMap<>();

        Resource resource = new ClassPathResource("dass21.csv");
        InputStream resourceInputStream = null;
        try {
            resourceInputStream = resource.getInputStream();
            listReader = new CsvListReader(new InputStreamReader(resourceInputStream), CsvPreference.STANDARD_PREFERENCE);
            listReader.getHeader(true); // skip the header (can't be used with CsvListReader)

            final CellProcessor[] processors = new CellProcessor[]{
                    new ParseLong(), // id
                    new ParseLong(), // participant id
                    new NotNull(), // session
                    new ParseInt(), // breaking
                    new ParseInt(), // dryness
                    new ParseInt(), // heart
                    new ParseInt(), // panic
                    new ParseInt(), // scared
                    new ParseInt(), // trembling
                    new ParseInt(), // worry
            };

            while ((listReader.read()) != null) {
                // use different processors depending on the number of columns
                final List<Object> dassList = listReader.executeProcessors(processors);
                DASS21_AS dass = new DASS21_AS();
                dass.setId((Long)dassList.get(0));
                dass.setBreathing((Integer)dassList.get(3));
                dass.setDryness((Integer)dassList.get(4));
                dass.setHeart((Integer)dassList.get(5));
                dass.setPanic((Integer)dassList.get(6));
                dass.setScared((Integer)dassList.get(7));
                dass.setTrembling((Integer)dassList.get(8));
                dass.setWorry((Integer)dassList.get(9));
                dass21Map.put((Long)dassList.get(1), dass);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  dass21Map;
    }


    private Map<Long, Demographics> loadDemographicsFromCSV() {
        ICsvListReader listReader = null;
        Map<Long, Demographics> demographicsMap = new HashMap<>();
        try {
            Resource resource = new ClassPathResource("demographics.csv");
            InputStream resourceInputStream = resource.getInputStream();
            listReader = new CsvListReader(new InputStreamReader(resourceInputStream), CsvPreference.STANDARD_PREFERENCE);
            listReader.getHeader(true); // skip the header (can't be used with CsvListReader)

            final CellProcessor[] processors = new CellProcessor[]{
                    new ParseLong(), // participant id
                    new NotNull() // gender
            };

            while ((listReader.read()) != null) {
                // use different processors depending on the number of columns
                final List<Object> list = listReader.executeProcessors(processors);
                Demographics d = new Demographics();
                d.setGender((String)list.get(1));
                demographicsMap.put((Long)list.get(0), d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return demographicsMap;
    }

}
