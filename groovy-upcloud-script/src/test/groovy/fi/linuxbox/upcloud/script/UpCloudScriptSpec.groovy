/*
 * Groovy UpCloud library - Script Module
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
package fi.linuxbox.upcloud.script

import org.codehaus.groovy.control.CompilerConfiguration
import spock.lang.Specification

class UpCloudScriptSpec extends Specification {

    def "low-level shutdown test"() {
        given: "a compiler configuration that customizes the script class"
            def config = new CompilerConfiguration()
            config.scriptBaseClass = UpCloudScript.name

        and: "a groovy shell with the configuration"
            def groovyShell = new GroovyShell(config)

        when: "a "
            groovyShell.evaluate("""
                def session = newSession("username", "p4ssw0rd")
    
                def names = ['Alice', 'Bob', 'Clive', 'Dylan']
                def count = 0
    
                names.each { name ->
                    log.info "Making request for \$name"
                    session.GET('/whatever') { response ->
                        log.info "Got response for \$name"
                        count++
                        if (count >= 2) {
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
}
