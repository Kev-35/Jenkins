package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class JenkinsReportsWithParamsTests {

    @BeforeEach
    void setEnvironment(){
        Configuration.baseUrl = System.getProperty("baseUrl","https://demoqa.com");
        Configuration.browser = System.getProperty("browser","chrome");
        Configuration.browserVersion = System.getProperty("browserVersion","128.0");
        Configuration.browserSize = System.getProperty("browserSize","1920x1080");
        Configuration.remote = System.getProperty("remoteSelenoidUrl", "https://user1:1234@selenoid.autotests.cloud/wd/hub");
        Configuration.timeout = 10000;

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.of(
                "enableVNC", true,
                "enableVideo", true));

        Configuration.browserCapabilities = capabilities;

        SelenideLogger.addListener("allure",new AllureSelenide());
    }
    @AfterEach
    void addAttachments() {

        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();

        Selenide.closeWebDriver();
    }

    @Test
    @Tag("buildJenkinsWithParams")
    void successFillingFormTest() {
        step("Открываем страницу регистрации пользователя",() -> {
            open("/automation-practice-form");
            executeJavaScript("$('footer').remove();");
            executeJavaScript("$('#fixedban').remove();");
        });
        step("Вводим данные пользователя (имя, фамилия, майл, пол, нмт)", () ->{
            $("#firstName").setValue("Don");
            $("#lastName").setValue("Carlione");
            $("#userEmail").setValue("Carl@mail.ru");
            $("#genterWrapper").$(byText("Male")).click();
            $("#userNumber").setValue("9001112222");
        });
        step("Выбираем дату рождения пользователя", () ->{
            $("#dateOfBirthInput").click();
            $(".react-datepicker__month-select").selectOption("July");
            $(".react-datepicker__year-select").selectOption("2008");
            $(".react-datepicker__day--030:not(.react-datepicker__day--outside-month)").click();
        });
        step("Выбираем предметы и хобби", () ->{
            $("#subjectsInput").setValue("Maths").pressEnter();
            $("#hobbiesWrapper").$(byText("Sports")).click();
        });
        step("Загружаем картинку", () ->{
            $("#uploadPicture").uploadFromClasspath("1.png");
        });
        step("Вводим текущий адрес", () ->{
            $("#currentAddress").setValue("Lesovskay 3");
        });
        step("Выбираем штат и город", () ->{
            $("#state").click();
            $("#react-select-3-input").setValue("NCR").pressEnter();
            $("#city").click();
            $("#react-select-4-input").setValue("Noida").pressEnter();
        });
        step("Нажимаем подтвердить", () -> $("#submit").click());
        step("Проверка данных в таблице регистрации", () ->{
            $(".modal-dialog").should(appear);
            $("#example-modal-sizes-title-lg").shouldHave(text("Thanks for submitting the form"));
            $(".table-responsive").shouldHave(
                    text("Don Carlione"),
                    text("Carl@mail.ru"),
                    text("Male"),
                    text("9001112222"),
                    text("30 July,2008"),
                    text("Maths"),
                    text("Sports"),
                    text("1.png"),
                    text("Lesovskay 3"),
                    text("NCR Noida"));
            $("#closeLargeModal").click();
        });
    }

    @Test
    @Tag("buildJenkinsWithParams")
    void FildLastNameIsNotInTest() {
        step("Открываем страницу регистрации пользователя",() -> {
            open(baseUrl + "/automation-practice-form");
            executeJavaScript("$('footer').remove();");
            executeJavaScript("$('#fixedban').remove();");
        });
        step("Вводим данные пользователя (имя, майл, пол, нмт)", () ->{
            $("#firstName").setValue("Don");
            $("#lastName").setValue("");
            $("#genterWrapper").find(byText("Male")).click();
            $("#userNumber").setValue("9001112222");
        });
        step("Нажимаем подтвердить", () -> $("#submit").click());
        step("Проверяем, что таблица с регистрации отсутствует", () ->{
            $(".modal-content").shouldNot();
        });
    }
}
