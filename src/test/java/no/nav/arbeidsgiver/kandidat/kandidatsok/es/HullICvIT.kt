package no.nav.arbeidsgiver.kandidat.kandidatsok.es

import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.EsCvObjectMother.*
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsForerkort
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsPerioderMedInaktivitet
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*
import java.util.Arrays.asList

@ExtendWith(ElasticSearchIntegrationTestExtension::class)
class HullICvIT {

    private val sokClient =
        ElasticSearchTestConfiguration.esSokService(DEFAULT_INDEX_NAME)

    private val indexerClient = ElasticSearchTestConfiguration.indexerCvService()

    private val minimumHullVarighetAntallÅr = 2L

    @BeforeEach
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

        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isFalse
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
        
        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isFalse
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

        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isFalse
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
        indexerClient.bulkIndex(listOf(cv), DEFAULT_INDEX_NAME)

        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isTrue
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
        assertThat(cv.forerkort).isNotEmpty
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isTrue
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

        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isTrue
    }

    @Test
    fun harIkkeHullHvisAldriVærtIAktivitetOgTomCv() {
        val cv = giveMeTomCv(
            "213548",
            EsPerioderMedInaktivitet(
                null,
                null
            )
        )
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isFalse
    }

    @Test
    fun kravOmAtCvSkalVæreUtfyltErOppfyltFordiHarYrkeserfaring() {
        val cv = giveMeCvSomIkkeErUtfyltOgHarLangInaktivitet()
        val anyYrkeserfaring = giveMeYrkeserfaring()
        cv.addYrkeserfaring(anyYrkeserfaring)
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isTrue
    }

    @Test
    fun kravOmAtCvSkalVæreUtfyltErOppfyltFordiHarUtdanning() {
        val cv = giveMeCvSomIkkeErUtfyltOgHarLangInaktivitet()
        val anyUtdanning = giveMeUtdanning()
        cv.addUtdanning(anyUtdanning)
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isTrue
    }

    @Test
    fun kravOmAtCvSkalVæreUtfyltErOppfyltFordiHarFørerkort() {
        val cv = giveMeCvSomIkkeErUtfyltOgHarLangInaktivitet()
        val anyFørerkort = giveMeFørerkort()
        cv.addForerkort(anyFørerkort)
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isTrue
    }

    @Test
    fun kravOmAtCvSkalVæreUtfyltErOppfyltFordiHarKurs() {
        val cv = giveMeCvSomIkkeErUtfyltOgHarLangInaktivitet()
        val anyKurs = giveMeKurs()
        cv.addKurs(anyKurs)
        indexerClient.index(cv, DEFAULT_INDEX_NAME)
        
        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isTrue
    }

    @Test
    fun kravOmAtCvSkalVæreUtfyltErOppfyltFordiHarFagdokumentasjon() {
        val cv = giveMeCvSomIkkeErUtfyltOgHarLangInaktivitet()
        val anyFagdokumentasjon = giveMeFagdokumentasjon()
        cv.addFagdokumentasjon(asList(anyFagdokumentasjon))
        indexerClient.index(cv, DEFAULT_INDEX_NAME)
        
        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isTrue
    }

    @Test
    fun kravOmAtCvSkalVæreUtfyltErOppfyltFordiHarAnnenErfaring() {
        val cv = giveMeCvSomIkkeErUtfyltOgHarLangInaktivitet()
        val anyAnnenErfaring = giveMeAnnenErfaring()
        cv.addAnnenErfaring(asList(anyAnnenErfaring))
        indexerClient.index(cv, DEFAULT_INDEX_NAME)

        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isTrue
    }

    @Test
    fun kravOmAtCvSkalVæreUtfyltErOppfyltFordiHarGodkjenninger() {
        val cv = giveMeCvSomIkkeErUtfyltOgHarLangInaktivitet()
        val anyGodkjenning = giveMeGodkjenning()
        cv.addGodkjenninger(asList(anyGodkjenning))
        indexerClient.index(cv, DEFAULT_INDEX_NAME)
        
        assertThat(sokClient.harHullICv(cv.aktorId, LocalDate.now())).isTrue
    }

    @Test
    fun sjekkOmPersonHarHullICv() {
        val aktorId = "9846517"
        val tidspunkt = LocalDate.of(2018, 4, 7)
        val cvMedHull = giveMeEsCv(
            aktorId,
            EsPerioderMedInaktivitet(
                toDate(tidspunkt),
                listOf(toDate(tidspunkt.minusYears(2)))
            )
        )
        indexerClient.bulkIndex(listOf(cvMedHull), DEFAULT_INDEX_NAME)

        val actual = sokClient.harHullICv(aktorId, tidspunkt);

        assertThat(actual).isTrue
    }

    @Test
    fun sjekkOmPersonIkkeHarHullICv() {
        val aktorId = "9846517"
        val cvMedHull = giveMeEsCv(
            aktorId,
            EsPerioderMedInaktivitet(
                toDate(LocalDate.now()),
                emptyList()
            )
        )

        indexerClient.bulkIndex(listOf(cvMedHull), DEFAULT_INDEX_NAME)

        val actual = sokClient.harHullICv(aktorId, LocalDate.now());

        assertThat(actual).isFalse
    }

    @Test
    fun sjekkAtViIkkevetOmPersonHarHullICvNårPersonIkkeFinnesIEs() {
        val aktorId = "0"

        val actual = sokClient.harHullICv(aktorId, LocalDate.now());

        assertThat(actual == null).isTrue
    }

    private fun toDate(localDate: LocalDate): Date? {
        return Date(localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
    }

    private fun giveMeCvSomIkkeErUtfyltOgHarLangInaktivitet() = giveMeTomCv(
        "1234",
        EsPerioderMedInaktivitet(
            null,
            null
        )
    )

    private fun giveMeEsCv(aktorId: String, esPerioderMedInaktivitet: EsPerioderMedInaktivitet): EsCv =
        giveMeTomCv(aktorId, esPerioderMedInaktivitet).apply {
            addForerkort(
                listOf(
                    EsForerkort(
                        toDate(LocalDate.of(1996, 2, 1)),
                        toDate(LocalDate.of(3050, 2, 1)),
                        "V1.6145",
                        "T - Traktor",
                        null,
                        ""
                    )
                )
            )

        }


    private fun giveMeTomCv(aktorId: String, esPerioderMedInaktivitet: EsPerioderMedInaktivitet): EsCv =
        EsCv(
            aktorId,
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
            "0220",
            false,
            false,
            true,
            true,
            "LEDIG_NAA",
            "5001",
            "H149390",
            "Viken",
            "Lier"
        ).apply {
            addPerioderMedInaktivitet(esPerioderMedInaktivitet)
        }
}
