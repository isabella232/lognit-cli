package net.intelie.lognit.cli;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

public class UserOptions {
    private final String server;
    private final String user;
    private final String password;
    private final String query;
    private final String format;
    private final boolean follow;
    private final boolean info;
    private final boolean help;
    private final int timeout;
    private final int lines;
    private final boolean complete;
    private final boolean verbose;

    public UserOptions(String... args) {
        ArgsParser parser = new ArgsParser(args);
        help = parser.flag("-?", "--help");
        server = parser.option(String.class, "-s", "--server");
        user = parser.option(String.class, "-u", "--user");
        password = parser.option(String.class, "-p", "--pass", "--password");
        timeout = def(parser.option(Integer.class, "-t", "--timeout"), 10);
        lines = def(parser.option(Integer.class, "-n", "--lines"), 20);
        format = def(parser.option(String.class, "-b", "--format"), "colored");
        follow = parser.flag("-f", "--follow");
        info = parser.flag("-i", "--info");
        complete = parser.flag("-c", "--complete");
        verbose = parser.flag("-v", "--verbose");
        query = parser.text();
    }

    private <T> T def(T value, T def) {
        return value != null ? value : def;
    }

    public String getServer() {
        return server;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getQuery() {
        return query;
    }

    public boolean isFollow() {
        return follow;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getTimeoutInMilliseconds() {
        return getTimeout() * 1000;
    }

    public boolean hasQuery() {
        return !Strings.isNullOrEmpty(query);
    }

    public int getLines() {
        return lines;
    }

    public boolean hasServer() {
        return server != null;
    }

    public boolean isInfo() {
        return info;
    }

    public boolean isUsage() {
        return help;
    }

    public boolean askPassword() {
        return user == null || password == null;
    }


    public boolean isComplete() {
        return complete;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public String getFormat() {
        return format;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserOptions)) return false;
        UserOptions that = (UserOptions) o;

        return Objects.equal(this.server, that.server) &&
                Objects.equal(this.user, that.user) &&
                Objects.equal(this.password, that.password) &&
                Objects.equal(this.query, that.query) &&
                Objects.equal(this.follow, that.follow) &&
                Objects.equal(this.timeout, that.timeout) &&
                Objects.equal(this.lines, that.lines) &&
                Objects.equal(this.info, that.info) &&
                Objects.equal(this.format, that.format) &&
                Objects.equal(this.complete, that.complete) &&
                Objects.equal(this.verbose, that.verbose) &&
                Objects.equal(this.help, that.help);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(server, user, password, query, follow, timeout, lines, info, format, complete, verbose, help);
    }


}