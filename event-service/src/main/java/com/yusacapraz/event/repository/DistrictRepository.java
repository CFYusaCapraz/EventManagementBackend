package com.yusacapraz.event.repository;

import com.yusacapraz.event.model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    Optional<District> findDistrictByDistrictName(String districtName);
}
