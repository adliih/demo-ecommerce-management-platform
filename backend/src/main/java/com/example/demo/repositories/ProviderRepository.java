package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
  Provider findOneByName(String name);
}
