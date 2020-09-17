package no.nav.arbeidsgiver.kandidat.indexer.config;

import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Map;


/**
 * Laster ES mapping og settings fra JSON-ressurser i classpath.
 */
public class StaticMappingProvider {

    public static final String CV_SETTINGS_CONFIG = "/no/nav/arbeidsgiver/cv/indexer/config/cv_settings.json";

    public static final String CV_MAPPING_CONFIG = "/no/nav/arbeidsgiver/cv/indexer/config/cv_mapping.json";

    public static Map<String, Object> cvSettings() throws IOException {
        XContentParser parser = XContentType.JSON.xContent().createParser(NamedXContentRegistry.EMPTY, null,
                StaticMappingProvider.class.getResourceAsStream(CV_SETTINGS_CONFIG));

        return parser.map();
    }

    public static Map<String, Object> cvMapping() throws IOException {
        XContentParser parser = XContentType.JSON.xContent().createParser(NamedXContentRegistry.EMPTY, null,
                StaticMappingProvider.class.getResourceAsStream(CV_MAPPING_CONFIG));

        return parser.map();
    }

}
