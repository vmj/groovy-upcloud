package fi.linuxbox.upcloud.script;

import ch.qos.logback.classic.*;
import ch.qos.logback.core.status.*;
import ch.qos.logback.core.util.*;

import static ch.qos.logback.classic.Level.*;

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

        lc.getLogger(getClass().getPackage().getName()).setLevel(INFO);

        StatusPrinter.print(lc);
        lc.getStatusManager().add(new OnConsoleStatusListener());
    }
}
