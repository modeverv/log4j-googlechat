package asia.daemon.lovesaemi.googlechatappender;

import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("ALL")
public class GoogleChatTest {
    public static final String GOOGLECHAT_WEBHOOK = "GOOGLECHAT_WEBHOOK";

    @Before
    public void setup() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setPackages("asia.daemon.lovesaemi");
        //String webhookUrl = System.getProperty(GOOGLECHAT_WEBHOOK, System.getenv(GOOGLECHAT_WEBHOOK));
        String webhookUrl = System.getenv(GOOGLECHAT_WEBHOOK);
        System.out.println(webhookUrl);
        assertNotNull(GOOGLECHAT_WEBHOOK + " MUST NOT be null", webhookUrl);
        AppenderComponentBuilder appenderComponentBuilder = builder.newAppender("googlechat", "GoogleChat");
        appenderComponentBuilder.addAttribute("webhook", webhookUrl);
        appenderComponentBuilder.add(builder.newFilter(
                "MarkerFilter",
                Filter.Result.ACCEPT,
                Filter.Result.DENY).addAttribute("marker", "GOOGLECHAT"));
        appenderComponentBuilder.add(builder.newLayout("PatternLayout").
                addAttribute("pattern", "[%d{yyyy-MM-dd HH:mm:ss.SSS}], %-5p, %t, %c, %m%n"));
        builder.add(appenderComponentBuilder);
        builder.add(builder.newRootLogger(Level.INFO).add(builder.newAppenderRef("googlechat")));
        System.out.println(builder.toXmlConfiguration());
        Configurator.initialize(builder.build());
    }

    @Test
    public void sendInfo() throws InterruptedException {
        Marker GOOGLECHAT = MarkerManager.getMarker("GOOGLECHAT");
        LogManager.getLogger(getClass()).warn(GOOGLECHAT,"Test warning message");
        Thread.sleep(2000);
    }

    @Test
    public void sendInfoLong() throws InterruptedException {
        Marker GOOGLECHAT = MarkerManager.getMarker("GOOGLECHAT");
        StringBuilder sb = new StringBuilder();
        for(int i=0,l=3001;i<l;i++){
            sb.append("韻");
        }
        LogManager.getLogger(getClass()).warn(GOOGLECHAT,sb.toString());
        Thread.sleep(2000);
    }
}