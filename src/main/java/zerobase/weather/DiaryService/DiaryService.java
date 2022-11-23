package zerobase.weather.DiaryService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import zerobase.weather.WeatherApplication;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.InvalidDate;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;
    private final static Logger logger = LoggerFactory.getLogger(WeatherApplication.class);

    @Value("${openweathermap.key}")
    private String apiKey;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveDateWeather() {
        dateWeatherRepository.save(this.getWeatherFromAPI());
    }

    private DateWeather getWeatherFromAPI() {

        String weatherData = this.getWeatherString();
        Map<String, Object> parsedWeather = this.parseWeather(weatherData);

        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(parsedWeather.get("weather").toString());
        dateWeather.setIcon(parsedWeather.get("icon").toString());
        dateWeather.setTemperature((double) parsedWeather.get("temperature"));
        return dateWeather;
    }

    public String getWeatherString() {

        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            System.out.println(response.toString());

            return response.toString();

        } catch (Exception e) {
            return "failed to get response";
        }
    }

    private Map<String, Object> parseWeather(String jsonString) {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashMap<>();

        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));

        JSONArray weatherDataArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherDataArray.get(0);
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));

        return resultMap;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text) {

        logger.info("started to create diary");

        DateWeather dateWeather = this.getDateWeather(date);

        Diary nowDiary = Diary.setDateWeather(dateWeather);
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);

        logger.info("end to create diary");
    }

    private DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> list = dateWeatherRepository.findAllByDate(date);
        if (list.size() == 0) {
            return getWeatherFromAPI();
        } else {
            return list.get(0);
        }
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate date) {

        logger.debug("read diary");

        if (date.isAfter(LocalDate.ofYearDay(3050, 0))) {
            throw new InvalidDate();
        }
        return diaryRepository.findAllByDate(date);
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {

        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    @Transactional
    public void updateDiary(LocalDate date, String text) {

        Diary diary = diaryRepository.getFirstByDate(date);
        diaryRepository.save(diary);
    }

    @Transactional
    public void deleteDiary(LocalDate date) {

        diaryRepository.deleteAllByDate(date);
    }
}
