package com.example.demo;

public class playerValidator {
    public static boolean isHeightValid(Long height){
        return height>50 && height <100;
    }
    public static boolean isWeightValid(Long weight){
        return weight>150 && weight <225;
    }
    public static boolean isYearValid(Long year){
        return year>=1950 && year<=2024;
    }
    public static boolean isMonthValid(Long month){
        return month>=1 && month<=12;
    }
    public static boolean isDayValid(Long day){
        return day>=1 && day<=31;
    }
}
