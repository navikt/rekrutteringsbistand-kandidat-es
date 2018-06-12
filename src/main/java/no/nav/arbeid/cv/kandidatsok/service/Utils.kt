package no.nav.arbeid.cv.kandidatsok.service

fun getEnvironment(default: String = "local"): String =
        System.getenv("FASIT_ENVIRONMENT_NAME") ?: default
