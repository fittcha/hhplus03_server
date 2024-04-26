package io.hhplus.server.intergration.base;

import com.google.common.base.CaseFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@ActiveProfiles("test")
public class DatabaseCleanup implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Environment env;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        boolean testProfile = isTestProfile();

        if (testProfile) {
            // H2 데이터베이스용 구문
            entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        } else {
            // 다른 데이터베이스용 구문
            entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        }

        for (String tableName : tableNames) {
            String formattedTableName = testProfile ? "\"" + tableName + "\"" : tableName;
            entityManager.createNativeQuery("TRUNCATE TABLE " + formattedTableName).executeUpdate();
            if (testProfile) {
                entityManager.createNativeQuery("ALTER TABLE " + formattedTableName + " AUTO_INCREMENT = 1").executeUpdate();
            }
        }

        if (isTestProfile()) {
            entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
        } else {
            entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        }
    }

    public boolean isTestProfile() {
        // 현재 활성화된 프로파일 확인
        String[] activeProfiles = env.getActiveProfiles();
        boolean isTestProfileActive = false;
        for (String profile : activeProfiles) {
            if ("test".equals(profile)) {
                isTestProfileActive = true;
                break;
            }
        }

        return isTestProfileActive;
    }
}
