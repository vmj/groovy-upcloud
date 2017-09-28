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
 * Preconfigured server plan.
 * <p>
 *     In addition to plans, server can be configured with {@code custom} plan which allows freely scalable
 *     CPU cores, memory amount and storage resources.
 * </p>
 * <p>
 *     A list of these is typically fetched from {@link fi.linuxbox.upcloud.api.UpCloud#plans(def) Plans API}.
 * </p>
 */
@InheritConstructors
class Plan extends Resource {
    /**
     * Number of CPU cores in this plan.
     */
    Integer coreNumber
    /**
     * Amount of memory in this plan.
     */
    Integer memoryAmount
    /**
     * Name of this plan.
     * <p>
     *     Names follow a pattern: e.g. {@code 1xCPU-1GB}.
     * </p>
     */
    String name
    /**
     * Transfer quota in this plan.
     */
    Integer publicTrafficOut
    /**
     * Storage quota in this plan.
     */
    Integer storageSize
    /**
     * Storage tier in this plan.
     */
    String storageTier
}
