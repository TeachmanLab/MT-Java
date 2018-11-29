package org.mindtrails.domain;

import org.junit.Assert;
import org.junit.Test;
import org.mindtrails.domain.Conditions.RandomCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 */
public class RandomBlockTest {


    @Test
    public void testRandomBlockGenerationProducesExactCounts() {
        Map<String, Float> valuePercentages = new HashMap<>();
        valuePercentages.put("control", 50.0f);
        valuePercentages.put("mobile", 25.0f);
        valuePercentages.put("phone", 25.0f);

        List<RandomCondition> blocks = RandomCondition.createBlocks(valuePercentages, 20, "male_low");

        int cc = 0;
        int mc = 0;
        int pc = 0;

        for(RandomCondition block : blocks) {
            if(block.getValue() == "control") cc++;
            if(block.getValue() == "mobile") mc++;
            if(block.getValue() == "phone") pc++;
        }

        Assert.assertEquals(10, cc);
        Assert.assertEquals(5, mc);
        Assert.assertEquals(5, pc);
    }


    /* Hard to test randomness, this is just a sanity check. */
    @Test
    public void testRandomBlockGenerationIsRandomized() {
        Map<String, Float> valuePercentages = new HashMap<>();
        for(int i = 0; i < 10; i++) {
            valuePercentages.put(i + "", 10.0f);
        }

        List<RandomCondition> blocks = RandomCondition.createBlocks(valuePercentages, 100, "test_test");

        // The first 10 items should not all have the key of "0", if they do, then
        // it is very unlikely the list was randomized.
        int matchCount = 0;
        for(int j = 0; j < 10; j++) {
          if(blocks.get(j).getValue().equals("0")) matchCount ++;
        }
        Assert.assertTrue(matchCount < 10);
    }

    /** Using a large collection of de-identified data, assure that we get even blocks
     * of participants properly segmented.
     */

}