package com.yusacapraz.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    private UUID addressId;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "door_number")
    private Short doorNumber;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "countryId")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "cityId")
    private City city;

    @ManyToOne
    @JoinColumn(name = "district_id", referencedColumnName = "districtId")
    private District district;
}
