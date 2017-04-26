package com.softserveinc.dsoky.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateFormatValidatorTest {
    @Test
    public void parseValidString(){
        assertTrue(DateFormatValidator.checkDateFormat("2017-01-01"));
    }
    @Test
    public void returnFalseForInvalidString(){
        assertFalse(DateFormatValidator.checkDateFormat("12-31-3000"));
    }
    @Test
    public void returnFalseForEmptyString(){
        assertFalse(DateFormatValidator.checkDateFormat(""));
    }
}
