package asia.daemon.lovesaemi.googlechatappender;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public class GoogleChatManager extends AbstractManager {

    private static final SlackManagerFactory FACTORY = new SlackManagerFactory();

    private final URL webhook;
    private final JsonFactory factory = new JsonFactory();

    private GoogleChatManager(final LoggerContext loggerContext, final String name, final URL webhook) {
        super(loggerContext, name);
        this.webhook = webhook;
    }

    public void sendMessage(String message) {
        try {
            HttpURLConnection connection = (HttpURLConnection) webhook.openConnection();
            connection.setDoOutput(true);
            //connection.setRequestMethod("POST");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            message = message.substring(0,Math.min(message.length(),3000));
            try (JsonGenerator generator = factory.createGenerator(connection.getOutputStream())) {
                generator.writeStartObject();
                generator.writeStringField("text", message);
                generator.writeEndObject();
            }
            int responseCode = connection.getResponseCode();
            //System.out.println(responseCode);
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println(connection.getResponseMessage());
                throw new AppenderLoggingException("Got a non-200 status: " + responseCode);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static GoogleChatManager getManager(final String name, final LoggerContext context, final URL webhook) {
        GoogleChatConfiguration config = new GoogleChatConfiguration();
        config.context = context;
        config.webhook = webhook;
        return getManager(name, FACTORY, config);
    }

    private static class GoogleChatConfiguration {
        private LoggerContext context;
        private URL webhook;
    }

    private static class SlackManagerFactory implements ManagerFactory<GoogleChatManager, GoogleChatConfiguration> {

        @Override
        public GoogleChatManager createManager(final String name, final GoogleChatConfiguration data) {
            return new GoogleChatManager(data.context, name, data.webhook);
        }
    }
}