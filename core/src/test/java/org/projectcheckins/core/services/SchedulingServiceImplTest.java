package org.projectcheckins.core.services;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.projectcheckins.core.forms.HowOften.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

import static java.time.DayOfWeek.*;
import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(startApplication = false)
class SchedulingServiceImplTest {

    private static final Set<DayOfWeek>
            MON = Set.of(MONDAY),
            TUE = Set.of(TUESDAY),
            WED = Set.of(WEDNESDAY),
            THU = Set.of(THURSDAY),
            FRI = Set.of(FRIDAY),
            SAT = Set.of(SATURDAY),
            THU_FRI = Set.of(THURSDAY, FRIDAY),
            SAT_SUN = Set.of(SATURDAY, SUNDAY),
            MON_TUE_WED_THU_FRI = Set.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);

    private static final ZoneId
            ZONE_ID = systemDefault();
    private static final LocalTime
            TIME = LocalTime.of(16, 30);

    private static final ZonedDateTime
            JANUARY_1_MON = ZonedDateTime.of(2018, 1, 1, TIME.getHour(), TIME.getMinute(), TIME.getSecond(), TIME.getNano(), ZONE_ID),
            JANUARY_2_TUE = JANUARY_1_MON.plusDays(1),
            JANUARY_3_WED = JANUARY_1_MON.plusDays(2),
            JANUARY_4_THU = JANUARY_1_MON.plusDays(3),
            JANUARY_5_FRI = JANUARY_1_MON.plusDays(4),
            JANUARY_6_SAT = JANUARY_1_MON.plusDays(5),
            JANUARY_7_SUN = JANUARY_1_MON.plusDays(6),
            JANUARY_8_MON = JANUARY_1_MON.plusDays(7),
            JANUARY_9_TUE = JANUARY_1_MON.plusDays(8),
            JANUARY_10_WED = JANUARY_1_MON.plusDays(9),
            JANUARY_11_THU = JANUARY_1_MON.plusDays(10),
            JANUARY_12_FRI = JANUARY_1_MON.plusDays(11),
            JANUARY_13_SAT = JANUARY_1_MON.plusDays(12),
            JANUARY_14_SUN = JANUARY_1_MON.plusDays(13),
            JANUARY_15_MON = JANUARY_1_MON.plusDays(14),
            JANUARY_16_TUE = JANUARY_1_MON.plusDays(15),
            JANUARY_17_WED = JANUARY_1_MON.plusDays(16),
            JANUARY_18_THU = JANUARY_1_MON.plusDays(17),
            JANUARY_19_FRI = JANUARY_1_MON.plusDays(18),
            JANUARY_20_SAT = JANUARY_1_MON.plusDays(19),
            JANUARY_21_SUN = JANUARY_1_MON.plusDays(20),
            JANUARY_22_MON = JANUARY_1_MON.plusDays(21),
            JANUARY_23_TUE = JANUARY_1_MON.plusDays(22),
            JANUARY_24_WED = JANUARY_1_MON.plusDays(23),
            JANUARY_25_THU = JANUARY_1_MON.plusDays(24),
            JANUARY_26_FRI = JANUARY_1_MON.plusDays(25),
            JANUARY_27_SAT = JANUARY_1_MON.plusDays(26),
            JANUARY_28_SUN = JANUARY_1_MON.plusDays(27),
            JANUARY_29_MON = JANUARY_1_MON.plusDays(28),
            JANUARY_30_TUE = JANUARY_1_MON.plusDays(29),
            JANUARY_31_WED = JANUARY_1_MON.plusDays(30),
            FEBRUARY_1_THU = JANUARY_1_MON.plusMonths(1),
            FEBRUARY_2_FRI = FEBRUARY_1_THU.plusDays(1),
            FEBRUARY_5_MON = FEBRUARY_1_THU.plusDays(4),
            FEBRUARY_6_TUE = FEBRUARY_1_THU.plusDays(5),
            FEBRUARY_12_MON = FEBRUARY_1_THU.plusDays(11),
            MARCH_1_THU = JANUARY_1_MON.plusMonths(2),
            MARCH_2_FRI = MARCH_1_THU.plusDays(1),
            MARCH_3_SAT = MARCH_1_THU.plusDays(2),
            MARCH_4_SUN = MARCH_1_THU.plusDays(3),
            MARCH_5_MON = MARCH_1_THU.plusDays(4);

    @Inject
    SchedulingServiceImpl scheduling;

    @Test
    void checkDayOfWeek() {
        assertThat(JANUARY_1_MON.getDayOfWeek()).isEqualTo(MONDAY);
        assertThat(JANUARY_2_TUE.getDayOfWeek()).isEqualTo(TUESDAY);
        assertThat(JANUARY_3_WED.getDayOfWeek()).isEqualTo(WEDNESDAY);
        assertThat(JANUARY_4_THU.getDayOfWeek()).isEqualTo(THURSDAY);
        assertThat(JANUARY_5_FRI.getDayOfWeek()).isEqualTo(FRIDAY);
        assertThat(JANUARY_6_SAT.getDayOfWeek()).isEqualTo(SATURDAY);
        assertThat(JANUARY_7_SUN.getDayOfWeek()).isEqualTo(SUNDAY);
        assertThat(JANUARY_8_MON.getDayOfWeek()).isEqualTo(MONDAY);
        assertThat(JANUARY_9_TUE.getDayOfWeek()).isEqualTo(TUESDAY);
        assertThat(JANUARY_10_WED.getDayOfWeek()).isEqualTo(WEDNESDAY);
        assertThat(JANUARY_11_THU.getDayOfWeek()).isEqualTo(THURSDAY);
        assertThat(JANUARY_12_FRI.getDayOfWeek()).isEqualTo(FRIDAY);
        assertThat(JANUARY_13_SAT.getDayOfWeek()).isEqualTo(SATURDAY);
        assertThat(JANUARY_14_SUN.getDayOfWeek()).isEqualTo(SUNDAY);
        assertThat(JANUARY_15_MON.getDayOfWeek()).isEqualTo(MONDAY);
        assertThat(JANUARY_16_TUE.getDayOfWeek()).isEqualTo(TUESDAY);
        assertThat(JANUARY_17_WED.getDayOfWeek()).isEqualTo(WEDNESDAY);
        assertThat(JANUARY_18_THU.getDayOfWeek()).isEqualTo(THURSDAY);
        assertThat(JANUARY_19_FRI.getDayOfWeek()).isEqualTo(FRIDAY);
        assertThat(JANUARY_22_MON.getDayOfWeek()).isEqualTo(MONDAY);
        assertThat(JANUARY_23_TUE.getDayOfWeek()).isEqualTo(TUESDAY);
        assertThat(JANUARY_24_WED.getDayOfWeek()).isEqualTo(WEDNESDAY);
        assertThat(JANUARY_25_THU.getDayOfWeek()).isEqualTo(THURSDAY);
        assertThat(JANUARY_26_FRI.getDayOfWeek()).isEqualTo(FRIDAY);
        assertThat(JANUARY_27_SAT.getDayOfWeek()).isEqualTo(SATURDAY);
        assertThat(JANUARY_28_SUN.getDayOfWeek()).isEqualTo(SUNDAY);
        assertThat(JANUARY_29_MON.getDayOfWeek()).isEqualTo(MONDAY);
        assertThat(JANUARY_30_TUE.getDayOfWeek()).isEqualTo(TUESDAY);
        assertThat(JANUARY_31_WED.getDayOfWeek()).isEqualTo(WEDNESDAY);
        assertThat(FEBRUARY_1_THU.getDayOfWeek()).isEqualTo(THURSDAY);
        assertThat(FEBRUARY_2_FRI.getDayOfWeek()).isEqualTo(FRIDAY);
        assertThat(FEBRUARY_5_MON.getDayOfWeek()).isEqualTo(MONDAY);
        assertThat(FEBRUARY_6_TUE.getDayOfWeek()).isEqualTo(TUESDAY);
        assertThat(FEBRUARY_12_MON.getDayOfWeek()).isEqualTo(MONDAY);
        assertThat(MARCH_1_THU.getDayOfWeek()).isEqualTo(THURSDAY);
        assertThat(MARCH_2_FRI.getDayOfWeek()).isEqualTo(FRIDAY);
        assertThat(MARCH_3_SAT.getDayOfWeek()).isEqualTo(SATURDAY);
        assertThat(MARCH_4_SUN.getDayOfWeek()).isEqualTo(SUNDAY);
        assertThat(MARCH_5_MON.getDayOfWeek()).isEqualTo(MONDAY);
    }

    @Test
    void calculateFirstDaily() {
        assertThat(scheduling.calculateFirst(JANUARY_1_MON, DAILY_ON, THU_FRI)).isEqualTo(JANUARY_4_THU);
        assertThat(scheduling.calculateFirst(JANUARY_1_MON, DAILY_ON, MON)).isEqualTo(JANUARY_1_MON);
        assertThat(scheduling.calculateFirst(JANUARY_2_TUE, DAILY_ON, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateFirst(JANUARY_3_WED, DAILY_ON, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateFirst(JANUARY_4_THU, DAILY_ON, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateFirst(JANUARY_5_FRI, DAILY_ON, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateFirst(JANUARY_6_SAT, DAILY_ON, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateFirst(JANUARY_7_SUN, DAILY_ON, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateFirst(JANUARY_8_MON, DAILY_ON, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateFirst(JANUARY_1_MON, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(JANUARY_1_MON);
        assertThat(scheduling.calculateFirst(JANUARY_1_MON, DAILY_ON, THU)).isEqualTo(JANUARY_4_THU);
        assertThat(scheduling.calculateFirst(JANUARY_4_THU, DAILY_ON, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateFirst(JANUARY_13_SAT, DAILY_ON, MON)).isEqualTo(JANUARY_15_MON);
        assertThat(scheduling.calculateFirst(JANUARY_14_SUN, DAILY_ON, MON)).isEqualTo(JANUARY_15_MON);
        assertThat(scheduling.calculateFirst(JANUARY_15_MON, DAILY_ON, MON)).isEqualTo(JANUARY_15_MON);
        assertThat(scheduling.calculateFirst(JANUARY_15_MON, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(JANUARY_15_MON);
        assertThat(scheduling.calculateFirst(JANUARY_24_WED, DAILY_ON, SAT_SUN)).isEqualTo(JANUARY_27_SAT);
        assertThat(scheduling.calculateFirst(JANUARY_28_SUN, DAILY_ON, THU)).isEqualTo(FEBRUARY_1_THU);
    }

    @Test
    void calculateFirstWeekly() {
        assertThat(scheduling.calculateFirst(JANUARY_1_MON, ONCE_A_WEEK, MON)).isEqualTo(JANUARY_1_MON);
        assertThat(scheduling.calculateFirst(JANUARY_1_MON, ONCE_A_WEEK, THU)).isEqualTo(JANUARY_4_THU);
        assertThat(scheduling.calculateFirst(JANUARY_2_TUE, ONCE_A_WEEK, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateFirst(JANUARY_4_THU, ONCE_A_WEEK, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateFirst(JANUARY_1_MON, ONCE_A_WEEK, THU)).isEqualTo(JANUARY_4_THU);
        assertThat(scheduling.calculateFirst(JANUARY_15_MON, ONCE_A_WEEK, MON)).isEqualTo(JANUARY_15_MON);
        assertThat(scheduling.calculateFirst(JANUARY_29_MON, ONCE_A_WEEK, WED)).isEqualTo(JANUARY_31_WED);
        assertThat(scheduling.calculateFirst(JANUARY_29_MON, ONCE_A_WEEK, THU)).isEqualTo(FEBRUARY_1_THU);
    }

    @Test
    void calculateFirstEveryOtherWeek() {
        assertThat(scheduling.calculateFirst(JANUARY_1_MON, EVERY_OTHER_WEEK, MON)).isEqualTo(JANUARY_1_MON);
        assertThat(scheduling.calculateFirst(JANUARY_1_MON, EVERY_OTHER_WEEK, THU)).isEqualTo(JANUARY_4_THU);
        assertThat(scheduling.calculateFirst(JANUARY_2_TUE, EVERY_OTHER_WEEK, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateFirst(JANUARY_4_THU, EVERY_OTHER_WEEK, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateFirst(JANUARY_1_MON, EVERY_OTHER_WEEK, THU)).isEqualTo(JANUARY_4_THU);
        assertThat(scheduling.calculateFirst(JANUARY_15_MON, EVERY_OTHER_WEEK, MON)).isEqualTo(JANUARY_15_MON);
        assertThat(scheduling.calculateFirst(JANUARY_29_MON, EVERY_OTHER_WEEK, WED)).isEqualTo(JANUARY_31_WED);
        assertThat(scheduling.calculateFirst(JANUARY_29_MON, EVERY_OTHER_WEEK, THU)).isEqualTo(FEBRUARY_1_THU);
    }

    @Test
    void calculateFirstMonthly() {
        assertThat(scheduling.calculateFirst(JANUARY_1_MON, ONCE_A_MONTH_ON_THE_FIRST, MON)).isEqualTo(JANUARY_1_MON);
        assertThat(scheduling.calculateFirst(JANUARY_4_THU, ONCE_A_MONTH_ON_THE_FIRST, MON)).isEqualTo(FEBRUARY_5_MON);
        assertThat(scheduling.calculateFirst(JANUARY_15_MON, ONCE_A_MONTH_ON_THE_FIRST, MON)).isEqualTo(FEBRUARY_5_MON);
        assertThat(scheduling.calculateFirst(FEBRUARY_6_TUE, ONCE_A_MONTH_ON_THE_FIRST, MON)).isEqualTo(MARCH_5_MON);
        assertThat(scheduling.calculateFirst(JANUARY_1_MON, ONCE_A_MONTH_ON_THE_FIRST, THU)).isEqualTo(JANUARY_4_THU);
        assertThat(scheduling.calculateFirst(JANUARY_4_THU, ONCE_A_MONTH_ON_THE_FIRST, THU)).isEqualTo(JANUARY_4_THU);
        assertThat(scheduling.calculateFirst(JANUARY_15_MON, ONCE_A_MONTH_ON_THE_FIRST, THU)).isEqualTo(FEBRUARY_1_THU);
        assertThat(scheduling.calculateFirst(FEBRUARY_1_THU, ONCE_A_MONTH_ON_THE_FIRST, THU)).isEqualTo(FEBRUARY_1_THU);
        assertThat(scheduling.calculateFirst(FEBRUARY_2_FRI, ONCE_A_MONTH_ON_THE_FIRST, THU)).isEqualTo(MARCH_1_THU);
        assertThat(scheduling.calculateFirst(MARCH_2_FRI, ONCE_A_MONTH_ON_THE_FIRST, MON)).isEqualTo(MARCH_5_MON);
    }

    @Test
    void calculateNextDaily() {
        assertThat(scheduling.calculateNext(JANUARY_1_MON, DAILY_ON, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateNext(JANUARY_8_MON, DAILY_ON, MON)).isEqualTo(JANUARY_15_MON);
        assertThat(scheduling.calculateNext(JANUARY_15_MON, DAILY_ON, MON)).isEqualTo(JANUARY_22_MON);
        assertThat(scheduling.calculateNext(JANUARY_22_MON, DAILY_ON, MON)).isEqualTo(JANUARY_29_MON);
        assertThat(scheduling.calculateNext(JANUARY_29_MON, DAILY_ON, MON)).isEqualTo(FEBRUARY_5_MON);
        assertThat(scheduling.calculateNext(JANUARY_4_THU, DAILY_ON, THU_FRI)).isEqualTo(JANUARY_5_FRI);
        assertThat(scheduling.calculateNext(JANUARY_5_FRI, DAILY_ON, THU_FRI)).isEqualTo(JANUARY_11_THU);
        assertThat(scheduling.calculateNext(JANUARY_11_THU, DAILY_ON, THU_FRI)).isEqualTo(JANUARY_12_FRI);
        assertThat(scheduling.calculateNext(JANUARY_12_FRI, DAILY_ON, THU_FRI)).isEqualTo(JANUARY_18_THU);
        assertThat(scheduling.calculateNext(JANUARY_14_SUN, DAILY_ON, SAT_SUN)).isEqualTo(JANUARY_20_SAT);
        assertThat(scheduling.calculateNext(JANUARY_18_THU, DAILY_ON, THU_FRI)).isEqualTo(JANUARY_19_FRI);
        assertThat(scheduling.calculateNext(JANUARY_19_FRI, DAILY_ON, THU_FRI)).isEqualTo(JANUARY_25_THU);
        assertThat(scheduling.calculateNext(JANUARY_25_THU, DAILY_ON, THU_FRI)).isEqualTo(JANUARY_26_FRI);
        assertThat(scheduling.calculateNext(JANUARY_26_FRI, DAILY_ON, THU_FRI)).isEqualTo(FEBRUARY_1_THU);
        assertThat(scheduling.calculateNext(JANUARY_21_SUN, DAILY_ON, SAT_SUN)).isEqualTo(JANUARY_27_SAT);
        assertThat(scheduling.calculateNext(FEBRUARY_1_THU, DAILY_ON, THU_FRI)).isEqualTo(FEBRUARY_2_FRI);
        assertThat(scheduling.calculateNext(JANUARY_1_MON, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(JANUARY_2_TUE);
        assertThat(scheduling.calculateNext(JANUARY_2_TUE, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(JANUARY_3_WED);
        assertThat(scheduling.calculateNext(JANUARY_3_WED, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(JANUARY_4_THU);
        assertThat(scheduling.calculateNext(JANUARY_4_THU, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(JANUARY_5_FRI);
        assertThat(scheduling.calculateNext(JANUARY_5_FRI, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateNext(JANUARY_12_FRI, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(JANUARY_15_MON);
        assertThat(scheduling.calculateNext(JANUARY_19_FRI, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(JANUARY_22_MON);
        assertThat(scheduling.calculateNext(JANUARY_26_FRI, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(JANUARY_29_MON);
        assertThat(scheduling.calculateNext(JANUARY_29_MON, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(JANUARY_30_TUE);
        assertThat(scheduling.calculateNext(JANUARY_30_TUE, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(JANUARY_31_WED);
        assertThat(scheduling.calculateNext(JANUARY_31_WED, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(FEBRUARY_1_THU);
        assertThat(scheduling.calculateNext(FEBRUARY_1_THU, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(FEBRUARY_2_FRI);
        assertThat(scheduling.calculateNext(FEBRUARY_2_FRI, DAILY_ON, MON_TUE_WED_THU_FRI)).isEqualTo(FEBRUARY_5_MON);
        assertThat(scheduling.calculateNext(MARCH_3_SAT, DAILY_ON, SAT_SUN)).isEqualTo(MARCH_4_SUN);
    }

    @Test
    void calculateNextWeekly() {
        assertThat(scheduling.calculateNext(JANUARY_1_MON, ONCE_A_WEEK, MON)).isEqualTo(JANUARY_8_MON);
        assertThat(scheduling.calculateNext(JANUARY_2_TUE, ONCE_A_WEEK, TUE)).isEqualTo(JANUARY_9_TUE);
        assertThat(scheduling.calculateNext(JANUARY_3_WED, ONCE_A_WEEK, WED)).isEqualTo(JANUARY_10_WED);
        assertThat(scheduling.calculateNext(JANUARY_5_FRI, ONCE_A_WEEK, FRI)).isEqualTo(JANUARY_12_FRI);
        assertThat(scheduling.calculateNext(JANUARY_8_MON, ONCE_A_WEEK, MON)).isEqualTo(JANUARY_15_MON);
        assertThat(scheduling.calculateNext(JANUARY_15_MON, ONCE_A_WEEK, MON)).isEqualTo(JANUARY_22_MON);
        assertThat(scheduling.calculateNext(JANUARY_16_TUE, ONCE_A_WEEK, TUE)).isEqualTo(JANUARY_23_TUE);
        assertThat(scheduling.calculateNext(JANUARY_22_MON, ONCE_A_WEEK, MON)).isEqualTo(JANUARY_29_MON);
        assertThat(scheduling.calculateNext(JANUARY_29_MON, ONCE_A_WEEK, MON)).isEqualTo(FEBRUARY_5_MON);
        assertThat(scheduling.calculateNext(FEBRUARY_5_MON, ONCE_A_WEEK, MON)).isEqualTo(FEBRUARY_12_MON);
    }

    @Test
    void calculateNextEveryOtherWeek() {
        assertThat(scheduling.calculateNext(JANUARY_1_MON, EVERY_OTHER_WEEK, MON)).isEqualTo(JANUARY_15_MON);
        assertThat(scheduling.calculateNext(JANUARY_3_WED, EVERY_OTHER_WEEK, WED)).isEqualTo(JANUARY_17_WED);
        assertThat(scheduling.calculateNext(JANUARY_5_FRI, EVERY_OTHER_WEEK, FRI)).isEqualTo(JANUARY_19_FRI);
        assertThat(scheduling.calculateNext(JANUARY_8_MON, EVERY_OTHER_WEEK, MON)).isEqualTo(JANUARY_22_MON);
        assertThat(scheduling.calculateNext(JANUARY_15_MON, EVERY_OTHER_WEEK, MON)).isEqualTo(JANUARY_29_MON);
        assertThat(scheduling.calculateNext(JANUARY_22_MON, EVERY_OTHER_WEEK, MON)).isEqualTo(FEBRUARY_5_MON);
        assertThat(scheduling.calculateNext(JANUARY_29_MON, EVERY_OTHER_WEEK, MON)).isEqualTo(FEBRUARY_12_MON);
    }

    @Test
    void calculateNextMonthly() {
        assertThat(scheduling.calculateNext(JANUARY_1_MON, ONCE_A_MONTH_ON_THE_FIRST, MON)).isEqualTo(FEBRUARY_5_MON);
        assertThat(scheduling.calculateNext(JANUARY_4_THU, ONCE_A_MONTH_ON_THE_FIRST, THU)).isEqualTo(FEBRUARY_1_THU);
        assertThat(scheduling.calculateNext(FEBRUARY_5_MON, ONCE_A_MONTH_ON_THE_FIRST, MON)).isEqualTo(MARCH_5_MON);
    }

    @Test
    void nextMonthly() {
        assertThat(scheduling.nextExecution(JANUARY_1_MON, ONCE_A_MONTH_ON_THE_FIRST, MON, ZONE_ID, TIME)).isEqualTo(FEBRUARY_5_MON);
        assertThat(scheduling.nextExecution(JANUARY_4_THU, ONCE_A_MONTH_ON_THE_FIRST, THU, ZONE_ID, TIME)).isEqualTo(FEBRUARY_1_THU);
        assertThat(scheduling.nextExecution(JANUARY_15_MON, ONCE_A_MONTH_ON_THE_FIRST, MON, ZONE_ID, TIME).getYear()).isGreaterThan(JANUARY_15_MON.getYear());
    }
}
