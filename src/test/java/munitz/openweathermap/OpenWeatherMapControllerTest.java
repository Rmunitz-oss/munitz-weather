package munitz.openweathermap;

import io.reactivex.rxjava3.core.Single;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import munitz.openweathermap.OpenWeatherMapForecast.HourlyForecast;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OpenWeatherMapControllerTest {
    private OpenWeatherMapController controller;
    private ChoiceBox<String> degreeUnitChoiceBox;
    private OpenWeatherMapServiceFactory factory;
    private OpenWeatherMapService service;
    private TextField locationTextField;
    private Text currentWeatherText;
    private Label currentWeatherLabel;
    private OpenWeatherMapFeed feed;
    private ImageView weatherIcon;
    private OpenWeatherMapForecast forecast;
    private HourlyForecast hourlyForecast;
    private List<Text> dateTextsArray, tempTextsArray;
    private List<ImageView> weatherIconsArray;

    @BeforeClass
    public static void beforeClass() {
        com.sun.javafx.application.PlatformImpl.startup(() -> {
        });

    }

    @Test
    public void initialize (){
        //missing factory/service verification?
        //given
        givenOpenWeatherMapController();

        //when
        controller.initialize();

        //verify
        verify(controller.degreeUnitChoiceBox).setValue("Fahrenheit");

    }

    @Test
    public void getParameters() {
        //given
        givenOpenWeatherMapController();
        doReturn("Tel Aviv").when(locationTextField).getText();
        doReturn("Fahrenheit").when(degreeUnitChoiceBox).getValue();

        //when
        controller.getParameters();

        //then
        assertEquals("Tel Aviv", controller.location);
        assertEquals("imperial", controller.unit);
    }

    @Test
    public void getWeather(){
        //given
        givenOpenWeatherMapController();
        doReturn("Tel Aviv").when(locationTextField).getText();
        doReturn("Fahrenheit").when(degreeUnitChoiceBox).getValue();
        doReturn(Single.never()).when(service).getCurrentWeather("Tel Aviv", "imperial");
        doReturn(Single.never()).when(service).getWeatherForecast("Tel Aviv", "imperial");

        //when
        controller.getWeather();

        //then
        verify(currentWeatherLabel).setText("Current Weather: ");
        verify(service).getCurrentWeather("Tel Aviv", "imperial");
        verify(service).getWeatherForecast("Tel Aviv", "imperial");
    }

    @Test
    public void onOpenWeatherMapFeed(){
        //given
        givenOpenWeatherMapController();
        //doReturn("90.0").when(feed.main.temp);
        doReturn("http://openweathermap.org/img/wn/01n@2x.png").when(feed.weather.get(0)).getIconUrl();

        //when
        controller.onOpenWeatherMapFeed(feed);

        //then
        verify(currentWeatherText).setText("90.0\u00B0");
        verify(weatherIcon).setImage(any(Image.class));

    }

    @Test
    public void onOpenWeatherMapForecast(){
        //given
        givenOpenWeatherMapController();
        doReturn(hourlyForecast).when(forecast).getForecastFor(1);
        doReturn("Wed May 12 11:00:00 EDT 2021").when(hourlyForecast.getDate());
        doReturn("http://openweathermap.org/img/wn/01n@2x.png").when(hourlyForecast.weather.get(0).getIconUrl());

        //when
        controller.onOpenWeatherMapForecast(forecast);

        //verify
        verify(dateTextsArray).get(0).setText("Wed May 12");
        verify(tempTextsArray).get(0).setText("90.0\u00B0");
        verify(weatherIconsArray).get(0).setImage(any(Image.class));

    }

    @Test
    public void onError(){
    }

    @Test
    public void givenOpenWeatherMapController() {
        service = mock(OpenWeatherMapService.class);
        controller = new OpenWeatherMapController(service);
        factory = mock(OpenWeatherMapServiceFactory.class);
        controller.factory = factory;
        degreeUnitChoiceBox = mock(ChoiceBox.class);
        controller.degreeUnitChoiceBox = degreeUnitChoiceBox;
        currentWeatherLabel = mock(Label.class);
        controller.currentWeatherLabel = currentWeatherLabel;
        locationTextField = mock(TextField.class);
        controller.locationTextField = locationTextField;
        currentWeatherText = mock(Text.class);
        controller.currentWeatherText = currentWeatherText;
        weatherIcon = mock(ImageView.class);
        controller.weatherIcon = weatherIcon;
        dateTextsArray = Arrays.asList(
                mock(Text.class));
        controller.dateTextsArray = dateTextsArray;
        tempTextsArray = Arrays.asList(mock(Text.class));
        controller.tempTextsArray = tempTextsArray;
        weatherIconsArray = Arrays.asList(mock(ImageView.class));
        controller.weatherIconsArray = weatherIconsArray;
        feed = mock(OpenWeatherMapFeed.class);
        feed.main = mock(OpenWeatherMapFeed.Main.class);
        feed.main.temp = 90.0;
        feed.weather = Arrays.asList(mock(OpenWeatherMapFeed.Weather.class));
        forecast = mock(OpenWeatherMapForecast.class);
        hourlyForecast = mock(HourlyForecast.class);
        hourlyForecast.weather = Arrays.asList(
               mock(OpenWeatherMapForecast.HourlyForecast.Weather.class));
        hourlyForecast.main = mock(OpenWeatherMapForecast.HourlyForecast.Main.class);
        hourlyForecast.main.temp= 90.0;
    }
}
