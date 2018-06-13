package no.nav.arbeid.cv.kandidatsok.service

import no.finn.unleash.DefaultUnleash
import no.finn.unleash.strategy.Strategy
import no.finn.unleash.util.UnleashConfig
import no.nav.arbeid.cv.kandidatsok.service.getEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service
import java.util.Arrays.asList

class IsDevStrategy : Strategy {
    override fun getName() = "isDev"

    override fun isEnabled(parameters: MutableMap<String, String>?) =
            "local".equals(getEnvironment(), ignoreCase = true)
}

class IsNotProdStrategy : Strategy {
    val prodEnvs: List<String> = asList("p", "q0")

    override fun getName() = "isNotProd"

    override fun isEnabled(parameters: Map<String, String>) =
            !prodEnvs.contains(getEnvironment())
}

@Service
class KandidatsokUnleashService {

    @Autowired
    lateinit var env: Environment

    val unleash: DefaultUnleash by lazy {

        val appName = "pam-cv-indexer"
        val environment = getEnvironment()
        val instanceId = "$appName-$environment"
        val apiUrl = env["unleash.api.url"]

        val config = UnleashConfig.builder()
                .appName(appName)
                .instanceId(instanceId)
                .unleashAPI(apiUrl)
                .build()

        DefaultUnleash(
                config,
                IsDevStrategy(),
                IsNotProdStrategy())
    }
}
