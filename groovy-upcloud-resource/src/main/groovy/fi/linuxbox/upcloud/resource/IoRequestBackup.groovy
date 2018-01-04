/*
 * Groovy UpCloud library - Resource Module
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
package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * I/O request price information for backups for a {@link Zone}.
 */
@InheritConstructors
class IoRequestBackup extends Resource {
    /**
     * Amount in IO operations.
     */
    Integer amount
    /**
     * Price in credits per {@link #amount} IO operations per hour.
     */
    BigDecimal price
}
