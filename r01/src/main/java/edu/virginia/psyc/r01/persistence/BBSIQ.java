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
        desc.put("party", "You are introduced to someone at a party. The person fails to reply to a question you ask. Why");
        desc.put("heart", "You notice that your heart is beating quickly and pounding. Why?");
        desc.put("confused", "Suddenly, you feel confused and find it hard to think straight. Why?");
        desc.put("urgent", "A letter marked \"URGENT\" arrives. What is in the letter?");
        desc.put("dizzy", "You notice that your heart is pounding, and you feel breathless, dizzy and unreal. Why?");
        return Collections.unmodifiableMap(desc);
    }


    @NotNull
    @MeasureField(desc = "They did not wish to outstay their welcome.", group="visitors")
    private Integer visitors_outstay;
    @NotNull
    @MeasureField(desc="They had another pressing engagement to go to.", group="visitors")
    private Integer visitors_engagement;
    @NotNull()
    @MeasureField(desc="They did not enjoy the visit and were bored with your company.", group="visitors")
    private Integer visitors_bored;

    @NotNull()
    @MeasureField(desc="You are developing the flu.", group="breath")
    private Integer breath_flu;
    @NotNull()
    @MeasureField(desc="You are about to suffocate or stop breathing.", group="breath")
    private Integer breath_suffocate;
    @NotNull()
    @MeasureField(desc="You are physically “out of shape”.", group="breath")
    private Integer breath_physically;

    @NotNull()
    @MeasureField(desc="You have strained your eyes slightly.", group="vision")
    private Integer vision_strained;
    @NotNull()
    @MeasureField(desc="You need to get glasses or change your existing glasses.", group="vision")
    private Integer vision_glasses;
    @NotNull()
    @MeasureField(desc="This is the sign of a serious illness.", group="vision")
    private Integer vision_illness;

    @NotNull()
    @MeasureField(desc="They are bored with their job, and this makes them rude.", group="shop")
    private Integer shop_bored;
    @NotNull()
    @MeasureField(desc="They are concentrating very hard on something else.", group="shop")
    private Integer shop_concentrating;
    @NotNull()
    @MeasureField(desc="They find you irritating and resent your presence.", group="shop")
    private Integer shop_irritating;

    @NotNull()
    @MeasureField(desc="You are about to faint.", group="lightheaded")
    private Integer lightheaded_faint;
    @NotNull()
    @MeasureField(desc="You need to get something to eat.", group="lightheaded")
    private Integer lightheaded_eat;
    @NotNull()
    @MeasureField(desc="You didn’t get enough sleep last night.", group="lightheaded")
    private Integer lightheaded_sleep;

    @NotNull()
    @MeasureField(desc="Your house is on fire.", group="smoke")
    private Integer smoke_house;
    @NotNull()
    @MeasureField(desc="Some food is burning.", group="smoke")
    private Integer smoke_food;
    @NotNull()
    @MeasureField(desc="Someone is smoking a cigarette.", group="smoke")
    private Integer smoke_cig;

    @NotNull()
    @MeasureField(desc="They are trying to be helpful.", group="friend")
    private Integer friend_helpful;
    @NotNull()
    @MeasureField(desc="They think you’re incompetent.", group="friend")
    private Integer friend_incompetent;
    @NotNull()
    @MeasureField(desc="They have done the job more often and know an easier way.", group="friend")
    private Integer friend_moreoften;

    @NotNull()
    @MeasureField(desc="You have indigestion.", group="chest")
    private Integer chest_indigestion;
    @NotNull()
    @MeasureField(desc="You have a sore muscle.", group="chest")
    private Integer chest_sore;
    @NotNull()
    @MeasureField(desc="Something is wrong with your heart.", group="chest")
    private Integer chest_heart;

    @NotNull()
    @MeasureField(desc="You were woken by a dream.", group="jolt")
    private Integer jolt_dream;
    @NotNull()
    @MeasureField(desc="A burglar broke into your house.", group="jolt")
    private Integer jolt_burglar;
    @NotNull()
    @MeasureField(desc="A door or window rattled in the wind.", group="jolt")
    private Integer jolt_wind;

    @NotNull()
    @MeasureField(desc="They did not hear the question.", group="party")
    private Integer party_hear;
    @NotNull()
    @MeasureField(desc="They think you are uninteresting and boring.", group="party")
    private Integer party_boring;
    @NotNull()
    @MeasureField(desc="They were preoccupied with something else at the time.", group="party")
    private Integer party_preoccupied;

    @NotNull()
    @MeasureField(desc="Because you have been physically active.", group="heart")
    private Integer heart_active;
    @NotNull()
    @MeasureField(desc="Because there is something wrong with your heart.", group="heart")
    private Integer heart_wrong;
    @NotNull()
    @MeasureField(desc="Because you are feeling excited.", group="heart")
    private Integer heart_excited;

    @NotNull()
    @MeasureField(desc="You are going out of your mind.", group="confused")
    private Integer confused_outofmind;
    @NotNull()
    @MeasureField(desc="You are coming down with a cold.", group="confused")
    private Integer confused_cold;
    @NotNull()
    @MeasureField(desc="You’ve been working too hard and need a rest.", group="confused")
    private Integer confused_work;

    @NotNull()
    @MeasureField(desc="It is junk mail designed to attract your attention.", group="urgent")
    private Integer urgent_junk;
    @NotNull()
    @MeasureField(desc="You forgot to pay a bill.", group="urgent")
    private Integer urgent_bill;
    @NotNull()
    @MeasureField(desc="It is news that someone you know has died or is seriously ill.", group="urgent")
    private Integer urgent_died;

    @NotNull()
    @MeasureField(desc="You have been overdoing it and are overtired.", group="dizzy")
    private Integer dizzy_overtired;
    @NotNull()
    @MeasureField(desc="Something you ate disagreed with you.", group="dizzy")
    private Integer dizzy_ate;
    @NotNull()
    @MeasureField(desc="You are dangerously ill or going mad.", group="dizzy")
    private Integer dizzy_ill;

}