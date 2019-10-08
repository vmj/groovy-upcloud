/*
 * Groovy UpCloud library - Core Module
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
package fi.linuxbox.upcloud.core

import fi.linuxbox.upcloud.core.http.simple.SimpleHeaderIterator
import fi.linuxbox.upcloud.http.spi.HTTP
import spock.lang.Specification
import spock.lang.Unroll

import static fi.linuxbox.upcloud.core.NamingContract.javaClassOrPropertyToJsonProperty
import static fi.linuxbox.upcloud.core.NamingContract.javaClassToJavaProperty
import static fi.linuxbox.upcloud.core.NamingContract.javaClassToPathSegment
import static fi.linuxbox.upcloud.core.NamingContract.jsonPropertyToJavaClass
import static fi.linuxbox.upcloud.core.NamingContract.jsonPropertyToJavaProperty

class NamingContractSpec extends Specification {

    @Unroll
    def "Name juggling: #klass -> #propertyName"() {
        expect:
        javaClassToJavaProperty(klass) == propertyName

        where:
        klass                | propertyName
        HTTP                 | 'http'
        Resource             | 'resource'
        ResourceLoader       | 'resourceLoader'
        SimpleHeaderIterator | 'simpleHeaderIterator'
    }

    @Unroll
    def "Name juggling: #type_name -> #className"() {
        expect:
        jsonPropertyToJavaClass(type_name) == className

        where:
        type_name                | className
        'server'                 | 'Server'
        'storage_device'         | 'StorageDevice'
        'destination_port_start' | 'DestinationPortStart'
        'server_plan_1xCPU-1GB'  | 'ServerPlan_1xCPU_1GB'
    }

    @Unroll
    def "Name juggling: #className -> #type_name"() {
        expect:
        javaClassOrPropertyToJsonProperty(className) == type_name

        where:
        className              | type_name
        'RESOURCE'             | 'resource'
        'Server'               | 'server'
        'StorageDevice'        | 'storage_device'
        'DestinationPortStart' | 'destination_port_start'
    }

    @Unroll
    def "Name juggling: #type_name -> #propertyName"() {
        expect:
        jsonPropertyToJavaProperty(type_name) == propertyName

        where:
        type_name                | propertyName
        'server'                 | 'server'
        'storage_device'         | 'storageDevice'
        'destination_port_start' | 'destinationPortStart'
        'server_plan_1xCPU-1GB'  | 'serverPlan_1xCPU_1GB'
    }

    @Unroll
    def "Name juggling: #className -> #urlPathSegment"() {
        expect:
        javaClassToPathSegment(className) == urlPathSegment

        where:
        className   | urlPathSegment
        'RESOURCE'  | 'resource'
        'IpAddress' | 'ip_address'
        'Server'    | 'server'
        'Storage'   | 'storage'
        'Tag'       | 'tag'
    }
}
