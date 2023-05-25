package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.Assert;

public class PlayerValidatorTest {

    @Test
    public void testIsHeightValid() {
        Assert.assertTrue(playerValidator.isHeightValid(60L));
        Assert.assertFalse(playerValidator.isHeightValid(40L));
        Assert.assertFalse(playerValidator.isHeightValid(110L));
    }

    @Test
    public void testIsWeightValid() {
        Assert.assertTrue(playerValidator.isWeightValid(180L));
        Assert.assertFalse(playerValidator.isWeightValid(120L));
        Assert.assertFalse(playerValidator.isWeightValid(250L));
    }

    @Test
    public void testIsYearValid() {
        Assert.assertTrue(playerValidator.isYearValid(2000L));
        Assert.assertFalse(playerValidator.isYearValid(1900L));
        Assert.assertFalse(playerValidator.isYearValid(2025L));
    }

    @Test
    public void testIsMonthValid() {
        Assert.assertTrue(playerValidator.isMonthValid(6L));
        Assert.assertFalse(playerValidator.isMonthValid(0L));
        Assert.assertFalse(playerValidator.isMonthValid(13L));
    }

    @Test
    public void testIsDayValid() {
        Assert.assertTrue(playerValidator.isDayValid(15L));
        Assert.assertFalse(playerValidator.isDayValid(0L));
        Assert.assertFalse(playerValidator.isDayValid(32L));
    }
}