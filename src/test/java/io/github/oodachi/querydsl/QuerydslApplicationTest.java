package io.github.oodachi.querydsl;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAProvider;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.SQLTemplatesRegistry;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class QuerydslApplicationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PostRepository postRepository;


    @Test
    void contextLoads() {
        Assertions.assertNotNull(entityManager);
        Assertions.assertNotNull(dataSource);
        Assertions.assertNotNull(postRepository);
    }

    @Test
    void testJPQLQuery() {
        JPQLTemplates templates = JPAProvider.getTemplates(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(templates, entityManager);

        Post post = new Post();
        post.setTitle(RandomStringUtils.randomAlphabetic(32));
        post.setContent(RandomStringUtils.randomAlphabetic(200));
        post.setDate(LocalDate.now());
        post.setTimestamp(LocalDateTime.now());
        postRepository.save(post);

        List<Post> fetch = jpaQueryFactory.select(QPost.post).from(QPost.post).fetch();
        assertFalse(fetch.isEmpty());
    }

    @Test
    void testJPASQLQuery() throws SQLException {
        var sqlTemplatesRegistry = new SQLTemplatesRegistry();
        DatabaseMetaData metaData;
        try (var connection = dataSource.getConnection()) {
            metaData = connection.getMetaData();
        }
        SQLTemplates templates = sqlTemplatesRegistry.getTemplates(metaData);

        JPASQLQuery<?> jpasqlQuery = new JPASQLQuery<>(entityManager, templates);
        List<Post> fetch = jpasqlQuery.select(QPost.post).from(QPost.post).fetch();
        assertFalse(fetch.isEmpty());
    }
}