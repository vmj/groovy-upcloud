/*
 * Groovy UpCloud library - Resource Module
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
package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.http.spi.HTTP
import spock.lang.*

import fi.linuxbox.upcloud.core.*

import static fi.linuxbox.upcloud.resource.Builder.*

/**
 *
 */
class BuilderSpec extends Specification {

    HTTPFacade<?> session = new SimpleSession(Mock(HTTP), null, null, null)

    def "builder inside list"() {
        when:
        def firewallRules = [
                firewallRule {
                    position = "1"
                },
                firewallRule {
                    position = "2"
                }
        ]

        then:
        firewallRules instanceof List
        firewallRules.size() == 2
        firewallRules.every { it instanceof FirewallRule }
        firewallRules[0].position == "1"
        firewallRules[1].position == "2"
    }

    def "builder inside builder"() {
        when:
        def server = server {
            storageDevices = [
                    storageDevice {
                        tier = "maxiops"
                    },
                    storageDevice {
                        tier = "hdd"
                    }
            ]
        }

        then:
        server instanceof Server
        server.storageDevices instanceof List
        server.storageDevices.every { it instanceof StorageDevice }
        server.storageDevices[0].tier == 'maxiops'
        server.storageDevices[1].tier == 'hdd'
    }

    def "account builder"() {
        when:
        def account = account {}

        then:
        account instanceof Account
    }

    def "account without config"() {
        when:
        def account = account()

        then:
        account instanceof Account
    }

    def "account with kwargs and config"() {
        when:
        def account = account HTTP: session, {}

        then:
        account instanceof Account
        account.HTTP == session
    }

    def "account with kwargs and no config"() {
        when:
        def account = account HTTP: session

        then:
        account instanceof Account
        account.HTTP == session
    }

    def "backupRule builder"() {
        when:
        def backupRule = backupRule {}

        then:
        backupRule instanceof BackupRule
    }

    def "backupRule without config"() {
        when:
        def backupRule = backupRule()

        then:
        backupRule instanceof BackupRule
    }

    def "backupRule with kwargs and config"() {
        when:
        def backupRule = backupRule HTTP: session, {}

        then:
        backupRule instanceof BackupRule
        backupRule.HTTP == session
    }

    def "backupRule with kwargs and no config"() {
        when:
        def backupRule = backupRule HTTP: session

        then:
        backupRule instanceof BackupRule
        backupRule.HTTP == session
    }

    def "firewall builder"() {
        when:
        def firewall = firewall {}

        then:
        firewall instanceof Firewall
    }

    def "firewall without config"() {
        when:
        def firewall = firewall()

        then:
        firewall instanceof Firewall
    }

    def "firewall with kwargs and config"() {
        when:
        def firewall = firewall HTTP: session, {}

        then:
        firewall instanceof Firewall
        firewall.HTTP == session
    }

    def "firewall with kwargs and no config"() {
        when:
        def firewall = firewall HTTP: session

        then:
        firewall instanceof Firewall
        firewall.HTTP == session
    }

    def "firewallRule builder"() {
        when:
        def firewallRule = firewallRule {}

        then:
        firewallRule instanceof FirewallRule
    }

    def "firewallRule without config"() {
        when:
        def firewallRule = firewallRule()

        then:
        firewallRule instanceof FirewallRule
    }

    def "firewallRule with kwargs and config"() {
        when:
        def firewallRule = firewallRule HTTP: session, {}

        then:
        firewallRule instanceof FirewallRule
        firewallRule.HTTP == session
    }

    def "firewallRule with kwargs and no config"() {
        when:
        def firewallRule = firewallRule HTTP: session

        then:
        firewallRule instanceof FirewallRule
        firewallRule.HTTP == session
    }

    def "ioRequestBackup builder"() {
        when:
        def ioRequestBackup = ioRequestBackup {}

        then:
        ioRequestBackup instanceof IoRequestBackup
    }

    def "ioRequestBackup without config"() {
        when:
        def ioRequestBackup = ioRequestBackup()

        then:
        ioRequestBackup instanceof IoRequestBackup
    }

    def "ioRequestBackup with kwargs and config"() {
        when:
        def ioRequestBackup = ioRequestBackup HTTP: session, {}

        then:
        ioRequestBackup instanceof IoRequestBackup
        ioRequestBackup.HTTP == session
    }

    def "ioRequestBackup with kwargs and no config"() {
        when:
        def ioRequestBackup = ioRequestBackup HTTP: session

        then:
        ioRequestBackup instanceof IoRequestBackup
        ioRequestBackup.HTTP == session
    }

