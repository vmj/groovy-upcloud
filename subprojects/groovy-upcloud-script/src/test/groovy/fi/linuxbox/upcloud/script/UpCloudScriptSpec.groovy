/*
 * Groovy UpCloud library - Script Module
 * Copyright (C) 2018  Mikko Värri
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
package fi.linuxbox.upcloud.script

import org.codehaus.groovy.control.CompilerConfiguration
import spock.lang.Specification

class UpCloudScriptSpec extends Specification {

    def "early shutdown test"() {
        when: "the script calls close() while requests are still in-flight"
        shell().evaluate("""
            final session = newSession("username", "p4ssw0rd")
            session.callback network_error: {}

            final names = ['Alice', 'Bob', 'Clive', 'Dylan']
            int count = 0

            names.each { final name ->
                log.info "Making request for \$name"
                session.GET('/whatever') { final response ->
                    log.info "Got response for \$name"
                    if (++count >= 2) {
                        log.info "Got enough responses, ignoring the rest and shutting down"
                        close()
                    }
                    log.info "Handled response for \$name"
                }
            }
        """)

        then: "nothing bad happens"
        noExceptionThrown()
    }

    def "unhandled exception in top-level code"() {
        when: "the script throws an exception from top-level code"
        shell().evaluate("""
            throw new FileNotFoundException("intentional test exception")
        """)

        then: "no timeout exception is thrown"
        noExceptionThrown()
    }

    def "auto-shutdown test"() {
        when: "the script is shutdown automatically after last response"
        shell().evaluate("""
            final session = newSession("username", "p4ssw0rd")
            session.callback network_error: {}

            session.GET('/whatever') { final response ->
                log.info "Callback done"
            }
            log.info "Top-level code done"
        """)

        then: "no timeout exception is thrown"
        noExceptionThrown()
    }

    private static GroovyShell shell() {
        final config = new CompilerConfiguration()
        config.scriptBaseClass = UpCloudScript.name

        return new GroovyShell(config)
    }
}
