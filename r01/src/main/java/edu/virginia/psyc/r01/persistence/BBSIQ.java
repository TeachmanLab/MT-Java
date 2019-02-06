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
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name = "BBSIQ")
@EqualsAndHashCode(callSuper = true)
@Data
public class BBSIQ extends LinkedQuestionnaireData {

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(0, "Not at all likely");
        tmpScale.put(1, "A little likely");
        tmpScale.put(2, "Moderately likely");
        tmpScale.put(3, "Very likely");
        tmpScale.put(4, "Extremely likely");
        return Collections.unmodifiableMap(tmpScale);
    }

    @Override
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        desc.put("visitors", "You have visitors over for dinner and they leave sooner than you expected. Why?");
        desc.put("breath", "You feel short of breath. Why?");
        desc.put("vision", " Your vision has become slightly blurred. Why?");
        desc.put("shop", "You go into a shop and the assistant ignores you. Why?");
        desc.put("lightheaded", "You feel lightheaded and weak. Why?");
        desc.put("smoke", "You smell smoke. What’s burning?");
        desc.put("friend", "A friend suggests that you change the way that you’re doing a job in your own house. Why? ");
        desc.put("chest", "Your chest feels uncomfortable and tight. Why?");
        desc.put("jolt", "You wake with a jolt in the middle of the night, thinking you heard a noise, but all is quiet. What woke you up?");
        desc.put("party", "You are introduced to someone at a party. The person fails to reply to a question you ask. Why?");
        desc.put("heart", "You notice that your heart is beating quickly and pounding. Why?");
        desc.put("confused", "Suddenly, you feel confused and find it hard to think straight. Why?");
        desc.put("urgent", "A letter marked \"URGENT\" arrives. What is in the letter?");
        desc.put("dizzy", "You notice that your heart is pounding, and you feel breathless, dizzy, and unreal. Why?");
        return Collections.unmodifiableMap(desc);
    }


    @NotNull
    @MeasureField(order=1, desc="They did not wish to outstay their welcome.", group="visitors")
    private Integer visitors_outstay;
    @NotNull
    @MeasureField(order=2, desc="They had another pressing engagement to go to.", group="visitors")
    private Integer visitors_engagement;
    @NotNull()
    @MeasureField(order=3, desc="They did not enjoy the visit and were bored with your company.", group="visitors")
    private Integer visitors_bored;

    @NotNull()
    @MeasureField(order=4, desc="You are developing the flu.", group="breath")
    private Integer breath_flu;
    @NotNull()
    @MeasureField(order=5, desc="You are about to suffocate or stop breathing.", group="breath")
    private Integer breath_suffocate;
    @NotNull()
    @MeasureField(order=6, desc="You are physically “out of shape.”", group="breath")
    private Integer breath_physically;

    @NotNull()
    @MeasureField(order=7, desc="You have strained your eyes slightly.", group="vision")
    private Integer vision_strained;
    @NotNull()
    @MeasureField(order=8, desc="You need to get glasses or change your existing glasses.", group="vision")
    private Integer vision_glasses;
    @NotNull()
    @MeasureField(order=9, desc="This is the sign of a serious illness.", group="vision")
    private Integer vision_illness;

    @NotNull()
    @MeasureField(order=10, desc="They are bored with their job, and this makes them rude.", group="shop")
    private Integer shop_bored;
    @NotNull()
    @MeasureField(order=11, desc="They are concentrating very hard on something else.", group="shop")
    private Integer shop_concentrating;
    @NotNull()
    @MeasureField(order=12, desc="They find you irritating and resent your presence.", group="shop")
    private Integer shop_irritating;

    @NotNull()
    @MeasureField(order=13, desc="You are about to faint.", group="lightheaded")
    private Integer lightheaded_faint;
    @NotNull()
    @MeasureField(order=14, desc="You need to get something to eat.", group="lightheaded")
    private Integer lightheaded_eat;
    @NotNull()
    @MeasureField(order=15, desc="You didn’t get enough sleep last night.", group="lightheaded")
    private Integer lightheaded_sleep;

    @NotNull()
    @MeasureField(order=16, desc="Your house is on fire.", group="smoke")
    private Integer smoke_house;
    @NotNull()
    @MeasureField(order=17, desc="Some food is burning.", group="smoke")
    private Integer smoke_food;
    @NotNull()
    @MeasureField(order=18, desc="Someone is smoking a cigarette.", group="smoke")
    private Integer smoke_cig;

    @NotNull()
    @MeasureField(order=19, desc="They are trying to be helpful.", group="friend")
    private Integer friend_helpful;
    @NotNull()
    @MeasureField(order=20, desc="They think you’re incompetent.", group="friend")
    private Integer friend_incompetent;
    @NotNull()
    @MeasureField(order=21, desc="They have done the job more often and know an easier way.", group="friend")
    private Integer friend_moreoften;

    @NotNull()
    @MeasureField(order=22, desc="You have indigestion.", group="chest")
    private Integer chest_indigestion;
    @NotNull()
    @MeasureField(order=23, desc="You have a sore muscle.", group="chest")
    private Integer chest_sore;
    @NotNull()
    @MeasureField(order=24, desc="Something is wrong with your heart.", group="chest")
    private Integer chest_heart;

    @NotNull()
    @MeasureField(order=25, desc="You were woken by a dream.", group="jolt")
    private Integer jolt_dream;
    @NotNull()
    @MeasureField(order=26, desc="A burglar broke into your house.", group="jolt")
    private Integer jolt_burglar;
    @NotNull()
    @MeasureField(order=27, desc="A door or window rattled in the wind.", group="jolt")
    private Integer jolt_wind;

    @NotNull()
    @MeasureField(order=28, desc="They did not hear the question.", group="party")
    private Integer party_hear;
    @NotNull()
    @MeasureField(order=29, desc="They think you are uninteresting and boring.", group="party")
    private Integer party_boring;
    @NotNull()
    @MeasureField(order=30, desc="They were preoccupied with something else at the time.", group="party")
    private Integer party_preoccupied;

    @NotNull()
    @MeasureField(order=31, desc="Because you have been physically active.", group="heart")
    private Integer heart_active;
    @NotNull()
    @MeasureField(order=32, desc="Because there is something wrong with your heart.", group="heart")
    private Integer heart_wrong;
    @NotNull()
    @MeasureField(order=33, desc="Because you are feeling excited.", group="heart")
    private Integer heart_excited;

    @NotNull()
    @MeasureField(order=34, desc="You are going out of your mind.", group="confused")
    private Integer confused_outofmind;
    @NotNull()
    @MeasureField(order=35, desc="You are coming down with a cold.", group="confused")
    private Integer confused_cold;
    @NotNull()
    @MeasureField(order=36, desc="You’ve been working too hard and need a rest.", group="confused")
    private Integer confused_work;

    @NotNull()
    @MeasureField(order=37, desc="It is junk mail designed to attract your attention.", group="urgent")
    private Integer urgent_junk;
    @NotNull()
    @MeasureField(order=38, desc="You forgot to pay a bill.", group="urgent")
    private Integer urgent_bill;
    @NotNull()
    @MeasureField(order=39, desc="It is news that someone you know has died or is seriously ill.", group="urgent")
    private Integer urgent_died;

    @NotNull()
    @MeasureField(order=40, desc="You have been overdoing it and are overtired.", group="dizzy")
    private Integer dizzy_overtired;
    @NotNull()
    @MeasureField(order=41, desc="Something you ate disagreed with you.", group="dizzy")
    private Integer dizzy_ate;
    @NotNull()
    @MeasureField(order=42, desc="You are dangerously ill or going mad.", group="dizzy")
    private Integer dizzy_ill;

}