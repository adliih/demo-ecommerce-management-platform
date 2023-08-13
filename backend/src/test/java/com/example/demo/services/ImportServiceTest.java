package com.example.demo.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImportServiceTest {

  @Autowired
  private ImportService importService;

  @Test
  void testImportProviderData() throws Exception {
    importService.importProviderData();
  }
}
