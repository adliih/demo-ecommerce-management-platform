package com.example.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, UUID> {
  Provider findOneByName(String name);
}
