package io.github.oodachi.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAProvider;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.PostgreSQLTemplates;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        for (int i = 0; i < 10; i++) {
            Post post = new Post();
            post.setTitle(RandomStringUtils.randomAlphabetic(10));
            post.setContent(RandomStringUtils.randomAlphabetic(10));
            post.setDate(LocalDate.now());
            post.setTimestamp(LocalDateTime.now());
            postRepository.save(post);
        }
    }

    @Test
    void testJPQLQuery() {
        JPQLTemplates templates = JPAProvider.getTemplates(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(templates, entityManager);

        List<PostVO> fetch1 = jpaQueryFactory.select(Projections.fields(PostVO.class,
                        QPost.post.title,
                        QPost.post.content,
//                        QPost.post.date,
                        QPost.post.timestamp))
                .from(QPost.post).fetch();
        assertEquals(10, fetch1.size());
    }

    @Test
    void testJPASQLQuery() {
        JPASQLQuery<?> jpasqlQuery = new JPASQLQuery<>(entityManager, PostgreSQLTemplates.DEFAULT);

        List<PostVO> fetch = jpasqlQuery.select(Projections.fields(PostVO.class,
                        QPost.post.title,
                        QPost.post.content,
//                        QPost.post.date,
                        QPost.post.timestamp))
                .from(QPost.post).fetch();
        assertEquals(10, fetch.size());
    }
}