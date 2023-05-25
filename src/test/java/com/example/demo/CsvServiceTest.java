package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.PlayerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class CsvServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private EntityManager entityManager;

    private CsvService csvService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        csvService = new CsvService(playerRepository, cityRepository, entityManager);
    }

    @Test
    public void testLoadDataFromCSV() throws Exception {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("playerId,field1,field2\n1,2,3\n".getBytes());
        when(csvService.getResourceAsStream()).thenReturn(inputStream);
        when(playerRepository.existsByPlayerID("1")).thenReturn(false);

        // Act
        csvService.loadDataFromCSV();

        // Assert
        verify(entityManager, times(1)).persist(any(Player.class));
        verify(entityManager, times(1)).setFlushMode(FlushModeType.COMMIT);
    }

    @Test
    public void testLoadDataFromCSV_PlayerExistsInDatabase() throws Exception {
        // Arrange
        CsvService csvService1 = Mockito.spy(csvService);
        InputStream inputStream = new ByteArrayInputStream(getCSVData().getBytes(StandardCharsets.UTF_8));

        Mockito.doReturn(inputStream).when(csvService1).getResourceAsStream();
        when(playerRepository.existsByPlayerID("1")).thenReturn(true);

        // Act
        csvService.loadDataFromCSV();

        // Assert
        verify(entityManager, never()).persist(any(Player.class));
    }

    @Test
    public void testCalculateDate_ValidData() throws ParseException {
        // Arrange
        String dateString = "2022-05-15";
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);

        // Act
        Date result = csvService.calculateDate(dateString);

        // Assert
        assert expectedDate.equals(result);
    }

    @Test
    public void testCalculateDate_BlankData() throws ParseException {
        // Arrange
        String dateString = "";

        // Act
        Date result = csvService.calculateDate(dateString);

        // Assert
        assert result == null;
    }

    @Test
    public void testCalculateChar_ValidData() {
        // Arrange
        String charString = "A";
        Character expectedChar = 'A';

        // Act
        Character result = csvService.calculateChar(charString);

        // Assert
        assert expectedChar.equals(result);
    }

    @Test
    public void testCalculateChar_BlankData() {
        // Arrange
        String charString = "";

        // Act
        Character result = csvService.calculateChar(charString);

        // Assert
        assert result == null;
    }

    @Test
    public void testCalculateLong_ValidData() {
        // Arrange
        String longString = "123";
        Long expectedLong = 123L;

        // Act
        Long result = csvService.calculateLong(longString);

        // Assert
        assert expectedLong.equals(result);
    }

    @Test
    public void testCalculateLong_BlankData() {
        // Arrange
        String longString = "";

        // Act
        Long result = csvService.calculateLong(longString);

        // Assert
        assert result == null;
    }
    private String getCSVData() {
        return "playerID,birthYear,birthMonth,birthDay,birthCountry,birthCity\n" +
                "abbotda01,1862,3,16,USA,OH,Portage,1930,2,13,USA,MI,Ottawa Lake,Dan,Abbott,Leander Franklin,190,71,R,R,1890-04-19,1890-05-23,abbod101,abbotda01\n"+
                "abbotfr01,1874,10,22,USA,OH,Versailles,1935,6,11,USA,CA,Los Angeles,Fred,Abbott,Harry Frederick,180,70,R,R,1903-04-25,1905-09-20,abbof101,abbotfr01\n"+
                "abbotgl01,1951,2,16,USA,AR,Little Rock,,,,,,,Glenn,Abbott,William Glenn,200,78,R,R,1973-07-29,1984-08-08,abbog001,abbotgl01\n";

    }
}
