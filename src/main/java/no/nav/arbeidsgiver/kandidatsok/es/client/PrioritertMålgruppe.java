package no.nav.arbeidsgiver.kandidatsok.es.client;

public enum PrioritertMålgruppe {
    hullICv("hullICv"),
    senior("senior"),
    ung("ung");

    public final String name;

    PrioritertMålgruppe(String name) {
        this.name = name;
    }
}
