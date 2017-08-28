import ch.qos.logback.classic.BasicConfigurator;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.util.StatusPrinter;
import fi.linuxbox.upcloud.script.UpCloudScript;

import static ch.qos.logback.classic.Level.DEBUG;
import static ch.qos.logback.classic.Level.INFO;

/**
 *
 */
public class LogbackConfigurator
        extends BasicConfigurator
{
    public LogbackConfigurator()
    {
        super();
    }

    @Override
    public void configure(final LoggerContext lc)
    {
        super.configure(lc);

        lc.getLogger(getClass().getPackage().getName()).setLevel(DEBUG);
        lc.getLogger(UpCloudScript.class.getPackage().getName()).setLevel(DEBUG);

        StatusPrinter.print(lc);
        lc.getStatusManager().add(new OnConsoleStatusListener());
    }
}
