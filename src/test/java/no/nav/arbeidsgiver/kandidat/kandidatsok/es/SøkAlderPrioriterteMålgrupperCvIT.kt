package no.nav.arbeidsgiver.kandidat.kandidatsok.es

import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.SokekriterierVeiledere
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME
import no.nav.arbeidsgiver.kandidatsok.es.client.PrioritertMålgruppe
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.ZoneId
import java.util.*

@ExtendWith(ElasticSearchIntegrationTestExtension::class)
class SøkAlderPrioriterteMålgrupperCvIT {

    private val anyAktorId = "100001000"
    private val anyAktorId2 = "100001001"

    private val sokClient =
        ElasticSearchTestConfiguration.esSokService(DEFAULT_INDEX_NAME)

    private val indexerClient = ElasticSearchTestConfiguration.indexerCvService()

    @BeforeEach
    fun before() {
        indexerClient.createIndex(DEFAULT_INDEX_NAME)
    }

    @AfterEach
    fun after() {
        indexerClient.deleteIndex(DEFAULT_INDEX_NAME)
    }

    @Test
    fun kandidatSomHarFyllt50årErSenior() {
        val cv = giveMeEsCv(anyAktorId, now().minusYears(50).minusDays(1))
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        val actual = sokClient.veilederSok(
            SokekriterierVeiledere.med().prioriterteMaalgrupper(PrioritertMålgruppe.senior).bygg()
        ).cver

        assertThat(actual).hasSize(1)
    }

    @Test
    fun kandidatSomFyller50årErSenior() {
        val cv = giveMeEsCv(anyAktorId, now().minusYears(50))
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        val actual = sokClient.veilederSok(
            SokekriterierVeiledere.med().prioriterteMaalgrupper(PrioritertMålgruppe.senior).bygg()
        ).cver

        assertThat(actual).hasSize(1)
    }

    @Test
    fun kandidatUnder50årErIkkeSenior() {

        val cv = giveMeEsCv(anyAktorId, now().minusYears(50).plusDays(1))
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        val actual = sokClient.veilederSok(
            SokekriterierVeiledere.med().prioriterteMaalgrupper(PrioritertMålgruppe.senior).bygg()
        ).cver

        assertThat(actual).hasSize(0)
    }

    @Test
    fun kandidatUnder30årErUng() {
        val cv = giveMeEsCv(anyAktorId, now().minusYears(30).plusDays(1))
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        val actual = sokClient.veilederSok(
            SokekriterierVeiledere.med().prioriterteMaalgrupper(PrioritertMålgruppe.ung).bygg()
        ).cver

        assertThat(actual).hasSize(1)
    }

    @Test
    fun kandidatSomFyller30årErIkkeUng() {
        val cv = giveMeEsCv(anyAktorId, now().minusYears(30))
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        val actual = sokClient.veilederSok(
            SokekriterierVeiledere.med().prioriterteMaalgrupper(PrioritertMålgruppe.ung).bygg()
        ).cver

        assertThat(actual).hasSize(0)
    }

    @Test
    fun kandidatSomHarFyllt30årErIkkeUng() {
        val cv = giveMeEsCv(anyAktorId, now().minusYears(30).minusDays(1))
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        val actual = sokClient.veilederSok(
            SokekriterierVeiledere.med().prioriterteMaalgrupper(PrioritertMålgruppe.ung).bygg()
        ).cver

        assertThat(actual).hasSize(0)
    }

    @Test
    fun kandidatSomErOver50OgKanidatSomErUnder30VisesOmBeggeKrysseneErAvkrysset() {
        val under30 = giveMeEsCv(anyAktorId, now().minusYears(20))
        // kandidatnr mappes til es _id og må være unikt for å unngå overskriving
        val over50 = giveMeEsCv(anyAktorId2, now().minusYears(60)).apply { kandidatnr = "2" }
        indexerClient.index(under30, DEFAULT_INDEX_NAME)
        indexerClient.index(over50, DEFAULT_INDEX_NAME)

        val actual = sokClient.veilederSok(
            SokekriterierVeiledere.med()
                .prioriterteMaalgrupper(PrioritertMålgruppe.ung, PrioritertMålgruppe.senior)
                .bygg()
        ).cver

        assertThat(actual).hasSize(2)
    }


    private fun giveMeEsCv(aktorId: String, fodselsDato: LocalDate): EsCv = EsCv(
        aktorId,
        "01016012345",
        "JENS",
        "NORDMANN",
        toDate(fodselsDato),
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
        true,
        "LEDIG_NAA",
        "5001",
        "H149390",
        false,
        "Viken",
        "Lier"
    )

    private fun toDate(localDate: LocalDate): Date? {
        return Date(localDate.atStartOfDay().atZone(ZoneId.of("Europe/Oslo")).toInstant().toEpochMilli())
    }


}
