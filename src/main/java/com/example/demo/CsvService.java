package com.example.demo;


import com.example.demo.model.City;
import com.example.demo.model.Country;
import com.example.demo.model.Player;
import com.example.demo.model.State;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Scope(proxyMode = ScopedProxyMode.INTERFACES)
@Transactional
@Component
public class CsvService implements CommandLineRunner {
    private final PlayerRepository playerRepository;
    private final CityRepository cityRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    private static final int BATCH_SIZE = 1000;

    @Autowired
    public CsvService(PlayerRepository playerRepository, CityRepository cityRepository, EntityManager entityManager) {
        this.playerRepository = playerRepository;
        this.cityRepository = cityRepository;
        this.entityManager = entityManager;
    }


    @Override
    public void run(String... args) throws Exception {
        loadDataFromCSV();
    }

    @Transactional
    public void loadDataFromCSV() {
        try (InputStream inputStream = getResourceAsStream()) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                List<Player> playerBatch = new ArrayList<>();
                Map<String, City> cityMap = new HashMap<>();
                Map<String, State> stateMap = new HashMap<>();
                Map<String, Country> countryMap = new HashMap<>();

                String line;
                int count = 0;
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    int fieldsNumber = 24;
                    if (data.length == fieldsNumber) {
                        String playerId = data[0];
                        boolean playerExistsInBatch = playerBatch.stream()
                                .anyMatch(player -> player.getPlayerID().equals(playerId));

                        if (!playerExistsInBatch) {
                            // Check if a player with the same name already exists in the database
                            boolean playerExistsInDatabase = playerRepository.existsByPlayerID(playerId);

                            if (!playerExistsInDatabase) {
                                Long birthYear = calculateLong(data[1]);
                                if (birthYear!=null&&!playerValidator.isYearValid(birthYear)) {
                                    birthYear = null;
                                }
                                Long birthMonth = calculateLong(data[2]);
                                if (birthMonth!=null&&!playerValidator.isMonthValid(birthMonth)) {
                                    birthMonth = null;
                                }
                                Long birthDay = calculateLong(data[3]);
                                if (birthDay!=null&&!playerValidator.isDayValid(birthDay)) {
                                    birthDay = null;
                                }
                                Country countryBirth = countryMap.computeIfAbsent(data[4], k -> {
                                    Country newCountry = new Country();
                                    newCountry.setName(data[4]);
                                    return newCountry;
                                });
                                State stateBirth = stateMap.computeIfAbsent(data[5], k -> {
                                    State newState = new State();
                                    newState.setName(data[5]);
                                    return newState;
                                });
                                City cityBirth = cityMap.computeIfAbsent(data[6], k -> {
                                    City newCity = new City();
                                    newCity.setName(data[6]);
                                    return newCity;
                                });
                                Long deathYear = calculateLong(data[7]);
                                if (deathYear!= null&& !playerValidator.isYearValid(deathYear)) {
                                    deathYear = null;
                                }
                                Long deathMonth = calculateLong(data[8]);
                                if (deathMonth!=null &&!playerValidator.isMonthValid(deathMonth)) {
                                    deathMonth = null;
                                }
                                Long deathDay = calculateLong(data[9]);
                                if (deathDay!=null&&!playerValidator.isDayValid(deathDay)) {
                                    deathDay = null;
                                }
                                Country countryDeath = countryMap.computeIfAbsent(data[10], k -> {
                                    Country newCountry = new Country();
                                    newCountry.setName(data[10]);
                                    return newCountry;
                                });
                                State stateDeath = stateMap.computeIfAbsent(data[11], k -> {
                                    State newState = new State();
                                    newState.setName(data[11]);
                                    return newState;
                                });
                                City cityDeath = cityMap.computeIfAbsent(data[12], k -> {
                                    City newCity = new City();
                                    newCity.setName(data[12]);
                                    return newCity;
                                });
                                String nameFirst = data[13];
                                String nameLast = data[14];
                                String nameGiven = data[15];
                                Long weight = calculateLong(data[16]);
                                if (weight!=null&& !playerValidator.isWeightValid(weight))
                                    weight = null;
                                Long height = calculateLong(data[17]);
                                if (height!=null &&!playerValidator.isHeightValid(height))
                                    height = null;
                                Character bats = calculateChar(data[18]);
                                Character _throws = calculateChar(data[19]);
                                Date debut = calculateDate(data[20]);
                                Date finalGame = calculateDate(data[21]);
                                String retroID = data[22];
                                String bbrefID = data[23];

                                Player player = new Player();
                                player.setPlayerID(playerId);
                                player.setPlayerID(playerId);
                                player.setBirthYear(birthYear);
                                player.setBirthMonth(birthMonth);
                                player.setBirthDay(birthDay);
                                player.setBirthState(stateBirth);
                                player.setBirthCountry(countryBirth);
                                player.setBirthCity(cityBirth);
                                player.setDeathYear(deathYear);
                                player.setDeathMonth(deathMonth);
                                player.setDeathDay(deathDay);
                                player.setDeathCountry(countryDeath);
                                player.setDeathState(stateDeath);
                                player.setDeathCity(cityDeath);
                                player.setNameFirst(nameFirst);
                                player.setNameLast(nameLast);
                                player.setNameGiven(nameGiven);
                                player.setWeight(weight);
                                player.setHeight(height);
                                player.setBats(bats);
                                player.set_throws(_throws);
                                player.setDebut(debut);
                                player.setFinalGame(finalGame);
                                player.setRetroID(retroID);
                                player.setBbrefID(bbrefID);
                                playerBatch.add(player);
                                count++;

                                if (count % BATCH_SIZE == 0) {
                                    savePlayerBatch(playerBatch);
                                }
                            }
                        }
                    }
                }

                saveCityBatch(new ArrayList<>(cityMap.values()));
                saveCountryBatch(new ArrayList<>(countryMap.values()));
                saveStateBatch(new ArrayList<>(stateMap.values()));
                savePlayerBatch(playerBatch);

            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public InputStream getResourceAsStream() {
        return getClass().getResourceAsStream("/player.csv");
    }

    Date calculateDate(String data) throws ParseException {
        if (!data.isBlank())
            return new SimpleDateFormat("yyyy-MM-dd").parse(data);
        return null;
    }

    Character calculateChar(String data) {
        if (!data.isBlank())
            return data.charAt(0);
        return null;
    }

    Long calculateLong(String data) {
        if (!data.isBlank())
            return Long.parseLong(data);
        return null;
    }

    @Transactional
    private void savePlayerBatch(List<Player> playerBatch) {
        playerBatch.forEach(entityManager::persist);
        entityManager.setFlushMode(FlushModeType.COMMIT);
    }

    @Transactional
    private void saveCityBatch(List<City> cityBatch) {
        cityBatch.forEach(entityManager::persist);
        entityManager.setFlushMode(FlushModeType.COMMIT);
    }

    @Transactional
    private void saveStateBatch(List<State> stateBatch) {
        stateBatch.forEach(entityManager::persist);
        entityManager.setFlushMode(FlushModeType.COMMIT);
    }

    @Transactional
    private void saveCountryBatch(List<Country> countryBatch) {
        countryBatch.forEach(entityManager::persist);
        entityManager.setFlushMode(FlushModeType.COMMIT);
    }
}
