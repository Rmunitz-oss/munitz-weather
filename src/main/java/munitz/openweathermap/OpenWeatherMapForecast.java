package munitz.openweathermap;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OpenWeatherMapForecast {
   List<HourlyForecast> list;

    static class HourlyForecast{
        long dt;
        Main main;
        List<Weather>weather;

        static class Main{
            double temp;
        }

        static class Weather{
            String main;
            String icon;

            public String getIconUrl(){
                return "http://openweathermap.org/img/wn/" + icon + "@2x.png";
            }
        }

        public Date getDate(){
            return new Date(dt * 1000);
        }

        @Override
        public String toString(){
            return getDate() + " ";
           // return getDate() + " " +
                    //main.temp + " " +
                    //weather.get(0).main + " " +
                    //weather.get(0).getIconUrl();
        }
    }

    /**
     *
     * @param day amount of days past today
     *            to retrieve midday forecasts of
     * @return forecast matching 11 AM of specified day
     *          or null if day is out of range
     */
    public HourlyForecast getForecastFor(int day){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date elevenAM = calendar.getTime();
        for(HourlyForecast forecast : list){
            if ( forecast.dt * 1000 >= elevenAM.getTime()){
                return forecast;
            }
        }
        return null;
    }
}
