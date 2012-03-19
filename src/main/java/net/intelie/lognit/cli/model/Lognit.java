package net.intelie.lognit.cli.model;

import net.intelie.lognit.cli.http.Entity;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestListener;
import net.intelie.lognit.cli.http.RestListenerHandle;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Lognit {
    public static final String URL_WELCOME = "/rest/users/welcome";
    public static final String URL_PURGE = "/rest/purge";
    public static final String URL_UNPURGE = "/rest/purge/unpurge";
    public static final String URL_PURGE_INFO = "/rest/purge/%s?all=%s";
    public static final String URL_PURGE_CANCEL = "/rest/purge/cancel/%s";
    public static final String URL_PURGE_CANCEL_ALL = "/rest/purge/cancel-all";
    public static final String URL_STATS = "/rest/stats";
    public static final String URL_SEARCH = "/rest/search?expression=%s&windowLength=%s";
    public static final String URL_TERMS = "/rest/terms?field=%s&term=%s&avoidColons=true&size=100";
    private final RestClient client;

    public Lognit(RestClient client) {
        this.client = client;
    }

    public void setServer(String server) {
        client.setServer(server);
    }

    public String getServer() {
        return client.getServer();
    }

    public void authenticate(String username, String password) {
        client.authenticate(username, password);
    }

    public RestListenerHandle search(String query, int windowLength, RestListener<MessageBag> listener) throws IOException {
        SearchChannel channel = client.get(make(URL_SEARCH, query, windowLength), SearchChannel.class);
        return client.listen(channel.getChannel(), MessageBag.class, listener);
    }

    public Purge purge(String query, int windowLength, boolean all) throws IOException {
        Entity entity = new Entity()
                .add("expression", query)
                .add("windowLength", Integer.toString(windowLength))
                .add("all", Boolean.toString(all));
        
        return client.post(URL_PURGE, entity, Purge.class);
    }

    public Purge unpurge(boolean all) throws IOException {
        Entity entity = new Entity().add("all", Boolean.toString(all));
        return client.post(make(URL_UNPURGE), entity, Purge.class);
    }

    public PurgeInfo purgeInfo(String id, boolean all) throws IOException {
        return client.get(make(URL_PURGE_INFO, id, all), PurgeInfo.class);
    }

    public void cancelPurge(String id, boolean all) throws IOException {
        Entity entity = new Entity().add("all", Boolean.toString(all));
        client.post(make(URL_PURGE_CANCEL, id), entity, Void.class);
    }

    public void cancelAllPurges(boolean all) throws IOException {
        Entity entity = new Entity().add("all", Boolean.toString(all));
        client.post(make(URL_PURGE_CANCEL_ALL), entity, Void.class);
    }

    public Terms terms(String field, String term) throws IOException {
        return client.get(make(URL_TERMS, field, term), Terms.class);
    }

    public Welcome welcome() throws IOException {
        return client.get(URL_WELCOME, Welcome.class);
    }

    public StatsSummary stats() throws IOException {
        return client.get(URL_STATS, StatsSummary.class);
    }

    private String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }

    private String make(String url, Object... args) throws UnsupportedEncodingException {
        Object[] encoded = new Object[args.length];
        for(int i=0; i<args.length; i++)
            encoded[i] = encode(args[i].toString());
        return String.format(url, encoded);
    }
}