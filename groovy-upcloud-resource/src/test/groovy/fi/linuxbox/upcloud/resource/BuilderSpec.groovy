package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.http.spi.HTTP
import spock.lang.*

import fi.linuxbox.upcloud.core.*

import static fi.linuxbox.upcloud.resource.Builder.*

/**
 *
 */
class BuilderSpec extends Specification {

    Session session = new Session(Mock(HTTP), null, null, null)

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
        def account = account SESSION: session, {}

        then:
        account instanceof Account
    }

    def "account with kwargs and no config"() {
        when:
        def account = account SESSION: session

        then:
        account instanceof Account
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
        def backupRule = backupRule SESSION: session, {}

        then:
        backupRule instanceof BackupRule
    }

    def "backupRule with kwargs and no config"() {
        when:
        def backupRule = backupRule SESSION: session

        then:
        backupRule instanceof BackupRule
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
        def firewall = firewall SESSION: session, {}

        then:
        firewall instanceof Firewall
    }

    def "firewall with kwargs and no config"() {
        when:
        def firewall = firewall SESSION: session

        then:
        firewall instanceof Firewall
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
        def firewallRule = firewallRule SESSION: session, {}

        then:
        firewallRule instanceof FirewallRule
    }

    def "firewallRule with kwargs and no config"() {
        when:
        def firewallRule = firewallRule SESSION: session

        then:
        firewallRule instanceof FirewallRule
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
        def ioRequestBackup = ioRequestBackup SESSION: session, {}

        then:
        ioRequestBackup instanceof IoRequestBackup
    }

    def "ioRequestBackup with kwargs and no config"() {
        when:
        def ioRequestBackup = ioRequestBackup SESSION: session

        then:
        ioRequestBackup instanceof IoRequestBackup
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
        def ioRequestHdd = ioRequestHdd SESSION: session, {}

        then:
        ioRequestHdd instanceof IoRequestHdd
    }

    def "ioRequestHdd with kwargs and no config"() {
        when:
        def ioRequestHdd = ioRequestHdd SESSION: session

        then:
        ioRequestHdd instanceof IoRequestHdd
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
        def ioRequestMaxiops = ioRequestMaxiops SESSION: session, {}

        then:
        ioRequestMaxiops instanceof IoRequestMaxiops
    }

    def "ioRequestMaxiops with kwargs and no config"() {
        when:
        def ioRequestMaxiops = ioRequestMaxiops SESSION: session

        then:
        ioRequestMaxiops instanceof IoRequestMaxiops
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
        def ipAddress = ipAddress SESSION: session, {}

        then:
        ipAddress instanceof IpAddress
    }

    def "ipAddress with kwargs and no config"() {
        when:
        def ipAddress = ipAddress SESSION: session

        then:
        ipAddress instanceof IpAddress
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
        def ipv4Address = ipv4Address SESSION: session, {}

        then:
        ipv4Address instanceof Ipv4Address
    }

    def "ipv4Address with kwargs and no config"() {
        when:
        def ipv4Address = ipv4Address SESSION: session

        then:
        ipv4Address instanceof Ipv4Address
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
        def ipv6Address = ipv6Address SESSION: session, {}

        then:
        ipv6Address instanceof Ipv6Address
    }

    def "ipv6Address with kwargs and no config"() {
        when:
        def ipv6Address = ipv6Address SESSION: session

        then:
        ipv6Address instanceof Ipv6Address
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
        def loginUser = loginUser SESSION: session, {}

        then:
        loginUser instanceof LoginUser
    }

    def "loginUser with kwargs and no config"() {
        when:
        def loginUser = loginUser SESSION: session

        then:
        loginUser instanceof LoginUser
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
        def plan = plan SESSION: session, {}

        then:
        plan instanceof Plan
    }

    def "plan with kwargs and no config"() {
        when:
        def plan = plan SESSION: session

        then:
        plan instanceof Plan
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
        def publicIpv4BandwidthIn = publicIpv4BandwidthIn SESSION: session, {}

        then:
        publicIpv4BandwidthIn instanceof PublicIpv4BandwidthIn
    }

    def "publicIpv4BandwidthIn with kwargs and no config"() {
        when:
        def publicIpv4BandwidthIn = publicIpv4BandwidthIn SESSION: session

        then:
        publicIpv4BandwidthIn instanceof PublicIpv4BandwidthIn
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
        def publicIpv4BandwidthOut = publicIpv4BandwidthOut SESSION: session, {}

        then:
        publicIpv4BandwidthOut instanceof PublicIpv4BandwidthOut
    }

    def "publicIpv4BandwidthOut with kwargs and no config"() {
        when:
        def publicIpv4BandwidthOut = publicIpv4BandwidthOut SESSION: session

        then:
        publicIpv4BandwidthOut instanceof PublicIpv4BandwidthOut
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
        def publicIpv6BandwidthIn = publicIpv6BandwidthIn SESSION: session, {}

        then:
        publicIpv6BandwidthIn instanceof PublicIpv6BandwidthIn
    }

    def "publicIpv6BandwidthIn with kwargs and no config"() {
        when:
        def publicIpv6BandwidthIn = publicIpv6BandwidthIn SESSION: session

        then:
        publicIpv6BandwidthIn instanceof PublicIpv6BandwidthIn
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
        def publicIpv6BandwidthOut = publicIpv6BandwidthOut SESSION: session, {}

        then:
        publicIpv6BandwidthOut instanceof PublicIpv6BandwidthOut
    }

    def "publicIpv6BandwidthOut with kwargs and no config"() {
        when:
        def publicIpv6BandwidthOut = publicIpv6BandwidthOut SESSION: session

        then:
        publicIpv6BandwidthOut instanceof PublicIpv6BandwidthOut
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
        def server = server SESSION: session, {}

        then:
        server instanceof Server
    }

    def "server with kwargs and no config"() {
        when:
        def server = server SESSION: session

        then:
        server instanceof Server
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
        def serverCore = serverCore SESSION: session, {}

        then:
        serverCore instanceof ServerCore
    }

    def "serverCore with kwargs and no config"() {
        when:
        def serverCore = serverCore SESSION: session

        then:
        serverCore instanceof ServerCore
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
        def serverMemory = serverMemory SESSION: session, {}

        then:
        serverMemory instanceof ServerMemory
    }

    def "serverMemory with kwargs and no config"() {
        when:
        def serverMemory = serverMemory SESSION: session

        then:
        serverMemory instanceof ServerMemory
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
        def serverPlan_1xCPU_1GB = serverPlan_1xCPU_1GB SESSION: session, {}

        then:
        serverPlan_1xCPU_1GB instanceof ServerPlan_1xCPU_1GB
    }

    def "serverPlan_1xCPU_1GB with kwargs and no config"() {
        when:
        def serverPlan_1xCPU_1GB = serverPlan_1xCPU_1GB SESSION: session

        then:
        serverPlan_1xCPU_1GB instanceof ServerPlan_1xCPU_1GB
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
        def serverPlan_2xCPU_2GB = serverPlan_2xCPU_2GB SESSION: session, {}

        then:
        serverPlan_2xCPU_2GB instanceof ServerPlan_2xCPU_2GB
    }

    def "serverPlan_2xCPU_2GB with kwargs and no config"() {
        when:
        def serverPlan_2xCPU_2GB = serverPlan_2xCPU_2GB SESSION: session

        then:
        serverPlan_2xCPU_2GB instanceof ServerPlan_2xCPU_2GB
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
        def serverSize = serverSize SESSION: session, {}

        then:
        serverSize instanceof ServerSize
    }

    def "serverSize with kwargs and no config"() {
        when:
        def serverSize = serverSize SESSION: session

        then:
        serverSize instanceof ServerSize
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
        def storage = storage SESSION: session, {}

        then:
        storage instanceof Storage
    }

    def "storage with kwargs and no config"() {
        when:
        def storage = storage SESSION: session

        then:
        storage instanceof Storage
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
        def storageBackup = storageBackup SESSION: session, {}

        then:
        storageBackup instanceof StorageBackup
    }

    def "storageBackup with kwargs and no config"() {
        when:
        def storageBackup = storageBackup SESSION: session

        then:
        storageBackup instanceof StorageBackup
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
        def storageDevice = storageDevice SESSION: session, {}

        then:
        storageDevice instanceof StorageDevice
    }

    def "storageDevice with kwargs and no config"() {
        when:
        def storageDevice = storageDevice SESSION: session

        then:
        storageDevice instanceof StorageDevice
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
        def storageHdd = storageHdd SESSION: session, {}

        then:
        storageHdd instanceof StorageHdd
    }

    def "storageHdd with kwargs and no config"() {
        when:
        def storageHdd = storageHdd SESSION: session

        then:
        storageHdd instanceof StorageHdd
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
        def storageMaxiops = storageMaxiops SESSION: session, {}

        then:
        storageMaxiops instanceof StorageMaxiops
    }

    def "storageMaxiops with kwargs and no config"() {
        when:
        def storageMaxiops = storageMaxiops SESSION: session

        then:
        storageMaxiops instanceof StorageMaxiops
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
        def tag = tag SESSION: session, {}

        then:
        tag instanceof Tag
    }

    def "tag with kwargs and no config"() {
        when:
        def tag = tag SESSION: session

        then:
        tag instanceof Tag
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
        def zone = zone SESSION: session, {}

        then:
        zone instanceof Zone
    }

    def "zone with kwargs and no config"() {
        when:
        def zone = zone SESSION: session

        then:
        zone instanceof Zone
    }
}