    def "ioRequestHdd builder"() {
        when:
        def ioRequestHdd = ioRequestHdd {}

        then:
        ioRequestHdd instanceof IoRequestHdd
    }

    def "ioRequestHdd without config"() {
        when:
        def ioRequestHdd = ioRequestHdd()

        then:
        ioRequestHdd instanceof IoRequestHdd
    }

    def "ioRequestHdd with kwargs and config"() {
        when:
        def ioRequestHdd = ioRequestHdd HTTP: session, {}

        then:
        ioRequestHdd instanceof IoRequestHdd
        ioRequestHdd.HTTP == session
    }

    def "ioRequestHdd with kwargs and no config"() {
        when:
        def ioRequestHdd = ioRequestHdd HTTP: session

        then:
        ioRequestHdd instanceof IoRequestHdd
        ioRequestHdd.HTTP == session
    }

    def "ioRequestMaxiops builder"() {
        when:
        def ioRequestMaxiops = ioRequestMaxiops {}

        then:
        ioRequestMaxiops instanceof IoRequestMaxiops
    }

    def "ioRequestMaxiops without config"() {
        when:
        def ioRequestMaxiops = ioRequestMaxiops()

        then:
        ioRequestMaxiops instanceof IoRequestMaxiops
    }

    def "ioRequestMaxiops with kwargs and config"() {
        when:
        def ioRequestMaxiops = ioRequestMaxiops HTTP: session, {}

        then:
        ioRequestMaxiops instanceof IoRequestMaxiops
        ioRequestMaxiops.HTTP == session
    }

    def "ioRequestMaxiops with kwargs and no config"() {
        when:
        def ioRequestMaxiops = ioRequestMaxiops HTTP: session

        then:
        ioRequestMaxiops instanceof IoRequestMaxiops
        ioRequestMaxiops.HTTP == session
    }

    def "ipAddress builder"() {
        when:
        def ipAddress = ipAddress {}

        then:
        ipAddress instanceof IpAddress
    }

    def "ipAddress without config"() {
        when:
        def ipAddress = ipAddress()

        then:
        ipAddress instanceof IpAddress
    }

    def "ipAddress with kwargs and config"() {
        when:
        def ipAddress = ipAddress HTTP: session, {}

        then:
        ipAddress instanceof IpAddress
        ipAddress.HTTP == session
    }

    def "ipAddress with kwargs and no config"() {
        when:
        def ipAddress = ipAddress HTTP: session

        then:
        ipAddress instanceof IpAddress
        ipAddress.HTTP == session
    }

    def "ipv4Address builder"() {
        when:
        def ipv4Address = ipv4Address {}

        then:
        ipv4Address instanceof Ipv4Address
    }

    def "ipv4Address without config"() {
        when:
        def ipv4Address = ipv4Address()

        then:
        ipv4Address instanceof Ipv4Address
    }

    def "ipv4Address with kwargs and config"() {
        when:
        def ipv4Address = ipv4Address HTTP: session, {}

        then:
        ipv4Address instanceof Ipv4Address
        ipv4Address.HTTP == session
    }

    def "ipv4Address with kwargs and no config"() {
        when:
        def ipv4Address = ipv4Address HTTP: session

        then:
        ipv4Address instanceof Ipv4Address
        ipv4Address.HTTP == session
    }

    def "ipv6Address builder"() {
        when:
        def ipv6Address = ipv6Address {}

        then:
        ipv6Address instanceof Ipv6Address
    }

    def "ipv6Address without config"() {
        when:
        def ipv6Address = ipv6Address()

        then:
        ipv6Address instanceof Ipv6Address
    }

    def "ipv6Address with kwargs and config"() {
        when:
        def ipv6Address = ipv6Address HTTP: session, {}

        then:
        ipv6Address instanceof Ipv6Address
        ipv6Address.HTTP == session
    }

    def "ipv6Address with kwargs and no config"() {
        when:
        def ipv6Address = ipv6Address HTTP: session

        then:
        ipv6Address instanceof Ipv6Address
        ipv6Address.HTTP == session
    }

    def "loginUser builder"() {
        when:
        def loginUser = loginUser {}

        then:
        loginUser instanceof LoginUser
    }

    def "loginUser without config"() {
        when:
        def loginUser = loginUser()

        then:
        loginUser instanceof LoginUser
    }

    def "loginUser with kwargs and config"() {
        when:
        def loginUser = loginUser HTTP: session, {}

        then:
        loginUser instanceof LoginUser
        loginUser.HTTP == session
    }

    def "loginUser with kwargs and no config"() {
        when:
        def loginUser = loginUser HTTP: session

        then:
        loginUser instanceof LoginUser
        loginUser.HTTP == session
    }

