package no.nav.arbeid.cv.kandidatsok.es;

import org.junit.Test;

public class RegexTest {

    @Test
    public void test() {

        String geografi = "NO03.0317";
        String[] geografiKoder = geografi.split("\\.");
        String regex ="";

        if(geografiKoder.length == 1){
            String fylkeskode = geografiKoder[0].substring(2,4);
            regex += fylkeskode + ".*";
        }
        else{
            regex += geografiKoder[1];
        }


        String i =" ";

    }



}
