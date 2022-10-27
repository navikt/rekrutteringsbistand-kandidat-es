package no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport

import org.apache.http.HttpHost
import org.opensearch.client.RequestOptions
import org.opensearch.client.RestClient
import org.opensearch.client.RestHighLevelClient
import org.opensearch.client.core.CountRequest

class TestSøkKlient {

    private val highLevelClient = RestHighLevelClient(
        RestClient.builder(
            HttpHost("localhost", ElasticSearchIntegrationTestExtension.getEsPort(), "http")
        )
    )

    fun antallKandidater(indexNavn: String): Int {
        val countRequest = CountRequest().apply { indices(indexNavn) }
        return highLevelClient.count(countRequest, RequestOptions.DEFAULT).count.toInt()
    }
}