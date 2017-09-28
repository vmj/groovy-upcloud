/*
 * Groovy UpCloud library - Resource Module
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
package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * Administrative account to create for a new server (from a Linux template).
 * <p>
 *     An instance of this class can be set to {@link Server#loginUser} property when creating a new server from a
 *     Linux template.
 * </p>
 */
@InheritConstructors
class LoginUser extends Resource {
    /**
     * A valid, non-reserved username.
     * <p>
     *     If set to other than {@code root}, normal user with {@code sudo} privileges is created.
     * </p>
     */
    String username
    /**
     * Whether to set administrative account's password: {@code yes} or {@code no}.
     * <p>
     *     This can be set to {@code no} if the user can login only via SSH.
     * </p>
     */
    String createPassword
    /**
     * Public SSH keys for passwordless login.
     */
    List<String> sshKeys
}
