package com.example.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Data
@NoArgsConstructor
public class Player {

    @Id
    String playerID;
    Long birthYear;
    Long birthMonth ;
    Long birthDay;
    @ManyToOne
    @JoinColumn(name = "birthCountry",referencedColumnName = "countryId")
    Country birthCountry;
    @ManyToOne
    @JoinColumn(name = "birthState",referencedColumnName = "stateId")
    State birthState;
    @ManyToOne
    @JoinColumn(name = "birthCity",referencedColumnName = "cityId")
    City birthCity;
    Long deathYear;
    Long deathMonth ;
    Long deathDay;
    @ManyToOne
    @JoinColumn(name = "deathCountry",referencedColumnName = "countryId")
    Country deathCountry;
    @ManyToOne
    @JoinColumn(name = "deathState",referencedColumnName = "stateId")
    State deathState;
    @ManyToOne
    @JoinColumn(name = "deathCity",referencedColumnName = "cityId")
    City deathCity;
    String nameFirst;
    String nameLast;
    String nameGiven;
    Double weight;
    Double height;
    Character bats;
    Character _throws;
    Date debut;
    Date finalGame;
    String retroID;
    String bbrefID;
}
