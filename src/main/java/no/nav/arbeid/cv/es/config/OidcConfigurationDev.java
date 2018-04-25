package no.nav.arbeid.cv.es.config;

import no.nav.security.spring.oidc.test.TokenGeneratorConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration()
@Profile({"dev"})
@Import(TokenGeneratorConfiguration.class)
public class OidcConfigurationDev {
}
