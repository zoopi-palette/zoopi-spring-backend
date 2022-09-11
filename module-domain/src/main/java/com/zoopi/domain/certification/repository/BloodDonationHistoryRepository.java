package com.zoopi.domain.certification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zoopi.domain.certification.entity.BloodDonationHistory;

@Repository
public interface BloodDonationHistoryRepository extends CrudRepository<BloodDonationHistory, Long> {

	@Query("select h from BloodDonationHistory h join fetch h.hospital join fetch h.pet where h.pet.id = :petId")
	List<BloodDonationHistory> findAllByPetId(Long petId);
}
