package no.nav.arbeid.cv.indexer.rest

import no.nav.arbeid.cv.indexer.service.IndexerUnleashService
import no.nav.security.spring.oidc.validation.api.Unprotected
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@RestController
@RequestMapping("/rest/indexer/toggles", produces = [(MediaType.APPLICATION_JSON_VALUE)])
@Unprotected
class IndexerFeatureToggleController @Inject constructor(val unleashService: IndexerUnleashService) {

    @GetMapping
    fun featureToggles(@RequestParam(name = "feature", required = false) features: List<String>?): ResponseEntity<Map<String, Boolean>> {
        val featureMap = features?.map { it to unleashService.unleash.isEnabled("pam-kandidatsok.$it") }?.toMap()
        return ResponseEntity(featureMap ?: HashMap(), HttpStatus.OK)
    }
}
