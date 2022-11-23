package zerobase.weather.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import zerobase.weather.DiaryService.DiaryService;
import zerobase.weather.domain.Diary;

@RequiredArgsConstructor
@RestController
public class DiaryController {

    private final DiaryService diaryService;

    @ApiOperation(value = "일기 텍스트와 날씨를 이용해서 DB에 일기 저장")
    @PostMapping("/create/diary")
    public void createDiary(

        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestBody String text) {
        
        diaryService.createDiary(date, text);        
    }

    @ApiOperation(value = "선택한 날짜에 모든 일기 데이터를 가져온다.")
    @GetMapping("/read/diary")
    public List<Diary> readDiaries(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {    
        
        return diaryService.readDiaries(date);
    }

    @ApiOperation(value = "선택한 기간중의 모든 일기 데이터를 가져온다.")
    @GetMapping("/read/diaries")
    public List<Diary> readDiaries(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "조회할 기간의 첫번째 날입니다.", example = "2022-10-22") LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "조회할 기간의 마지막 날입니다.", example = "2022-10-22") LocalDate endDate) {    
        
        return diaryService.readDiaries(startDate, endDate);
    }

    @PutMapping("/update/diary")
    public void updateDiary(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestBody String text) {

        diaryService.updateDiary(date, text);
    }

    @DeleteMapping("/delete/diary")
    public void deleteDiary(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        diaryService.deleteDiary(date);
    }

}
