package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.MeasureField;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntegeregerelliJ IDEA.
 * User: dan
 * Date: 5/26/14
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="RR")
@EqualsAndHashCode(callSuper = true)
@Data
public class RR extends LinkedQuestionnaireData {

    @NotNull
    @MeasureField(desc="You think that the elevator will probably break down while you are on it.", group="elevator")
    private Integer elevator_NS;
    @NotNull
    @MeasureField(desc="You think that you are going to like your friend’s new apartment.", group="elevator")
    private Integer elevator_PF;
    @NotNull
    @MeasureField(desc="You think about how smelly the lobby is.", group="elevator")
    private Integer elevator_NF;
    @NotNull
    @MeasureField(desc="You think that riding the elevator will be safe.", group="elevator")
    private Integer elevator_PS;

    @NotNull
    @MeasureField(desc="People in the audience laugh in appreciation.", group="wedding")
    private Integer wedding_PS;
    @NotNull
    @MeasureField(desc="You notice a friend you were hoping to see walk into the reception.", group="wedding")
    private Integer wedding_PF;
    @NotNull
    @MeasureField(desc="People in the audience find your efforts laughable.", group="wedding")
    private Integer wedding_NS;
    @NotNull
    @MeasureField(desc="You notice someone you do not like just walked into the reception.", group="wedding")
    private Integer wedding_NF;

    @NotNull
    @MeasureField(desc="And know that you can rely on your savings.", group="job")
    private Integer job_PS;
    @NotNull
    @MeasureField(desc="And are excited about not having to set an alarm.", group="job")
    private Integer job_PF;
    @NotNull
    @MeasureField(desc="And worry about becoming broke.", group="job")
    private Integer job_NS;
    @NotNull
    @MeasureField(desc="And are sad about leaving your current coworkers.", group="job")
    private Integer job_NF;

    @NotNull
    @MeasureField(desc="You feel happy, and think about how lovely your house is.", group="noise")
    private Integer noise_PF;
    @NotNull
    @MeasureField(desc="You feel afraid, and worry that you cannot handle the fear.", group="noise")
    private Integer noise_NS;
    @NotNull
    @MeasureField(desc="You feel afraid, but you know that you can tolerate the feeling.", group="noise")
    private Integer noise_PS;
    @NotNull
    @MeasureField(desc="You feel cold, and think about how the house needs better heating.", group="noise")
    private Integer noise_NF;

    @NotNull
    @MeasureField(desc="Order your favorite snack.", group="friend")
    private Integer meeting_friend_PF;
    @NotNull
    @MeasureField(desc="Notice the bar smells gross.", group="friend")
    private Integer meeting_friend_NF;
    @NotNull
    @MeasureField(desc="Get a call from your friend who is on her way, but running late.", group="friend")
    private Integer meeting_friend_PS;
    @NotNull
    @MeasureField(desc="Think your friend decided she did not want to see you.", group="friend")
    private Integer meeting_friend_NS;

    @NotNull
    @MeasureField(desc="Because she thinks you are a slob.", group="lunch")
    private Integer lunch_NS;
    @NotNull
    @MeasureField(desc="And you frown because you forgot to bring water to lunch.", group="lunch")
    private Integer lunch_NF;
    @NotNull
    @MeasureField(desc="Because she is paying attention as you describe your weekend plans.", group="lunch")
    private Integer lunch_PS;
    @NotNull
    @MeasureField(desc="And you smile because your lunch tastes good.", group="lunch")
    private Integer lunch_PF;

    @NotNull
    @MeasureField(desc="And you think it will probably get seriously infected.", group="scrape")
    private Integer scrape_NS;
    @NotNull
    @MeasureField(desc="And you are frustrated because you tore your shorts.", group="scrape")
    private Integer scrape_NF;
    @NotNull
    @MeasureField(desc="But you know you will be okay.", group="scrape")
    private Integer scrape_PS;
    @NotNull
    @MeasureField(desc="But you are happy that you are getting exercise.", group="scrape")
    private Integer scrape_PF;

