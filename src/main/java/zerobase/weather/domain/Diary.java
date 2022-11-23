package zerobase.weather.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String weather;
    private String icon;
    private double temperature;
    private String text;
    private LocalDate date; 

    public static Diary setDateWeather(DateWeather dateWeather) {

        Diary diary = new Diary();
        diary.date = dateWeather.getDate();
        diary.weather = dateWeather.getWeather();
        diary.icon = dateWeather.getIcon();
        diary.temperature = dateWeather.getTemperature();
        return diary;
    }
}
