/*
 * Groovy UpCloud library - API Module
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
package fi.linuxbox.upcloud.api

import ch.qos.logback.classic.*
import ch.qos.logback.core.status.*
import ch.qos.logback.core.util.*
import groovy.transform.CompileStatic

import static ch.qos.logback.classic.Level.*

/**
 * Configures logging of tests in this module.
 */
@CompileStatic
class LogbackConfigurator extends BasicConfigurator {
    @Override
    void configure(final LoggerContext loggerContext) {
        super.configure loggerContext

        loggerContext.getLogger(this.class.package.name).level = DEBUG

        StatusPrinter.print loggerContext
        loggerContext.statusManager.add new OnConsoleStatusListener()
    }
}