    @NotNull
    @MeasureField(desc="And think you are probably coming down with the strange illness.", group="shopping")
    private Integer shopping_NS;
    @NotNull
    @MeasureField(desc="And think you are unlikely to catch the strange illness.", group="shopping")
    private Integer shopping_PS;
    @NotNull
    @MeasureField(desc="And smile because you enjoy shopping.", group="shopping")
    private Integer shopping_PF;
    @NotNull
    @MeasureField(desc="And feel bored of shopping.", group="shopping")
    private Integer shopping_NF;

    @NotNull
    @MeasureField(desc="And you think about how nice your doctor is.", group="blood")
    private Integer blood_test_PF;
    @NotNull
    @MeasureField(desc="And you are annoyed because your doctor is not very friendly.", group="blood")
    private Integer blood_test_NF;
    @NotNull
    @MeasureField(desc="And you think that you will not be able to stand your anxiety while you wait.", group="blood")
    private Integer blood_test_NS;
    @NotNull
    @MeasureField(desc="And you know that you can handle your anxiety while you wait.", group="blood")
    private Integer blood_test_PS;

    @Override
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        desc.put("elevator", "THE ELEVATOR: The building looks old, and as you get on the elevator...");
        desc.put("wedding", "THE WEDDING RECEPTION: As you enter the room...");
        desc.put("job", "THE JOB: You think about not having an income for a few weeks...");
        desc.put("noise", "THE LOUD NOISE: As you walk downstairs...");
        desc.put("friend", "MEETING A FRIEND: You arrive a little late, and...");
        desc.put("lunch", "THE LUNCH: Your friend looks at you...");
        desc.put("scrape", "THE SCRAPE: The scrape hurts a bit...");
        desc.put("shopping", "THE SHOPPING TRIP: You think about your recent health...");
        desc.put("blood", "THE BLOOD TEST: The doctor says he will call you in a few weeks...");
        return Collections.unmodifiableMap(desc);
    }

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        switch (group) {
            case("elevator"):
                tmpScale.put(1, "Differently");
                tmpScale.put(2, "Very differently");
                tmpScale.put(3, "Similar");
                tmpScale.put(4, "Very Similar");
                break;
            case("wedding"):
                tmpScale.put(1, "Differently");
                tmpScale.put(2, "Very differently");
                tmpScale.put(3, "Similar");
                tmpScale.put(4, "Very Similar");
                break;
            case("job"):
                tmpScale.put(1, "Differently");
                tmpScale.put(2, "Very differently");
                tmpScale.put(3, "Similar");
                tmpScale.put(4, "Very Similar");
                break;
            case("noise"):
                tmpScale.put(1, "Differently");
                tmpScale.put(2, "Very differently");
                tmpScale.put(3, "Similar");
                tmpScale.put(4, "Very Similar");
                break;
            case("friend"):
                tmpScale.put(1, "Differently");
                tmpScale.put(2, "Very differently");
                tmpScale.put(3, "Similar");
                tmpScale.put(4, "Very Similar");
                break;
            case("lunch"):
                tmpScale.put(1, "Differently");
                tmpScale.put(2, "Very differently");
                tmpScale.put(3, "Similar");
                tmpScale.put(4, "Very Similar");
                break;
            case("scrape"):
                tmpScale.put(1, "Differently");
                tmpScale.put(2, "Very differently");
                tmpScale.put(3, "Similar");
                tmpScale.put(4, "Very Similar");
                break;
            case("shopping"):
                tmpScale.put(1, "Differently");
                tmpScale.put(2, "Very differently");
                tmpScale.put(3, "Similar");
                tmpScale.put(4, "Very Similar");
                break;
            case("blood"):
                tmpScale.put(1, "Differently");
                tmpScale.put(2, "Very differently");
                tmpScale.put(3, "Similar");
                tmpScale.put(4, "Very Similar");
                break;
        }
        return Collections.unmodifiableMap(tmpScale);
    }
}