    def "plan builder"() {
        when:
        def plan = plan {}

        then:
        plan instanceof Plan
    }

    def "plan without config"() {
        when:
        def plan = plan()

        then:
        plan instanceof Plan
    }

    def "plan with kwargs and config"() {
        when:
        def plan = plan HTTP: session, {}

        then:
        plan instanceof Plan
        plan.HTTP == session
    }

    def "plan with kwargs and no config"() {
        when:
        def plan = plan HTTP: session

        then:
        plan instanceof Plan
        plan.HTTP == session
    }

    def "publicIpv4BandwidthIn builder"() {
        when:
        def publicIpv4BandwidthIn = publicIpv4BandwidthIn {}

        then:
        publicIpv4BandwidthIn instanceof PublicIpv4BandwidthIn
    }

    def "publicIpv4BandwidthIn without config"() {
        when:
        def publicIpv4BandwidthIn = publicIpv4BandwidthIn()

        then:
        publicIpv4BandwidthIn instanceof PublicIpv4BandwidthIn
    }

    def "publicIpv4BandwidthIn with kwargs and config"() {
        when:
        def publicIpv4BandwidthIn = publicIpv4BandwidthIn HTTP: session, {}

        then:
        publicIpv4BandwidthIn instanceof PublicIpv4BandwidthIn
        publicIpv4BandwidthIn.HTTP == session
    }

    def "publicIpv4BandwidthIn with kwargs and no config"() {
        when:
        def publicIpv4BandwidthIn = publicIpv4BandwidthIn HTTP: session

        then:
        publicIpv4BandwidthIn instanceof PublicIpv4BandwidthIn
        publicIpv4BandwidthIn.HTTP == session
    }

    def "publicIpv4BandwidthOut builder"() {
        when:
        def publicIpv4BandwidthOut = publicIpv4BandwidthOut {}

        then:
        publicIpv4BandwidthOut instanceof PublicIpv4BandwidthOut
    }

    def "publicIpv4BandwidthOut without config"() {
        when:
        def publicIpv4BandwidthOut = publicIpv4BandwidthOut()

        then:
        publicIpv4BandwidthOut instanceof PublicIpv4BandwidthOut
    }

    def "publicIpv4BandwidthOut with kwargs and config"() {
        when:
        def publicIpv4BandwidthOut = publicIpv4BandwidthOut HTTP: session, {}

        then:
        publicIpv4BandwidthOut instanceof PublicIpv4BandwidthOut
        publicIpv4BandwidthOut.HTTP == session
    }

    def "publicIpv4BandwidthOut with kwargs and no config"() {
        when:
        def publicIpv4BandwidthOut = publicIpv4BandwidthOut HTTP: session

        then:
        publicIpv4BandwidthOut instanceof PublicIpv4BandwidthOut
        publicIpv4BandwidthOut.HTTP == session
    }

    def "publicIpv6BandwidthIn builder"() {
        when:
        def publicIpv6BandwidthIn = publicIpv6BandwidthIn {}

        then:
        publicIpv6BandwidthIn instanceof PublicIpv6BandwidthIn
    }

    def "publicIpv6BandwidthIn without config"() {
        when:
        def publicIpv6BandwidthIn = publicIpv6BandwidthIn()

        then:
        publicIpv6BandwidthIn instanceof PublicIpv6BandwidthIn
    }

    def "publicIpv6BandwidthIn with kwargs and config"() {
        when:
        def publicIpv6BandwidthIn = publicIpv6BandwidthIn HTTP: session, {}

        then:
        publicIpv6BandwidthIn instanceof PublicIpv6BandwidthIn
        publicIpv6BandwidthIn.HTTP == session
    }

    def "publicIpv6BandwidthIn with kwargs and no config"() {
        when:
        def publicIpv6BandwidthIn = publicIpv6BandwidthIn HTTP: session

        then:
        publicIpv6BandwidthIn instanceof PublicIpv6BandwidthIn
        publicIpv6BandwidthIn.HTTP == session
    }

    def "publicIpv6BandwidthOut builder"() {
        when:
        def publicIpv6BandwidthOut = publicIpv6BandwidthOut {}

        then:
        publicIpv6BandwidthOut instanceof PublicIpv6BandwidthOut
    }

    def "publicIpv6BandwidthOut without config"() {
        when:
        def publicIpv6BandwidthOut = publicIpv6BandwidthOut()

        then:
        publicIpv6BandwidthOut instanceof PublicIpv6BandwidthOut
    }

