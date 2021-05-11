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
    private OpenWeatherMapFeed feed;
    private Text currentWeatherText;
    private ImageView weatherIcon;
    private ArrayList<Text> dateTextsArray, tempTextsArray;
   // private ArrayList<Text> tempTextsArray;
    private ArrayList<ImageView> weatherIconsArray;
    private HourlyForecast hourlyForecast;
    private Label currentWeatherLabel;
    private OpenWeatherMapForecast forecast;

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
        //verify(controller).getParameters(); //controller is not a mock. how to verify getParameters() is called?
        verify(currentWeatherLabel).setText("Current Weather in Tel Aviv: ");
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
        //any image?
        //Image("http://openweathermap.org/img/wn/01n@2x.png"));

    }

    @Test
    //how to test?
    public void cleanDate(){
        //given
        givenOpenWeatherMapController();
        String [] splitDay = any(String[].class);
        //doReturn(anyString()).when(controller.cleanDate(hourlyForecast));

        //when
        controller.cleanDate(hourlyForecast);

        //then
        verify(String.valueOf(hourlyForecast.getDate()).split(""));
    }
    
    @Test
    public void onOpenWeatherMapForecast(){
        //given
        givenOpenWeatherMapController();
        doReturn(hourlyForecast).when(forecast).getForecastFor(1);
        //doReturn("May 4 ").when(controller.cleanDate(hourlyForecast));
        //doReturn("90").when(hourlyForecast.main.temp);
        //doReturn("http://openweathermap.org/img/wn/01n@2x.png").when(hourlyForecast.weather.get(0).getIconUrl());


        //when
        controller.onOpenWeatherMapForecast(forecast);

        //verify
        verify(dateTextsArray).get(0).setText("May 4");
        verify(tempTextsArray).get(0).setText("90.0\u00B0");
        verify(weatherIconsArray).get(0).setImage(any(Image.class));

    }

    @Test
    public void onError(){
    }



    @Test
    //redo
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
        dateTextsArray = mock(ArrayList.class);
        controller.dateTextsArray = dateTextsArray;
        tempTextsArray = mock(ArrayList.class);
        controller.tempTextsArray = tempTextsArray;
        weatherIconsArray = mock(ArrayList.class);
        controller.weatherIconsArray = weatherIconsArray;
        forecast = mock(OpenWeatherMapForecast.class);
        hourlyForecast = mock(HourlyForecast.class);
        feed = mock(OpenWeatherMapFeed.class);
        feed.main = mock(OpenWeatherMapFeed.Main.class);
        feed.main.temp = 90.0;
        //hourlyForecast.main.temp= 90.0;
        hourlyForecast.weather = Arrays.asList(
               mock(OpenWeatherMapForecast.HourlyForecast.Weather.class));

    }
}
