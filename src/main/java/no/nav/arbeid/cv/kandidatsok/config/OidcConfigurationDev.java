package no.nav.arbeid.cv.kandidatsok.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import no.nav.security.spring.oidc.test.TokenGeneratorConfiguration;

@Configuration()
@Profile({"dev"})
@Import(TokenGeneratorConfiguration.class)
public class OidcConfigurationDev {
}
