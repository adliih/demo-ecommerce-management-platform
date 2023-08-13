package com.example.demo.repositories;

import com.example.demo.entities.Variant;
import com.example.demo.repositories.custom.CustomVariantRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VariantRepository extends JpaRepository<Variant, UUID>, CustomVariantRepository {

}