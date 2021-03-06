package net.intelie.lognit.cli.http;

import org.cometd.client.BayeuxClient;

import java.util.concurrent.atomic.AtomicBoolean;

public class BayeuxHandle implements RestListenerHandle {
    private final AtomicBoolean closed;
    private final BayeuxClient client;
    private final String channel;
    private volatile boolean valid = true;

    public BayeuxHandle(BayeuxClient client, String channel) {
        this.client = client;
        this.channel = channel;
        this.closed = new AtomicBoolean(false);
    }

    @Override
    public void waitDisconnected() {
        while (valid)
            if (this.client.waitFor(1000, BayeuxClient.State.UNCONNECTED, BayeuxClient.State.DISCONNECTED))
                return;
    }

    public void invalidate() {
        this.valid = false;
    }

    @Override
    public void close() {
        if (!closed.getAndSet(true)) {
            this.client.getChannel(channel).unsubscribe();
            this.client.disconnect();
        }
    }
}
