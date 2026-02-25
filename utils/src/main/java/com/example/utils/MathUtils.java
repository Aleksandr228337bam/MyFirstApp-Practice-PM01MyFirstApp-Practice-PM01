package com.example.utils;

public class MathUtils {

    public static int add(int a, int b) {
        return a + b;
    }

    public static double calculateBmi(double weightKg, double heightMeters) {
        if (heightMeters == 0) {
            return 0;
        }
        return weightKg / (heightMeters * heightMeters);
    }
}