    def "publicIpv6BandwidthOut with kwargs and config"() {
        when:
        def publicIpv6BandwidthOut = publicIpv6BandwidthOut HTTP: session, {}

        then:
        publicIpv6BandwidthOut instanceof PublicIpv6BandwidthOut
        publicIpv6BandwidthOut.HTTP == session
    }

    def "publicIpv6BandwidthOut with kwargs and no config"() {
        when:
        def publicIpv6BandwidthOut = publicIpv6BandwidthOut HTTP: session

        then:
        publicIpv6BandwidthOut instanceof PublicIpv6BandwidthOut
        publicIpv6BandwidthOut.HTTP == session
    }

    def "resourceLimits builder"() {
        when:
        def resourceLimits = resourceLimits {}

        then:
        resourceLimits instanceof ResourceLimits
    }

    def "resourceLimits without config"() {
        when:
        def resourceLimits = resourceLimits()

        then:
        resourceLimits instanceof ResourceLimits
    }

    def "resourceLimits with kwargs and config"() {
        when:
        def resourceLimits = resourceLimits HTTP: session, {}

        then:
        resourceLimits instanceof ResourceLimits
        resourceLimits.HTTP == session
    }

    def "resourceLimits with kwargs and no config"() {
        when:
        def resourceLimits = resourceLimits HTTP: session

        then:
        resourceLimits instanceof ResourceLimits
        resourceLimits.HTTP == session
    }

    def "server builder"() {
        when:
        def server = server {}

        then:
        server instanceof Server
    }

    def "server without config"() {
        when:
        def server = server()

        then:
        server instanceof Server
    }

    def "server with kwargs and config"() {
        when:
        def server = server HTTP: session, {}

        then:
        server instanceof Server
        server.HTTP == session
    }

    def "server with kwargs and no config"() {
        when:
        def server = server HTTP: session

        then:
        server instanceof Server
        server.HTTP == session
    }

    def "serverCore builder"() {
        when:
        def serverCore = serverCore {}

        then:
        serverCore instanceof ServerCore
    }

    def "serverCore without config"() {
        when:
        def serverCore = serverCore()

        then:
        serverCore instanceof ServerCore
    }

    def "serverCore with kwargs and config"() {
        when:
        def serverCore = serverCore HTTP: session, {}

        then:
        serverCore instanceof ServerCore
        serverCore.HTTP == session
    }

    def "serverCore with kwargs and no config"() {
        when:
        def serverCore = serverCore HTTP: session

        then:
        serverCore instanceof ServerCore
        serverCore.HTTP == session
    }

    def "serverMemory builder"() {
        when:
        def serverMemory = serverMemory {}

        then:
        serverMemory instanceof ServerMemory
    }

    def "serverMemory without config"() {
        when:
        def serverMemory = serverMemory()

        then:
        serverMemory instanceof ServerMemory
    }

    def "serverMemory with kwargs and config"() {
        when:
        def serverMemory = serverMemory HTTP: session, {}

        then:
        serverMemory instanceof ServerMemory
        serverMemory.HTTP == session
    }

    def "serverMemory with kwargs and no config"() {
        when:
        def serverMemory = serverMemory HTTP: session

        then:
        serverMemory instanceof ServerMemory
        serverMemory.HTTP == session
    }

    def "serverPlan_1xCPU_1GB builder"() {
        when:
        def serverPlan_1xCPU_1GB = serverPlan_1xCPU_1GB {}

        then:
        serverPlan_1xCPU_1GB instanceof ServerPlan_1xCPU_1GB
    }

    def "serverPlan_1xCPU_1GB without config"() {
        when:
        def serverPlan_1xCPU_1GB = serverPlan_1xCPU_1GB()

        then:
        serverPlan_1xCPU_1GB instanceof ServerPlan_1xCPU_1GB
    }

    def "serverPlan_1xCPU_1GB with kwargs and config"() {
        when:
        def serverPlan_1xCPU_1GB = serverPlan_1xCPU_1GB HTTP: session, {}

        then:
        serverPlan_1xCPU_1GB instanceof ServerPlan_1xCPU_1GB
        serverPlan_1xCPU_1GB.HTTP == session
    }

    def "serverPlan_1xCPU_1GB with kwargs and no config"() {
        when:
        def serverPlan_1xCPU_1GB = serverPlan_1xCPU_1GB HTTP: session

        then:
        serverPlan_1xCPU_1GB instanceof ServerPlan_1xCPU_1GB
        serverPlan_1xCPU_1GB.HTTP == session
    }

    def "serverPlan_1xCPU_2GB builder"() {
        when:
        def serverPlan_1xCPU_2GB = serverPlan_1xCPU_2GB {}

        then:
        serverPlan_1xCPU_2GB instanceof ServerPlan_1xCPU_2GB
    }

