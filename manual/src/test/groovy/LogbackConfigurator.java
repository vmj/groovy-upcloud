/*
 * Groovy UpCloud library - User Manual
 * Copyright (C) 2017  <mikko@varri.fi>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
