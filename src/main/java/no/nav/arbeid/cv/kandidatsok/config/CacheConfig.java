package no.nav.arbeid.cv.kandidatsok.config;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig implements JCacheManagerCustomizer {
  private final static Logger LOGGER = LoggerFactory.getLogger(CacheConfig.class);

  public static final String ALTINN_RIGHTS_CACHE = "altinnRightsCache";
  public static final String ALTINN_ROLES_CACHE = "altinnRolesCache";

  @Override
  public void customize(CacheManager cacheManager) {

    MutableConfiguration<Object, Object> configurationTenMinutes =
        new MutableConfiguration<>().setTypes(Object.class, Object.class).setStoreByValue(false)
            .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.TEN_MINUTES));

    try {
      cacheManager.createCache(ALTINN_RIGHTS_CACHE, configurationTenMinutes);
    } catch (CacheException e) {
      LOGGER.info("Greide ikke å opprette cache {}. Fins den fra før? {}", ALTINN_RIGHTS_CACHE,
          e.getMessage(), e);
    }
    try {
      cacheManager.createCache(ALTINN_ROLES_CACHE, configurationTenMinutes);
    } catch (CacheException e) {
      LOGGER.info("Greide ikke å opprette cache {}. Fins den fra før? {}", ALTINN_ROLES_CACHE,
          e.getMessage(), e);
    }

  }

}

