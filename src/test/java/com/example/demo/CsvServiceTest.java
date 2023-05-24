package com.example.demo;

import com.example.demo.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CsvServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CsvService csvService;

    @Test
    public void testLoadDataFromCSV() throws Exception {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream(getCSVData().getBytes(StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        when(csvService.getResourceAsStream()).thenReturn(inputStream);
        when(cityRepository.save(any(City.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(entityManager.getFlushMode()).thenReturn(FlushModeType.AUTO);

        // Act
        csvService.run();

        // Assert
        verify(cityRepository, times(4)).save(any(City.class));
        verify(playerRepository, times(4)).save(any(Player.class));
        verify(entityManager, times(4)).setFlushMode(FlushModeType.COMMIT);
    }

    private String getCSVData() {
        return "playerID,birthYear,birthMonth,birthDay,birthCountry,birthCity\n" +
"abbotda01,1862,3,16,USA,OH,Portage,1930,2,13,USA,MI,Ottawa Lake,Dan,Abbott,Leander Franklin,190,71,R,R,1890-04-19,1890-05-23,abbod101,abbotda01\n"+
        "abbotfr01,1874,10,22,USA,OH,Versailles,1935,6,11,USA,CA,Los Angeles,Fred,Abbott,Harry Frederick,180,70,R,R,1903-04-25,1905-09-20,abbof101,abbotfr01\n"+
        "abbotgl01,1951,2,16,USA,AR,Little Rock,,,,,,,Glenn,Abbott,William Glenn,200,78,R,R,1973-07-29,1984-08-08,abbog001,abbotgl01\n";

    }
}