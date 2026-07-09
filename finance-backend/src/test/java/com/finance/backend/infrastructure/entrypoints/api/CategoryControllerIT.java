package com.finance.backend.infrastructure.entrypoints.api;

import com.finance.backend.BaseIntegrationTest;
import com.finance.backend.infrastructure.database.entities.CategoryEntity;
import com.finance.backend.infrastructure.database.entities.UserEntity;
import com.finance.backend.infrastructure.database.repositories.SpringDataCategoryRepository;
import com.finance.backend.infrastructure.database.repositories.SpringDataUserRepository; // Assumindo que exista
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryControllerIT extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SpringDataCategoryRepository categoryRepository;

    @Autowired
    private CacheManager cacheManager;

    private UUID userId;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();

        // Limpa o cache do Redis antes de cada teste
        Objects.requireNonNull(cacheManager.getCache("categories")).clear();

        userId = UUID.randomUUID();

        // Cria uma categoria diretamente no banco do container para testar o GET
        CategoryEntity category = CategoryEntity.builder()
                .name("Alimentação")
                .icon("🍔")
                .color("#FF5733")
                .user(UserEntity.builder().id(userId).build()) // ID mockado do usuário
                .build();

        categoryRepository.save(category);
    }

    @Test
    void shouldListCategoriesAndCacheThemInRedis() {
        // 1. Simula a chamada HTTP GET passando o userId (Simulando o comportamento pós-JWT)
        // Como o controlador usa o @AuthenticationPrincipal, passamos o mock do ID
        String url = "/api/categories";

        // Dispara a requisição
        ResponseEntity<List> response = restTemplate.getForEntity(url + "?userId=" + userId, List.class);

        // Asserts do HTTP
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        // 2. VALIDAÇÃO DO REDIS: Verifica se o Spring realmente guardou o valor no cache "categories"
        var cache = cacheManager.getCache("categories");
        assertThat(cache).isNotNull();

        var cachedValue = cache.get(userId);
        assertThat(cachedValue).isNotNull(); // Se não for nulo, o Redis guardou com sucesso!
    }
}
