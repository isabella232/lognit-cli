package net.intelie.lognit.cli.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static net.intelie.lognit.cli.model.JsonHelpers.jsonElement;
import static net.intelie.lognit.cli.model.JsonHelpers.jsonExpected;
import static net.intelie.lognit.cli.model.JsonHelpers.jsonPrepare;
import static org.fest.assertions.Assertions.assertThat;

public class StatsSummaryTest {
    public static final String TEST_JSON = "{ total_docs: 150," +
            " nodes: ['AA', 'BB']," +
            " queries: ['AAA','BBB','CCC','DDD']," +
            " missing: 2, " +
            " per_nodes: [" +
            "    { " +
            "      node: 'AA'," +
            "      queries: ['AAA', 'BBB', 'CCC']," +
            "      total_docs: 100 " +
            "    }," +
            "    { " +
            "      node: 'BB'," +
            "      queries: ['DDD', 'BBB', 'CCC']," +
            "      total_docs: 50 " +
            "    }]" +
            " }";

    @Test
    public void whenDeserializing() {
        StatsSummary message = new Gson().fromJson(
                TEST_JSON, StatsSummary.class);

        assertThat(message.getNodes()).containsOnly("AA", "BB");
        assertThat(message.getQueries()).containsOnly("AAA", "BBB", "CCC", "DDD");
        assertThat(message.getTotalDocs()).isEqualTo(150);
        assertThat(message.getMissing()).isEqualTo(2);

        Iterator<Stats> it = message.getPerNodes().iterator();

        Stats st1 = it.next(), st2 = it.next();
        assertThat(st1.getNode()).isEqualTo("AA");
        assertThat(st1.getQueries()).containsOnly("AAA", "BBB", "CCC");
        assertThat(st1.getTotalDocs()).isEqualTo(100);

        assertThat(st2.getNode()).isEqualTo("BB");
        assertThat(st2.getQueries()).containsOnly("DDD", "BBB", "CCC");
        assertThat(st2.getTotalDocs()).isEqualTo(50);
    }

    @Test
    public void whenSerializing() {
        JsonElement actual = jsonElement(new StatsSummary(Arrays.asList(
                new Stats("AA", 100, Arrays.asList("AAA", "BBB", "CCC")),
                new Stats("BB", 50, Arrays.asList("DDD", "BBB", "CCC"))), 2));

        assertThat(actual).isEqualTo(jsonExpected(TEST_JSON));
    }

    @Test
    public void fullTest() {
        JsonElement actual = jsonPrepare(TEST_JSON, StatsSummary.class);
        assertThat(actual).isEqualTo(jsonExpected(TEST_JSON));
    }
}