package munitz.openweathermap;


import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {

    @GET("data/2.5/weather?q&appid=d97a6d2415d815195efeb79b37e22a09")
    Single<OpenWeatherMapFeed>getCurrentWeather(@Query("q")String location);
}
