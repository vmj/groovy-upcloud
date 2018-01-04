/*
 * Groovy UpCloud library - an example application
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
package fi.linuxbox.upcloud.app

import spock.lang.*
/**
 *
 */
class AppSpec extends Specification {

    def "Initialization"() {
        when:
            def app = new App(new String[2])

        then:
            app.isRunning()
    }

    def "Close"() {
        given:
            def app = new App(new String[2])

        when:
            app.close()

        then:
            !app.isRunning()
    }

    def "Wrong initialization"() {
        when:
            def app = new App()

        then:
            !app.isRunning()
    }
}
