package no.nav.arbeidsgiver.kandidat.kandidatsok.es

import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.KandidatsokTransformer
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsPerioderMedInaktivitet
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.SokekriterierVeiledere
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME
import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

class SøkEtterHullICvIT {

    private val sokClient =
        ElasticSearchTestConfiguration.esSokService(DEFAULT_INDEX_NAME)

    private val indexerClient = ElasticSearchTestConfiguration.indexerCvService()

    private val søkekriterierHullICv = SokekriterierVeiledere.med().hullICv(true).bygg()

    private val minimumHullVarighetAntallÅr = 2L

    @BeforeClass
    fun before() {
        indexerClient.createIndex(DEFAULT_INDEX_NAME)
    }

    @AfterEach
    fun after() {
        indexerClient.deleteIndex(DEFAULT_INDEX_NAME)
    }

    @Test
    fun harIkkeHullHvisErIJobb() {
        val cv = giveMeEsCv(
            "125687",
            EsPerioderMedInaktivitet(
                null,
                listOf(toDate(LocalDate.now().minusYears(minimumHullVarighetAntallÅr)))
            )
        )
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        val actual = sokClient.veilederSok(søkekriterierHullICv).cver
        assertThat(actual).hasSize(0)
    }

    @Test
    fun harIkkeHullNårHarBlittInaktivForKortTidSiden() {
        val cv = giveMeEsCv(
            "9846517",
            EsPerioderMedInaktivitet(
                toDate(LocalDate.now().minusYears(minimumHullVarighetAntallÅr).plusDays(1)),
                listOf(toDate(LocalDate.now().minusYears(8)))
            )
        )
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        val actual = sokClient.veilederSok(søkekriterierHullICv).cver

        assertThat(actual).hasSize(0)
    }

    @Test
    fun harIkkeHullFordiLangInaktivPeriodeErForGammel() {
        val cv = giveMeEsCv(
            "9846517",
            EsPerioderMedInaktivitet(
                toDate(LocalDate.now().minusYears(1)),
                listOf(toDate(LocalDate.now().minusYears(8)))
            )
        )
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        val actual = sokClient.veilederSok(søkekriterierHullICv).cver

        assertThat(actual).hasSize(0)
    }

    @Test
    fun harHullHvisKandidatenHarVærtInaktivLenge() {
        val cv = giveMeEsCv(
            "9846517",
            EsPerioderMedInaktivitet(
                toDate(LocalDate.now().minusYears(2)),
                listOf(toDate(LocalDate.now().minusYears(5)))
            )
        )
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        val actual = sokClient.veilederSok(søkekriterierHullICv).cver

        assertThat(actual).hasSize(1)
        assertThat(actual.first().aktorId).isEqualTo(cv!!.aktorId)
    }

    @Test
    fun harHullHvisNyligInaktivMenHarLangNokInaktivPeriodeFraTidligere() {
        val cv = giveMeEsCv(
            "213548",
            EsPerioderMedInaktivitet(
                toDate(LocalDate.now().minusDays(2)),
                listOf(toDate(LocalDate.now().minusYears(1)))
            )
        )
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        val actual = sokClient.veilederSok(søkekriterierHullICv).cver

        assertThat(actual).hasSize(1)
        assertThat(actual.first().aktorId).isEqualTo(cv!!.aktorId)
    }

    @Test
    fun harHullHvisAldriVærtIAktivitet() {
        val cv = giveMeEsCv(
            "213548",
            EsPerioderMedInaktivitet(
                null,
                null
            )
        )
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        val actual = sokClient.veilederSok(søkekriterierHullICv).cver

        assertThat(actual).hasSize(1)
        assertThat(actual.first().aktorId).isEqualTo(cv!!.aktorId)
    }

    private fun toDate(localDate: LocalDate): Date? {
        return Date(localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
    }

    private fun giveMeEsCv(aktorId: String, esPerioderMedInaktivitet: EsPerioderMedInaktivitet): EsCv? {
        val esCv = EsCv(
            "96541328",
            "01016012345",
            "JENS",
            "NORDMANN",
            Date.from(Instant.now()),
            false,
            "JOBBS",
            "måpussetenna@mamma.com",
            "(+47) 22334455",
            "12345678",
            "NO",
            "1L",
            "hererjeg",
            "N",
            Date.from(Instant.now()),
            "Minvei 1",
            "",
            "",
            "0654",
            "OSLO",
            "NO",
            5001,
            false,
            Date.from(Instant.now()),
            301,
            false,
            null,
            "IKVAL",
            null,
            "0220 NAV Asker",
            false,
            false,
            true,
            false,
            "LEDIG_NAA",
            "5001",
            "H149390",
            false,
            "Viken",
            "Lier"
        )
        esCv.addPerioderMedInaktivitet(esPerioderMedInaktivitet)
        return esCv
    }
}
