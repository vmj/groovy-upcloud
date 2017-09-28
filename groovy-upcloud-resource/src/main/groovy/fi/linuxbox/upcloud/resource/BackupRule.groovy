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
 * Backup rule of a {@link Storage}.
 */
@InheritConstructors
class BackupRule extends Resource {
    /**
     * The weekday when the backup is created: {@code daily}, {@code mon}, {@code tue}, {@code wed}, {@code thu},
     * {@code fri}, {@code sat}, or {@code sun}.
     * <p>
     *     If {@code daily} is selected, backups are made every day at the same time.
     * </p>
     */
    String interval
    /**
     * The time of day when the backup is created: {@code 0000} - {@code 2359}.
     */
    String time
    /**
     * The number of days before a backup is automatically deleted: {@code 1} - {@code 1095}.
     * <p>
     *     The maximum retention period is three years (1095 days).
     * </p>
     */
    String retention
}
