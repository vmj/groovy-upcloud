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
package fi.linuxbox.upcloud.core

import fi.linuxbox.upcloud.core.http.simple.SimpleHeaders
import groovy.transform.CompileStatic

/**
 * UpCloud API constants.
 */
@CompileStatic
class UpCloudContract {
    /**
     * UpCloud API host.
     */
    static final String HOST = 'https://api.upcloud.com'
    /**
     * UpCloud API context path.
     */
    static final String API_VERSION = '/1.2/'

    /**
     * Construct request headers for a session.
     *
     * @param username UpCloud API username.  This is not the one you use to login to the control panel.
     * @param password UpCloud API password.  This is not the one you use to login to the control panel.
     * @param userAgent
     * @return Read-only request headers.
     */
    static SimpleHeaders requestHeaders(final String username, final String password, final String userAgent) {
         // The Host header ensures that it is set correctly.  At least the Apache HttpAsyncClient would add it by
         // default, but it would add the ":443" port there, too.  The UpCloud server doesn't like that.
        new SimpleHeaders([
                'Accept'       : 'application/json; charset=UTF-8',
                'Authorization': 'Basic ' + "$username:$password".bytes.encodeBase64().toString(),
                'Content-Type' : 'application/json',
                'Host'         : 'api.upcloud.com',
                'User-Agent'   : 'Groovy UpCloud/0.0.7-SNAPSHOT ' + userAgent,
        ])
    }
}