    def "serverPlan_1xCPU_2GB without config"() {
        when:
        def serverPlan_1xCPU_2GB = serverPlan_1xCPU_2GB()

        then:
        serverPlan_1xCPU_2GB instanceof ServerPlan_1xCPU_2GB
    }

    def "serverPlan_1xCPU_2GB with kwargs and config"() {
        when:
        def serverPlan_1xCPU_2GB = serverPlan_1xCPU_2GB HTTP: session, {}

        then:
        serverPlan_1xCPU_2GB instanceof ServerPlan_1xCPU_2GB
        serverPlan_1xCPU_2GB.HTTP == session
    }

    def "serverPlan_1xCPU_2GB with kwargs and no config"() {
        when:
        def serverPlan_1xCPU_2GB = serverPlan_1xCPU_2GB HTTP: session

        then:
        serverPlan_1xCPU_2GB instanceof ServerPlan_1xCPU_2GB
        serverPlan_1xCPU_2GB.HTTP == session
    }

    def "serverPlan_2xCPU_2GB builder"() {
        when:
        def serverPlan_2xCPU_2GB = serverPlan_2xCPU_2GB {}

        then:
        serverPlan_2xCPU_2GB instanceof ServerPlan_2xCPU_2GB
    }

    def "serverPlan_2xCPU_2GB without config"() {
        when:
        def serverPlan_2xCPU_2GB = serverPlan_2xCPU_2GB()

        then:
        serverPlan_2xCPU_2GB instanceof ServerPlan_2xCPU_2GB
    }

    def "serverPlan_2xCPU_2GB with kwargs and config"() {
        when:
        def serverPlan_2xCPU_2GB = serverPlan_2xCPU_2GB HTTP: session, {}

        then:
        serverPlan_2xCPU_2GB instanceof ServerPlan_2xCPU_2GB
        serverPlan_2xCPU_2GB.HTTP == session
    }

    def "serverPlan_2xCPU_2GB with kwargs and no config"() {
        when:
        def serverPlan_2xCPU_2GB = serverPlan_2xCPU_2GB HTTP: session

        then:
        serverPlan_2xCPU_2GB instanceof ServerPlan_2xCPU_2GB
        serverPlan_2xCPU_2GB.HTTP == session
    }

    def "serverPlan_2xCPU_4GB builder"() {
        when:
        def serverPlan_2xCPU_4GB = serverPlan_2xCPU_4GB {}

        then:
        serverPlan_2xCPU_4GB instanceof ServerPlan_2xCPU_4GB
    }

    def "serverPlan_2xCPU_4GB without config"() {
        when:
        def serverPlan_2xCPU_4GB = serverPlan_2xCPU_4GB()

        then:
        serverPlan_2xCPU_4GB instanceof ServerPlan_2xCPU_4GB
    }

    def "serverPlan_2xCPU_4GB with kwargs and config"() {
        when:
        def serverPlan_2xCPU_4GB = serverPlan_2xCPU_4GB HTTP: session, {}

        then:
        serverPlan_2xCPU_4GB instanceof ServerPlan_2xCPU_4GB
        serverPlan_2xCPU_4GB.HTTP == session
    }

    def "serverPlan_2xCPU_4GB with kwargs and no config"() {
        when:
        def serverPlan_2xCPU_4GB = serverPlan_2xCPU_4GB HTTP: session

        then:
        serverPlan_2xCPU_4GB instanceof ServerPlan_2xCPU_4GB
        serverPlan_2xCPU_4GB.HTTP == session
    }

    def "serverPlan_4xCPU_8GB builder"() {
        when:
        def serverPlan_4xCPU_8GB = serverPlan_4xCPU_8GB {}

        then:
        serverPlan_4xCPU_8GB instanceof ServerPlan_4xCPU_8GB
    }

    def "serverPlan_4xCPU_8GB without config"() {
        when:
        def serverPlan_4xCPU_8GB = serverPlan_4xCPU_8GB()

        then:
        serverPlan_4xCPU_8GB instanceof ServerPlan_4xCPU_8GB
    }

    def "serverPlan_4xCPU_8GB with kwargs and config"() {
        when:
        def serverPlan_4xCPU_8GB = serverPlan_4xCPU_8GB HTTP: session, {}

        then:
        serverPlan_4xCPU_8GB instanceof ServerPlan_4xCPU_8GB
        serverPlan_4xCPU_8GB.HTTP == session
    }

