package no.nav.arbeid.cv.es.util


fun getEnvironment(default: String = "local"): String =
        System.getenv("FASIT_ENVIRONMENT_NAME") ?: default
