package munitz.openweathermap;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import munitz.openweathermap.OpenWeatherMapForecast.HourlyForecast;
import java.util.ArrayList;
import java.util.List;


public class OpenWeatherMapController {
    @FXML
    TextField locationTextField;
    @FXML
    Label currentWeatherLabel;
    @FXML
    Text currentWeatherText;
    @FXML
    ChoiceBox<String> degreeUnitChoiceBox;
    @FXML
    ImageView weatherIcon;
    @FXML
    List<Text> dateTextsArray;
    @FXML
    List<Text> tempTextsArray;
    @FXML
    List<ImageView>weatherIconsArray;

    String location;
    String unit;
    OpenWeatherMapServiceFactory factory;
    OpenWeatherMapService service;

    public OpenWeatherMapController(OpenWeatherMapService service){
        this.service = service;
    }

    /**
     * initializes app
     * sets default degree unit to Fahrenheit
     */
    @FXML
    public void initialize(){
        factory = new OpenWeatherMapServiceFactory();
        service = factory.newInstance();
        degreeUnitChoiceBox.setItems(FXCollections.observableArrayList("Celsius", "Fahrenheit"));
        degreeUnitChoiceBox.setValue("Fahrenheit");
    }

    /**
     * gets location and degree unit
     * from user input
     */
    public void getParameters(){
        location = locationTextField.getText();
        unit = (degreeUnitChoiceBox.getValue().equals("Fahrenheit"))? "imperial" : "metric";
    }

    /**
     * calls getParameter method
     * and getCurrentWeather and getWeatherForecast
     *
     */
    public void getWeather(){
        getParameters();
        currentWeatherLabel.setText("Current Weather: ");
        Disposable disposableFeed = service.getCurrentWeather(location,unit)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
                .subscribe(this :: onOpenWeatherMapFeed, this :: onError);
        Disposable disposableForecast = service.getWeatherForecast(location,unit)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
                .subscribe(this :: onOpenWeatherMapForecast, this :: onError);
    }

    /**
     * gets current weather and weather icon
     * @param feed
     */
    public void onOpenWeatherMapFeed(OpenWeatherMapFeed feed){
        currentWeatherText.setText((feed.main.temp) +"\u00B0");
        weatherIcon.setImage(new Image(feed.weather.get(0).getIconUrl()));
    }

    /**
     * gets and sets date,weather forecast,and weather icon
     * for the next five days
     * @param forecast
     */
    public void onOpenWeatherMapForecast(OpenWeatherMapForecast forecast){// fix size of array
        for (int day = 0; day < dateTextsArray.size(); day++){
            HourlyForecast hourlyForecast = forecast.getForecastFor(day+1);
            dateTextsArray.get(day).setText(cleanDate(hourlyForecast));
            tempTextsArray.get(day).setText((hourlyForecast.main.temp) + "\u00B0");
            weatherIconsArray.get(day).setImage(new Image(hourlyForecast.weather.get(0).getIconUrl()));
        }
    }

    /**
     * cleans full date from HourlyForecast object
     * and returns day of the week and date
     * @param hourlyForecast
     * @return string date
     */
    public String cleanDate(HourlyForecast hourlyForecast){
        String[] splitDay = String.valueOf(hourlyForecast.getDate()).split(" ");
        String cleanDate = splitDay[0] + " "
                + splitDay[1] + " "
                + splitDay[2];
        return cleanDate;
    }


    public void onError(Throwable throwable){
        System.out.println("Error");
    }
}