    def "serverPlan_4xCPU_8GB with kwargs and no config"() {
        when:
        def serverPlan_4xCPU_8GB = serverPlan_4xCPU_8GB HTTP: session

        then:
        serverPlan_4xCPU_8GB instanceof ServerPlan_4xCPU_8GB
        serverPlan_4xCPU_8GB.HTTP == session
    }

    def "serverPlan_6xCPU_16GB builder"() {
        when:
        def serverPlan_6xCPU_16GB = serverPlan_6xCPU_16GB {}

        then:
        serverPlan_6xCPU_16GB instanceof ServerPlan_6xCPU_16GB
    }

    def "serverPlan_6xCPU_16GB without config"() {
        when:
        def serverPlan_6xCPU_16GB = serverPlan_6xCPU_16GB()

        then:
        serverPlan_6xCPU_16GB instanceof ServerPlan_6xCPU_16GB
    }

    def "serverPlan_6xCPU_16GB with kwargs and config"() {
        when:
        def serverPlan_6xCPU_16GB = serverPlan_6xCPU_16GB HTTP: session, {}

        then:
        serverPlan_6xCPU_16GB instanceof ServerPlan_6xCPU_16GB
        serverPlan_6xCPU_16GB.HTTP == session
    }

    def "serverPlan_6xCPU_16GB with kwargs and no config"() {
        when:
        def serverPlan_6xCPU_16GB = serverPlan_6xCPU_16GB HTTP: session

        then:
        serverPlan_6xCPU_16GB instanceof ServerPlan_6xCPU_16GB
        serverPlan_6xCPU_16GB.HTTP == session
    }

    def "serverPlan_8xCPU_32GB builder"() {
        when:
        def serverPlan_8xCPU_32GB = serverPlan_8xCPU_32GB {}

        then:
        serverPlan_8xCPU_32GB instanceof ServerPlan_8xCPU_32GB
    }

    def "serverPlan_8xCPU_32GB without config"() {
        when:
        def serverPlan_8xCPU_32GB = serverPlan_8xCPU_32GB()

        then:
        serverPlan_8xCPU_32GB instanceof ServerPlan_8xCPU_32GB
    }

    def "serverPlan_8xCPU_32GB with kwargs and config"() {
        when:
        def serverPlan_8xCPU_32GB = serverPlan_8xCPU_32GB HTTP: session, {}

        then:
        serverPlan_8xCPU_32GB instanceof ServerPlan_8xCPU_32GB
        serverPlan_8xCPU_32GB.HTTP == session
    }

    def "serverPlan_8xCPU_32GB with kwargs and no config"() {
        when:
        def serverPlan_8xCPU_32GB = serverPlan_8xCPU_32GB HTTP: session

        then:
        serverPlan_8xCPU_32GB instanceof ServerPlan_8xCPU_32GB
        serverPlan_8xCPU_32GB.HTTP == session
    }

    def "serverPlan_12xCPU_48GB builder"() {
        when:
        def serverPlan_12xCPU_48GB = serverPlan_12xCPU_48GB {}

        then:
        serverPlan_12xCPU_48GB instanceof ServerPlan_12xCPU_48GB
    }

    def "serverPlan_12xCPU_48GB without config"() {
        when:
        def serverPlan_12xCPU_48GB = serverPlan_12xCPU_48GB()

        then:
        serverPlan_12xCPU_48GB instanceof ServerPlan_12xCPU_48GB
    }

    def "serverPlan_12xCPU_48GB with kwargs and config"() {
        when:
        def serverPlan_12xCPU_48GB = serverPlan_12xCPU_48GB HTTP: session, {}

        then:
        serverPlan_12xCPU_48GB instanceof ServerPlan_12xCPU_48GB
        serverPlan_12xCPU_48GB.HTTP == session
    }

    def "serverPlan_12xCPU_48GB with kwargs and no config"() {
        when:
        def serverPlan_12xCPU_48GB = serverPlan_12xCPU_48GB HTTP: session

        then:
        serverPlan_12xCPU_48GB instanceof ServerPlan_12xCPU_48GB
        serverPlan_12xCPU_48GB.HTTP == session
    }

    def "serverPlan_16xCPU_64GB builder"() {
        when:
        def serverPlan_16xCPU_64GB = serverPlan_16xCPU_64GB {}

        then:
        serverPlan_16xCPU_64GB instanceof ServerPlan_16xCPU_64GB
    }

    def "serverPlan_16xCPU_64GB without config"() {
        when:
        def serverPlan_16xCPU_64GB = serverPlan_16xCPU_64GB()

        then:
        serverPlan_16xCPU_64GB instanceof ServerPlan_16xCPU_64GB
    }

