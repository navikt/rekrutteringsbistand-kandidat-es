package no.nav.arbeid.cv.kandidatsok.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ShutdownStrategy;
import com.palantir.docker.compose.connection.DockerMachine;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import no.nav.arbeid.cv.indexer.config.EsServiceConfig;
import no.nav.arbeid.cv.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeid.cv.kandidatsok.domene.es.KandidatsokTransformer;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Aggregering;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokekriterier;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokeresultat;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeid.kandidatsok.es.client.EsSokService;
import org.apache.http.HttpHost;
import org.assertj.core.api.Assertions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class IndexCvTest {

    /*
     * For å kunne kjøre denne testen må Linux rekonfigureres litt.. Lag en fil i
     * /etc/sysctl.d/01-increase_vm_max_map_count.conf som inneholder følgende: vm.max_map_count = 262144
     */

    // Kjører "docker-compose up" manuelt istedenfor denne ClassRule:

    @ClassRule
    public static DockerComposeRule docker =
            DockerComposeRule.builder().file("src/test/resources/docker-compose-kun-es.yml")
                    .machine(DockerMachine.localMachine()
                            .withAdditionalEnvironmentVariable("ES_PORT",
                                    System.getProperty("ES_PORT"))
                            .build())
                    .shutdownStrategy(ShutdownStrategy.KILL_DOWN).build();

    @Autowired
    private EsSokService sokClient;

    @Autowired
    private EsIndexerService indexerClient;

    @Autowired
    private ObjectMapper objectMapper;

    private KandidatsokTransformer kandidatsokTransformer = new KandidatsokTransformer();

    @Configuration
    @Import({EsServiceConfig.class})
    static class TestConfig {

        @Bean
        @Autowired
        public RestHighLevelClient restHighLevelClient(@Value("${ES_PORT}") Integer port) {
            return new RestHighLevelClient(
                    RestClient.builder(new HttpHost("localhost", port, "http")));
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        public MeterRegistry meterRegistry() {
            Counter counter = mock(Counter.class);
            MeterRegistry meterRegistry = mock(MeterRegistry.class);
            when(meterRegistry.counter(anyString(), any(Tags.class))).thenReturn(counter);

            return meterRegistry;
        }
    }

    @Before
    public void before() throws IOException {
        try {
            indexerClient.deleteIndex();
        } catch (Exception e) {
            // Ignore
        }

        indexerClient.createIndex();

        indexerClient.index(EsCvObjectMother.giveMeEsCv());
        indexerClient.index(EsCvObjectMother.giveMeEsCv2());
        indexerClient.index(EsCvObjectMother.giveMeEsCv3());
        indexerClient.index(EsCvObjectMother.giveMeEsCv4());
        indexerClient.index(EsCvObjectMother.giveMeEsCv5());
        indexerClient.index(EsCvObjectMother.giveMeEsCv6());
    }

    @After
    public void after() throws IOException {
        indexerClient.deleteIndex();
    }

    @Test
    public void test() throws IOException {
        Sokeresultat sokeres =
                sokClient.sok(Sokekriterier.med().etternavn("NORDMANN").nusKode("355211").bygg());
        List<EsCv> list = sokeres.getCver();
        List<Aggregering> aggregeringer = sokeres.getAggregeringer();

        assertThat(list.size()).isEqualTo(1);
        EsCv esCv = list.get(0);
        assertThat(esCv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv()));
    }

    @Test
    public void skalIkkeFeileMedIllegalArgumentFraES() throws Exception {
        no.nav.arbeid.cv.kandidatsok.es.domene.EsCv cv1 =
                objectMapper.readValue(
                        new InputStreamReader(
                            getClass().getResourceAsStream("/utfordrende_cv1.json"), "ISO-8859-1"),
                        no.nav.arbeid.cv.kandidatsok.es.domene.EsCv.class);
        no.nav.arbeid.cv.kandidatsok.es.domene.EsCv cv2 =
                objectMapper.readValue(
                        new InputStreamReader(
                                getClass().getResourceAsStream("/utfordrende_cv2.json"), "ISO-8859-1"),
                        no.nav.arbeid.cv.kandidatsok.es.domene.EsCv.class);

        List<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> bulkEventer =
                asList(cv1, cv2);

        int antallIndeksert = indexerClient.bulkIndex(bulkEventer);
        assertThat(antallIndeksert).isEqualTo(bulkEventer.size());
    }

    @Test
    @Ignore("Brukes til utforskende testing")
    public void skalLoggFeilVedBulkIndeksereCVMedNullfelter() throws Exception {
        List<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> bulkEventer =
                asList(EsCvObjectMother.giveMeEsCv(),
                        EsCvObjectMother.giveMeEsCvMedFeil(),
                        EsCvObjectMother.giveMeEsCv2());

        bulkEventer.forEach(e -> e.setArenaPersonId(e.getArenaPersonId() + 9998));
        indexerClient.bulkIndex(bulkEventer);

    }

    @Test
    @Ignore("Dette gjelder ikke lenger når vi splitter prosjektet!")
    public void skalOppretteIndexHvisDenIkkeFinnes() throws IOException {
        indexerClient.deleteIndex();
        Sokeresultat sokeres =
                sokClient.sok(Sokekriterier.med().etternavn("NORDMANN").nusKode("355211").bygg());
        List<EsCv> list = sokeres.getCver();
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    public void testUtenSokekriterierReturnererAlleTestPersoner() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(Sokekriterier.med().bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(6);
    }

    @Test
    public void testFlereInputFritekstGirBredereResultat() throws IOException {
        Sokeresultat sokeresultat1 =
                sokClient.sok(Sokekriterier.med().fritekst("javautvikler").bygg());
        Sokeresultat sokeresultat = sokClient
                .sok(Sokekriterier.med().fritekst("industrimekaniker javautvikler").bygg());

        List<EsCv> cver1 = sokeresultat1.getCver();
        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver1.size()).isLessThan(cver.size());
        assertThat(cver1.size()).isEqualTo(1);
        assertThat(cver.size()).isEqualTo(4);
    }

    @Test
    public void testSokPaNorskeStoppordGirIkkeResultat() throws IOException {
        Sokeresultat sokeresultatYrke = sokClient
                .sok(Sokekriterier.med().stillingstitler(Collections.singletonList("og")).bygg());
        Sokeresultat sokeresultatKomp = sokClient
                .sok(Sokekriterier.med().kompetanser(Collections.singletonList("og")).bygg());
        Sokeresultat sokeresultatUtdanning = sokClient
                .sok(Sokekriterier.med().utdanninger(Collections.singletonList("og")).bygg());
        Sokeresultat sokeresultatFritekst =
                sokClient.sok(Sokekriterier.med().fritekst("og").bygg());

        List<EsCv> cverYrke = sokeresultatYrke.getCver();
        List<EsCv> cverKomp = sokeresultatKomp.getCver();
        List<EsCv> cverUtdanning = sokeresultatUtdanning.getCver();
        List<EsCv> cverFritekst = sokeresultatFritekst.getCver();

        assertThat(cverYrke.size()).isEqualTo(0);
        assertThat(cverKomp.size()).isEqualTo(0);
        assertThat(cverUtdanning.size()).isEqualTo(0);
        assertThat(cverFritekst.size()).isEqualTo(0);
    }

    @Test
    public void testSokMedFlereKriterierGirSvarMedAlleFelter() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(
                Sokekriterier.med().stillingstitler(Collections.singletonList("Butikkmedarbeider"))
                        .kompetanser(Collections.singletonList("Hallovert"))
                        .utdanninger(Collections.singletonList("Bygg og anlegg")).bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(1);
        EsCv cv = cver.get(0);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv4()));
    }

    @Test
    public void testFlereInputYrkeGirMindreTreff() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(Sokekriterier.med()
                .stillingstitler(Collections.singletonList("Industrimekaniker")).bygg());
        Sokeresultat sokeresultat2 = sokClient.sok(Sokekriterier.med()
                .stillingstitler(asList("Progger", "Industrimekaniker")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
    }

    @Test
    public void testFlereInputKompetanseGirMindreTreff() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(Sokekriterier.med()
                .kompetanser(Collections.singletonList("Programvareutvikler")).bygg());
        Sokeresultat sokeresultat2 = sokClient.sok(Sokekriterier.med()
                .kompetanser(asList("Programvareutvikler", "Nyhetsanker")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
    }

    @Test
    public void testFlereInputUtdanningGirMindreTreff() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(Sokekriterier.med()
                .utdanninger(Collections.singletonList("Bygg og anlegg")).bygg());
        Sokeresultat sokeresultat2 = sokClient.sok(Sokekriterier.med()
                .utdanninger(asList("Bygg og anlegg", "master i sikkerhet")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
    }

    @Test
    public void testStemOrdSkalGiSammeResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(
                Sokekriterier.med().stillingstitler(Collections.singletonList("Progger")).bygg());
        Sokeresultat sokeresultatStemOrd = sokClient.sok(
                Sokekriterier.med().stillingstitler(Collections.singletonList("Progg")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cverStemOrd = sokeresultatStemOrd.getCver();

        assertThat(cver.size()).isEqualTo(cverStemOrd.size());
        assertThat(cver.get(0)).isEqualTo(cverStemOrd.get(0));
    }

    @Test
    public void testSokPaStyrkKode() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(Sokekriterier.med().styrkKode("5684.05").bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
    }

    @Test
    public void testSokPaNusKode() throws IOException {
        Sokeresultat sokeresultatAlle = sokClient.sok(Sokekriterier.med().bygg());
        assertThat(sokeresultatAlle.getCver()).hasSize(6);

        Sokeresultat sokeresultat = sokClient.sok(Sokekriterier.med().nusKode("355211").bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);
        assertThat(cver).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv()));
    }

    @Test
    public void testSokPaFlereStyrkKoderGirBegrensendeResultat() throws IOException {
        List<String> styrkKoder = new ArrayList<>();
        styrkKoder.add("5684.05");

        Sokeresultat sokeresultat =
                sokClient.sok(Sokekriterier.med().styrkKoder(styrkKoder).bygg());

        styrkKoder.add("5124.46");

        Sokeresultat sokeresultatToKoder =
                sokClient.sok(Sokekriterier.med().styrkKoder(styrkKoder).bygg());

        styrkKoder.add("5746.07");

        Sokeresultat sokeresultatTreKoder =
                sokClient.sok(Sokekriterier.med().styrkKoder(styrkKoder).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultatToKoder.getCver();
        List<EsCv> cver3 = sokeresultatTreKoder.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
        assertThat(cver2.size()).isGreaterThan(cver3.size());
    }

    @Test
    public void testSokPaFlereNusKoderGirBegrensendeResultat() throws IOException {
        List<String> nusKoder = new ArrayList<>();
        nusKoder.add("296647");

        Sokeresultat sokeresultat = sokClient.sok(Sokekriterier.med().nusKoder(nusKoder).bygg());

        nusKoder.add("456375");

        Sokeresultat sokeresultatToKoder =
                sokClient.sok(Sokekriterier.med().nusKoder(nusKoder).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultatToKoder.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());

    }

    @Test
    public void testSamletKompetanseSkalIkkeGiResultatVedSokPaSprak() throws IOException {
        Sokeresultat sokeresultat = sokClient
                .sok(Sokekriterier.med().kompetanser(Collections.singletonList("Dansk")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(0);
    }

    @Test
    public void testSokPaSprak() throws IOException {
        Sokeresultat sokeresultat =
                sokClient.sok(Sokekriterier.med().sprak(Collections.singletonList("Dansk")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));
    }

    @Test
    public void testSamletKompetanseSkalGiResultatVedSokPaSertifikater() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(Sokekriterier.med()
                .kompetanser(Collections.singletonList("Truckførerbevis")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
    }

    @Test
    public void testSamletKompetanseSkalGiResultatVedSokPaForerkort() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(
                Sokekriterier.med().kompetanser(Collections.singletonList("Traktorlappen")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
    }

    @Test
    @Ignore
    public void testSamletKompetanseSkalGiResultatVedSokPaKurs() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(
                Sokekriterier.med().kompetanser(Collections.singletonList("Spring Boot")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));
    }

    @Test
    public void testSamletKompetanseSkalGiResultatVedSokPaKompetanse() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(
                Sokekriterier.med().kompetanser(Collections.singletonList("Javautvikler")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void testSokPaFlereGeografiJobbonskerGirBegrensendeResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(
                Sokekriterier.med().geografiList(Collections.singletonList("NO34.3434.1")).bygg());
        Sokeresultat sokeresultat2 = sokClient.sok(
                Sokekriterier.med().geografiList(asList("NO34.3434.1", "NO21.2020")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
    }

    @Test
    public void testSokPaBostedGirBegrensendeResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(
                Sokekriterier.med().geografiList(Collections.singletonList("NO34.3434.1")).bygg());
        Sokeresultat sokeresultat2 = sokClient.sok(
                Sokekriterier.med().geografiList(asList("NO34.3434")).maaBoInnenforGeografi(true).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
    }

    @Test
    public void testPaTotalYrkeserfaringSkalGiKorrektResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(
                Sokekriterier.med().totalYrkeserfaring(Collections.singletonList("37-72")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokPaFlereUtdanningsnivaSkalGiFlereResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(
                Sokekriterier.med().utdanningsniva(Collections.singletonList("Master")).bygg());
        Sokeresultat sokeresultat1 = sokClient.sok(
                Sokekriterier.med().utdanningsniva(asList("Master", "Fagskole")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver1 = sokeresultat1.getCver();

        assertThat(cver.size()).isLessThan(cver1.size());
    }

    @Test
    public void sokPaVideregaendeSkalGiTreffPaKorrektKompetanse() throws IOException {
        Sokeresultat sokeresultatVideregaende = sokClient.sok(Sokekriterier.med()
                .utdanningsniva(Collections.singletonList("Videregaende")).bygg());

        List<EsCv> cverVideregaende = sokeresultatVideregaende.getCver();

        // assertThat(cverVideregaende).contains(kandidatsokTransformer
        // .transformer(transformer.transform(TempCvEventObjectMother.giveMeEsCv2())));
    }

    @Test
    public void sokPaIngenUtdanningSkalGiKorrektResultat() throws IOException {
        Sokeresultat sokeresultatIngen = sokClient
                .sok(Sokekriterier.med().utdanningsniva(Collections.singletonList("Ingen")).bygg());
        Sokeresultat sokeresultatIngenOgGrunnskole = sokClient
                .sok(Sokekriterier.med().utdanningsniva(asList("Ingen", "Master")).bygg());

        List<EsCv> cverIngen = sokeresultatIngen.getCver();
        List<EsCv> cverIngenOgGrunnskole = sokeresultatIngenOgGrunnskole.getCver();

        // assertThat(cverIngen).contains(kandidatsokTransformer
        // .transformer(transformer.transform(TempCvEventObjectMother.giveMeEsCv5())));
        // assertThat(cverIngen).contains(kandidatsokTransformer
        // .transformer(transformer.transform(TempCvEventObjectMother.giveMeEsCv2())));
        assertThat(cverIngen.size()).isLessThan(cverIngenOgGrunnskole.size());
    }

    @Test
    public void skalBulkIndeksereCVerIdempotent() throws Exception {
        List<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> bulkEventer =
                asList(EsCvObjectMother.giveMeEsCv(), EsCvObjectMother.giveMeEsCv2(),
                        EsCvObjectMother.giveMeEsCv3(), EsCvObjectMother.giveMeEsCv4(),
                        EsCvObjectMother.giveMeEsCv5());

        bulkEventer.forEach(e -> e.setArenaPersonId(e.getArenaPersonId() + 9999));

        int antallForBulkIndeksering = sokClient.sok(Sokekriterier.med().bygg()).getCver().size();
        indexerClient.bulkIndex(bulkEventer);
        int antallEtterIndeksering = sokClient.sok(Sokekriterier.med().bygg()).getCver().size();

        Assertions.assertThat(antallEtterIndeksering - antallForBulkIndeksering)
                .isEqualTo(bulkEventer.size());

        // Reindekser
        indexerClient.bulkIndex(bulkEventer);
        antallEtterIndeksering = sokClient.sok(Sokekriterier.med().bygg()).getCver().size();

        Assertions.assertThat(antallEtterIndeksering - antallForBulkIndeksering)
                .isEqualTo(bulkEventer.size());
    }

    @Test
    public void skalBulkSletteCVer() throws Exception {
        List<Long> sletteIder = asList(EsCvObjectMother.giveMeEsCv().getArenaPersonId(),
                EsCvObjectMother.giveMeEsCv2().getArenaPersonId());

        int antallForBulkSletting = sokClient.sok(Sokekriterier.med().bygg()).getCver().size();
        indexerClient.bulkSlett(sletteIder);
        int antallEtterSletting = sokClient.sok(Sokekriterier.med().bygg()).getCver().size();

        Assertions.assertThat(antallForBulkSletting - antallEtterSletting)
                .isEqualTo(sletteIder.size());
    }

    @Test
    public void sokPaYrkeJobbonskerSkalGiKorrektResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(Sokekriterier.med()
                .yrkeJobbonsker(Collections.singletonList("Lastebilsjåfør")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));
    }

    @Test
    public void sokPaFlereYrkeJobbonskerSkalGiUtvidendeResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(Sokekriterier.med()
                .yrkeJobbonsker(Collections.singletonList("Butikkmedarbeider")).bygg());
        Sokeresultat sokeresultat1 = sokClient.sok(Sokekriterier.med()
                .yrkeJobbonsker(Arrays.asList("Butikkmedarbeider", "Ordfører")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver1 = sokeresultat1.getCver();
        assertThat(cver.size()).isLessThan(cver1.size());
    }

    @Test
    public void skalKunneIndeksereOppCvUtenKompetanser() throws IOException {
        indexerClient.index(EsCvObjectMother.giveMeCvUtenKompetanse());
    }

    @Test
    public void hentKandidaterBevarerRekkefolge() throws IOException {
        String KANDIDATNUMMER1 = "N883773";
        String KANDIDATNUMMER2 = "S221234";
        String KANDIDATNUMMER3 = "H738234";
        List<String> kandidatnummer1 = asList(KANDIDATNUMMER1, KANDIDATNUMMER2, KANDIDATNUMMER3);
        List<String> kandidatnummer2 = asList(KANDIDATNUMMER3, KANDIDATNUMMER1, KANDIDATNUMMER2);

        Sokeresultat resultat1 = sokClient.hentKandidater(kandidatnummer1);
        List<String> resultatkandidatnummer1 = resultat1.getCver().stream()
                .map(EsCv::getArenaKandidatnr)
                .collect(Collectors.toList());
        assertThat(resultatkandidatnummer1).isEqualTo(kandidatnummer1);

        Sokeresultat resultat2 = sokClient.hentKandidater(kandidatnummer2);
        List<String> resultatkandidatnummer2 = resultat2.getCver().stream()
                .map(EsCv::getArenaKandidatnr)
                .collect(Collectors.toList());
        assertThat(resultatkandidatnummer2).isEqualTo(kandidatnummer2);
    }

    @Test
    public void hentKandidaterHandtererIkkeeksisterendeKandidatnummer() throws IOException {
        String KANDIDATNUMMER1 = "N883773";
        String KANDIDATNUMMER2 = "S221234";
        List<String> kandidatnummer = asList(KANDIDATNUMMER1, "IKKEEKSISTERNDE_KANDIDATNUMMER", KANDIDATNUMMER2);

        Sokeresultat resultat = sokClient.hentKandidater(kandidatnummer);
        List<String> resultatkandidatnummer = resultat.getCver().stream()
                .map(EsCv::getArenaKandidatnr)
                .collect(Collectors.toList());
        assertThat(resultatkandidatnummer).isEqualTo(asList(KANDIDATNUMMER1, KANDIDATNUMMER2));
    }

    @Test
    public void sokMedIngenUtdanningSkalGiFlerResultaterSelvOmManSpesifisererUtdanning() throws IOException {
        Sokeresultat sokeresultatVideregaende = sokClient
            .sok(Sokekriterier.med()
                .utdanningsniva(Collections.singletonList("Videregaende"))
                .utdanninger(Collections.singletonList("Bygg og anlegg"))
                .bygg());

        Sokeresultat sokeresultatVideregaendeOgIngenUtdanning = sokClient
            .sok(Sokekriterier.med()
                .utdanningsniva(asList("Ingen", "Videregaende"))
                .utdanninger(Collections.singletonList("Bygg og anlegg"))
                .bygg());

        List<EsCv> cverVideregaende = sokeresultatVideregaende.getCver();
        List<EsCv> cverVideregaendeOgIngenUtdanning = sokeresultatVideregaendeOgIngenUtdanning.getCver();
        assertThat(cverVideregaende.size()).isLessThan(cverVideregaendeOgIngenUtdanning.size());
    }

    @Test
    public void sokMedDoktorgradSkalIkkeGiResulatMedKandidaterUtenDoktorgrad() throws IOException {
        Sokeresultat sokeresultatDoktorgrad = sokClient
                .sok(Sokekriterier.med().utdanningsniva(Collections.singletonList("Doktorgrad")).bygg());

        assertThat(sokeresultatDoktorgrad.getCver()).hasSize(1);
    }

    @Test
    public void sokMedFraTilSkalReturnereRiktigAntallKandidater() throws IOException {
        Sokeresultat sokeresultat = sokClient.sok(Sokekriterier.med().antallResultater(1).bygg());
        assertThat(sokeresultat.getCver()).hasSize(1);

        //fra og med tredje posisjon
        //av totalt 6 i indexen
        Sokeresultat sokeresultat2 = sokClient.sok(Sokekriterier.med().fraIndex(2).antallResultater(5).bygg());
        assertThat(sokeresultat2.getCver()).hasSize(4);
    }
}
