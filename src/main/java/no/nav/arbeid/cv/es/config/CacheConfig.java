package no.nav.arbeid.cv.es.config;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;


@Configuration
@EnableCaching
public class CacheConfig implements JCacheManagerCustomizer {


    public static final String ALTINN_RIGHTS_CACHE = "altinnRightsCache";
    public static final String ALTINN_ROLES_CACHE = "altinnRolesCache";

    @Override
    public void customize(CacheManager cacheManager) {

        MutableConfiguration<Object, Object> configurationTenMinutes =
                new MutableConfiguration<>().setTypes(Object.class, Object.class).setStoreByValue(false)
                        .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.TEN_MINUTES));

        cacheManager.createCache(ALTINN_RIGHTS_CACHE, configurationTenMinutes);
        cacheManager.createCache(ALTINN_ROLES_CACHE, configurationTenMinutes);

    }

}