    def "serverPlan_16xCPU_64GB with kwargs and config"() {
        when:
        def serverPlan_16xCPU_64GB = serverPlan_16xCPU_64GB HTTP: session, {}

        then:
        serverPlan_16xCPU_64GB instanceof ServerPlan_16xCPU_64GB
        serverPlan_16xCPU_64GB.HTTP == session
    }

    def "serverPlan_16xCPU_64GB with kwargs and no config"() {
        when:
        def serverPlan_16xCPU_64GB = serverPlan_16xCPU_64GB HTTP: session

        then:
        serverPlan_16xCPU_64GB instanceof ServerPlan_16xCPU_64GB
        serverPlan_16xCPU_64GB.HTTP == session
    }

    def "serverPlan_20xCPU_96GB builder"() {
        when:
        def serverPlan_20xCPU_96GB = serverPlan_20xCPU_96GB {}

        then:
        serverPlan_20xCPU_96GB instanceof ServerPlan_20xCPU_96GB
    }

    def "serverPlan_20xCPU_96GB without config"() {
        when:
        def serverPlan_20xCPU_96GB = serverPlan_20xCPU_96GB()

        then:
        serverPlan_20xCPU_96GB instanceof ServerPlan_20xCPU_96GB
    }

    def "serverPlan_20xCPU_96GB with kwargs and config"() {
        when:
        def serverPlan_20xCPU_96GB = serverPlan_20xCPU_96GB HTTP: session, {}

        then:
        serverPlan_20xCPU_96GB instanceof ServerPlan_20xCPU_96GB
        serverPlan_20xCPU_96GB.HTTP == session
    }

    def "serverPlan_20xCPU_96GB with kwargs and no config"() {
        when:
        def serverPlan_20xCPU_96GB = serverPlan_20xCPU_96GB HTTP: session

        then:
        serverPlan_20xCPU_96GB instanceof ServerPlan_20xCPU_96GB
        serverPlan_20xCPU_96GB.HTTP == session
    }

    def "serverPlan_20xCPU_128GB builder"() {
        when:
        def serverPlan_20xCPU_128GB = serverPlan_20xCPU_128GB {}

        then:
        serverPlan_20xCPU_128GB instanceof ServerPlan_20xCPU_128GB
    }

    def "serverPlan_20xCPU_128GB without config"() {
        when:
        def serverPlan_20xCPU_128GB = serverPlan_20xCPU_128GB()

        then:
        serverPlan_20xCPU_128GB instanceof ServerPlan_20xCPU_128GB
    }

    def "serverPlan_20xCPU_128GB with kwargs and config"() {
        when:
        def serverPlan_20xCPU_128GB = serverPlan_20xCPU_128GB HTTP: session, {}

        then:
        serverPlan_20xCPU_128GB instanceof ServerPlan_20xCPU_128GB
        serverPlan_20xCPU_128GB.HTTP == session
    }

    def "serverPlan_20xCPU_128GB with kwargs and no config"() {
        when:
        def serverPlan_20xCPU_128GB = serverPlan_20xCPU_128GB HTTP: session

        then:
        serverPlan_20xCPU_128GB instanceof ServerPlan_20xCPU_128GB
        serverPlan_20xCPU_128GB.HTTP == session
    }

    def "serverSize builder"() {
        when:
        def serverSize = serverSize {}

        then:
        serverSize instanceof ServerSize
    }


    def "serverSize without config"() {
        when:
        def serverSize = serverSize()

        then:
        serverSize instanceof ServerSize
    }

    def "serverSize with kwargs and config"() {
        when:
        def serverSize = serverSize HTTP: session, {}

        then:
        serverSize instanceof ServerSize
        serverSize.HTTP == session
    }

    def "serverSize with kwargs and no config"() {
        when:
        def serverSize = serverSize HTTP: session

        then:
        serverSize instanceof ServerSize
        serverSize.HTTP == session
    }

    def "storage builder"() {
        when:
        def storage = storage {}

        then:
        storage instanceof Storage
    }

    def "storage without config"() {
        when:
        def storage = storage()

        then:
        storage instanceof Storage
    }

    def "storage with kwargs and config"() {
        when:
        def storage = storage HTTP: session, {}

        then:
        storage instanceof Storage
        storage.HTTP == session
    }

    def "storage with kwargs and no config"() {
        when:
        def storage = storage HTTP: session

        then:
        storage instanceof Storage
        storage.HTTP == session
    }

    def "storageBackup builder"() {
        when:
        def storageBackup = storageBackup {}

        then:
        storageBackup instanceof StorageBackup
    }

    def "storageBackup without config"() {
        when:
        def storageBackup = storageBackup()

        then:
        storageBackup instanceof StorageBackup
    }

