package com.zoopi.domain.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.pet.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {

}
