package no.nav.arbeid.cv.kandidatsok.es;

import org.junit.Test;

public class RegexTest {

    @Test
    public void test() {

        String geografi = "NO";
        String[] geografiKoder = geografi.split("\\.");
        String regex = "";

        if (geografiKoder.length == 1) {

            if (geografiKoder[0].length() < 4) {
                return;
            }

            if (geografiKoder[0].startsWith("NO0")) {
                String fylkeskode = geografiKoder[0].substring(3, 4);
                regex += fylkeskode + ".*";
            } else {
                String fylkeskode = geografiKoder[0].substring(2, 4);
                regex += fylkeskode + ".*";
            }

        } else {
            if (geografiKoder[1].startsWith("0")) {
                regex += geografiKoder[1].substring(1, 4);
            } else {
                regex += geografiKoder[1].substring(0, 4);
            }

        }


        String i = " ";

    }


}