    def "storageBackup with kwargs and config"() {
        when:
        def storageBackup = storageBackup HTTP: session, {}

        then:
        storageBackup instanceof StorageBackup
        storageBackup.HTTP == session
    }

    def "storageBackup with kwargs and no config"() {
        when:
        def storageBackup = storageBackup HTTP: session

        then:
        storageBackup instanceof StorageBackup
        storageBackup.HTTP == session
    }

    def "storageDevice builder"() {
        when:
        def storageDevice = storageDevice {}

        then:
        storageDevice instanceof StorageDevice
    }

    def "storageDevice without config"() {
        when:
        def storageDevice = storageDevice()

        then:
        storageDevice instanceof StorageDevice
    }

    def "storageDevice with kwargs and config"() {
        when:
        def storageDevice = storageDevice HTTP: session, {}

        then:
        storageDevice instanceof StorageDevice
        storageDevice.HTTP == session
    }

    def "storageDevice with kwargs and no config"() {
        when:
        def storageDevice = storageDevice HTTP: session

        then:
        storageDevice instanceof StorageDevice
        storageDevice.HTTP == session
    }

    def "storageHdd builder"() {
        when:
        def storageHdd = storageHdd {}

        then:
        storageHdd instanceof StorageHdd
    }

    def "storageHdd without config"() {
        when:
        def storageHdd = storageHdd()

        then:
        storageHdd instanceof StorageHdd
    }

    def "storageHdd with kwargs and config"() {
        when:
        def storageHdd = storageHdd HTTP: session, {}

        then:
        storageHdd instanceof StorageHdd
        storageHdd.HTTP == session
    }

    def "storageHdd with kwargs and no config"() {
        when:
        def storageHdd = storageHdd HTTP: session

        then:
        storageHdd instanceof StorageHdd
        storageHdd.HTTP == session
    }

    def "storageMaxiops builder"() {
        when:
        def storageMaxiops = storageMaxiops {}

        then:
        storageMaxiops instanceof StorageMaxiops
    }

    def "storageMaxiops without config"() {
        when:
        def storageMaxiops = storageMaxiops()

        then:
        storageMaxiops instanceof StorageMaxiops
    }

    def "storageMaxiops with kwargs and config"() {
        when:
        def storageMaxiops = storageMaxiops HTTP: session, {}

        then:
        storageMaxiops instanceof StorageMaxiops
        storageMaxiops.HTTP == session
    }

    def "storageMaxiops with kwargs and no config"() {
        when:
        def storageMaxiops = storageMaxiops HTTP: session

        then:
        storageMaxiops instanceof StorageMaxiops
        storageMaxiops.HTTP == session
    }

    def "tag builder"() {
        when:
        def tag = tag {}

        then:
        tag instanceof Tag
    }

    def "tag without config"() {
        when:
        def tag = tag()

        then:
        tag instanceof Tag
    }

    def "tag with kwargs and config"() {
        when:
        def tag = tag HTTP: session, {}

        then:
        tag instanceof Tag
        tag.HTTP == session
    }

    def "tag with kwargs and no config"() {
        when:
        def tag = tag HTTP: session

        then:
        tag instanceof Tag
        tag.HTTP == session
    }

    def "trialResourceLimits builder"() {
        when:
        def trialResourceLimits = trialResourceLimits {}

        then:
        trialResourceLimits instanceof TrialResourceLimits
    }

    def "trialResourceLimits without config"() {
        when:
        def trialResourceLimits = trialResourceLimits()

        then:
        trialResourceLimits instanceof TrialResourceLimits
    }

    def "trialResourceLimits with kwargs and config"() {
        when:
        def trialResourceLimits = trialResourceLimits HTTP: session, {}

        then:
        trialResourceLimits instanceof TrialResourceLimits
        trialResourceLimits.HTTP == session
    }

    def "trialResourceLimits with kwargs and no config"() {
        when:
        def trialResourceLimits = trialResourceLimits HTTP: session

        then:
        trialResourceLimits instanceof TrialResourceLimits
        trialResourceLimits.HTTP == session
    }

    def "zone builder"() {
        when:
        def zone = zone {}

        then:
        zone instanceof Zone
    }

    def "zone without config"() {
        when:
        def zone = zone()

        then:
        zone instanceof Zone
    }

    def "zone with kwargs and config"() {
        when:
        def zone = zone HTTP: session, {}

        then:
        zone instanceof Zone
        zone.HTTP == session
    }

    def "zone with kwargs and no config"() {
        when:
        def zone = zone HTTP: session

        then:
        zone instanceof Zone
        zone.HTTP == session
    }
}
