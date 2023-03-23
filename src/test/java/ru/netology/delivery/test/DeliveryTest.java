package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "800x900";
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan meeting")
    void shouldSuccessfulPlanMeeting(){
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");

//        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $x("//*[contains(text(), 'Успешно!')]").should(Condition.appear);
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + DataGenerator.generateDate(3))).shouldBe(Condition.visible);

    }
    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndRePlanMeeting() {
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 7;
        String secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");

        $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME), Keys.DELETE);
        $("[placeholder='Дата встречи']").setValue(firstMeetingDate);
        $("[name='name']").setValue(validUser.getName());
        $("[name='phone']").setValue(validUser.getPhone());
        $("[class='checkbox__box']").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $x("//*[contains(text(), 'Успешно!')]").should(Condition.appear);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME), Keys.DELETE);
        $("[placeholder='Дата встречи']").setValue(secondMeetingDate);
        $("[class='button__text']").click();
        $("[data-test-id=replan-notification] .notification__content")
                .shouldBe(visible)
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id='replan-notification'] button").click();
        $x("//*[contains(text(), 'Встреча успешно')]").shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(Condition.visible);
    }
}
