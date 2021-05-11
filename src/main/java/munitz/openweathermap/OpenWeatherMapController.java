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



public class OpenWeatherMapController {
    @FXML
    Label locationLabel;
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
    ArrayList<Text> dateTextsArray;
    @FXML
    ArrayList<Text> tempTextsArray;
    @FXML
    ArrayList<ImageView>weatherIconsArray;

    String location;
    String unit;
    String iconURL;
    OpenWeatherMapServiceFactory factory;
    OpenWeatherMapService service;

    public OpenWeatherMapController(OpenWeatherMapService service){
        this.service = service;
    }


    @FXML
    public void initialize(){
        factory = new OpenWeatherMapServiceFactory();
        service = factory.newInstance();
        degreeUnitChoiceBox.setItems(FXCollections.observableArrayList("Celsius", "Fahrenheit"));
        //default to Fahrenheit
        degreeUnitChoiceBox.setValue("Fahrenheit");

    }

    public void getParameters(){
        location = locationTextField.getText();
        unit = (degreeUnitChoiceBox.getValue().equals("Fahrenheit"))? "imperial" : "metric";
    }

    public void getWeather(){
        getParameters();
        currentWeatherLabel.setText("Current Weather in " + location + ": ");
        Disposable disposableFeed = service.getCurrentWeather(location,unit)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
                .subscribe(this :: onOpenWeatherMapFeed, this :: onError);
        Disposable disposableForecast = service.getWeatherForecast(location,unit)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
                .subscribe(this :: onOpenWeatherMapForecast, this :: onError);
    }

    public void onOpenWeatherMapFeed(OpenWeatherMapFeed feed){
        currentWeatherText.setText((feed.main.temp) +"\u00B0");
        weatherIcon.setImage(new Image(feed.weather.get(0).getIconUrl()));
    }

    public void onOpenWeatherMapForecast(OpenWeatherMapForecast forecast){
        for (int day = 1; day < 6; day++){
            HourlyForecast hourlyForecast = forecast.getForecastFor(day);
            dateTextsArray.get(day -1).setText(cleanDate(hourlyForecast));
            tempTextsArray.get(day -1).setText((hourlyForecast.main.temp) + "\u00B0");
            weatherIconsArray.get(day-1).setImage(new Image(hourlyForecast.weather.get(0).getIconUrl()));
        }
    }

    public String cleanDate(HourlyForecast hourlyForecast){
        String[] splitDay = String.valueOf(hourlyForecast.getDate()).split(" ");
        String cleanDate = splitDay[0] + " "
                + splitDay[1] + " "
                + splitDay[2];
        return cleanDate;
    }


    public void onError(Throwable throwable){

        //throwable.getStackTrace();
        System.out.println("Error");
    }
}
