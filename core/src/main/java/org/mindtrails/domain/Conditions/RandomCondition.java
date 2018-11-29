package org.mindtrails.domain.Conditions;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Entity
@Table(name="random_condition")
@Data
/**
 * NIH published an excellent, simple article (https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3942596/
 * around the proper assignment of participants to randomized trials that addresses stratification.
 *
 * We will use a stratified block randomization process.  For each segmentation, (women, high stress for example)
 * we pre-generate a list of 100 assignments, containing exactly 50 control and 50 training.   We then randomize
 * this list.   As new participants come into the study that meet this segmentation we pull an item off of the list,
 * and use it to assign the participants.  After 100 participants in this segment come through,
 * we generate another list of 100 assignments and randomize the order of those.
 * (I pulled 100 out of a hat, this could be any statistically significant amount).  In this way we can assure
 * that the assignments are fully random, and that we get a balanced distribution across each stratification.
 */
public class RandomCondition {

    @Id
    @GeneratedValue
    private long id;
    private String segmentName;
    private String value;

    public RandomCondition() {}

    public RandomCondition(String value, String segmentName) {
        this.value = value;
        this.segmentName = segmentName;
    }

    /**
     * Generates [count] number of RandomBlock instances, where the segments occur the percentage number of times,
     * the order of the resulting list is randomized. It is possible we will be one less than the requested count,
     * but this doesn't seem to impact the quality of the randomization, so I'm not adding a bunch of complex code
     * to account for it.
     */
    public static List<RandomCondition> createBlocks(Map<String, Float> valuePercentage, int count, String segmentName) {
        List<RandomCondition> blocks = new ArrayList<>();

        for (Map.Entry<String, Float> entry : valuePercentage.entrySet()) {
            // This will round down, which may mean extra items at the end.
            int total = (int) (count * (entry.getValue() / 100));

            for(int i = 0; i<total; i++) {
                blocks.add(new RandomCondition(entry.getKey(), segmentName));
            }
        }

        Collections.shuffle(blocks);
        return blocks;
    }

}
