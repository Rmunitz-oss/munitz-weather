package munitz.openweathermap;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import munitz.openweathermap.OpenWeatherMapForecast.HourlyForecast;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

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
    private ArrayList<Text> dateTextsArray;
    private ArrayList<Text> tempTextsArray;
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
    //does not work
    public void initialize (){
        //given
        givenOpenWeatherMapController();
        doReturn(service).when(factory).newInstance();
        doNothing().when(degreeUnitChoiceBox).setItems(FXCollections.observableArrayList("Celsius", "Fahrenheit"));
        //when
        controller.initialize();
        //then
        verify(factory).newInstance();
        //verify(controller.degreeUnitChoiceBox).getItems()
                //.equals(FXCollections.observableArrayList("Celsius", "Fahrenheit"));
        verify(controller.degreeUnitChoiceBox).getValue().equals("Fahrenheit");
        Assert.assertEquals(service,factory.newInstance());
        Assert.assertEquals("Fahrenheit", degreeUnitChoiceBox.getValue());

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
        Assert.assertEquals("Tel Aviv", controller.location);
        Assert.assertEquals("imperial", controller.unit);
    }

    @Test
    //does not work
    public void getWeather(){
        //given
        givenOpenWeatherMapController();
        controller.location = "Tel Aviv";

        //when
        controller.getWeather();

        //then
        verify(controller, times(1)).getParameters();
        verify(currentWeatherLabel).setText("Current Weather in Tel Aviv: ");
    }


    @Test
    public void onOpenWeatherMapFeed(){
        //given
        givenOpenWeatherMapController();
        doNothing().when(currentWeatherText).setText((feed.main.temp) +"\u00B0");
        //doReturn("http://openweathermap.org/img/wn/01n@2x.png").when(feed.weather.get(0).getIconUrl());
        //Image image = mock(Image.class);
        //doNothing().when(weatherIcon).setImage(any(Image.class));


        //when
        controller.onOpenWeatherMapFeed(feed);

        //then
        verify(currentWeatherText).setText(String.valueOf(feed.main.temp));
       // verify(weatherIcon).setImage(any(Image.class));

    }


    @Test
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
        doReturn(hourlyForecast).when(forecast).getForecastFor(anyInt());


    }

    @Test
    public void onError(){


    }



    @Test
    public void givenOpenWeatherMapController(){
        controller = new OpenWeatherMapController();
        service = mock(OpenWeatherMapService.class);
        controller.service = service;
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
        feed.main.temp = 90;
    }
}
