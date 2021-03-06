/*
 * Groovy UpCloud library - API Module
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
package fi.linuxbox.upcloud.api.spec

import fi.linuxbox.upcloud.http.spi.Request
import fi.linuxbox.upcloud.http.spi.HTTP
import fi.linuxbox.upcloud.json.spi.JSON
import spock.lang.*

import fi.linuxbox.upcloud.api.mock.*
import fi.linuxbox.upcloud.core.*

/**
 * Base class for API specifications.
 */
abstract class ApiSpecification extends Specification {

    HTTP http = Mock()
    JSON json = Mock()
    Session<?> session = new SimpleSession(http, json, null, null)
    Map<String, Object> repr
    Request req

    def setup() {
        // allow default request callback to take only one argument
        session.callback network_error: {}

        // given a JSON implementation that saves the repr
        (_..1) * json.encode(_) >> { repr = it[0]; null }
        // and an HTTP implementation that saves the Request
        (_..1) * http.execute(*_) >> { req = it[0]; null }
    }

    void requestIs(def method, def resource, def repr = null) {
        // then request and resource were given as expected
        assert req?.method == method
        assert req.resource.endsWith(resource)
        // and the repr is as expected
        assert this.repr == repr
    }
}
