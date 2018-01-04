/*
 * Groovy UpCloud library - Core Module
 * Copyright (C) 2018  <mikko@varri.fi>
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
package fi.linuxbox.upcloud.core;

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

        lc.getLogger(getClass().getPackage().getName()).setLevel(DEBUG);

        StatusPrinter.print(lc);
        lc.getStatusManager().add(new OnConsoleStatusListener());
    }
}
