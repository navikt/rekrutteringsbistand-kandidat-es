package no.nav.arbeid.kandidatsok.es.helper;

public class KandidatnrHelper {

    public static String toKandidatnr(Long personId) {

        String vTempId = String.format("%08d", personId);

        return new StringBuilder()
                .append(toChar(toNumber(vTempId.charAt(3)) + 17))
                .append(toChar(toNumber(vTempId.charAt(4)) + 18))
                .append(vTempId.substring(2, 3))
                .append(vTempId.substring(5, 6))
                .append(vTempId.substring(1, 2))
                .append(vTempId.substring(6, 7))
                .append(vTempId.substring(0, 1))
                .append(vTempId.substring(7, 8)).toString();
    }

    private static char toChar(int intValue) {
        return (char) intValue;
    }

    private static int toNumber(char charValue) {
        return (int) charValue;
    }
}
