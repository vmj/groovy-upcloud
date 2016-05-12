package fi.linuxbox.upcloud.builder

import spock.lang.*

import fi.linuxbox.upcloud.core.*

import static ResourceBuilder.*

/**
 *
 */
@Stepwise
class ResourceBuilderSpec extends Specification{

    def "Configuring an existing resource"() {
        given:
            Resource resource = new Resource()

        when:
            configure resource, {
                hostname = 'server1.example.com'
                coreNumber = '1'
                memoryAmount = '2048'
            }

        then:
            resource?.hostname == 'server1.example.com'
            resource.coreNumber == '1'
            resource.memoryAmount == '2048'
    }

    def "Configuration no-op"() {
        given:
            def someResource = new Resource()

        when:
            def resource = configure someResource

        then:
            resource?.class.simpleName == 'Resource'
    }

    def "Named resource creation and configuration"() {
        when:
            def resource = build 'ipAddress', {
                hostname = 'server1.example.com'
                coreNumber = '1'
                memoryAmount = '2048'
            }

        then:
            resource?.class.simpleName == 'IpAddress'
            resource.hostname == 'server1.example.com'
            resource.coreNumber == '1'
            resource.memoryAmount == '2048'
    }

    def "Named resource creation without configuration"() {
        when:
            def server = build 'portRangeStart'

        then:
            server?.class.simpleName == 'PortRangeStart'
    }

    def "Custom resource creation and configuration"() {
        given:
            def builder = new ResourceBuilder()

        when:
            def resource = builder.dockerImage {
                image = 'nginx:latest'
                state = 'running'
            }

        then:
            resource?.class.simpleName == 'DockerImage'
            resource.image == 'nginx:latest'
            resource.state == 'running'
    }

    def "Custom resource creation without configuration"() {
        given:
            def builder = new ResourceBuilder()

        when:
            def resource = builder.RancherAgent()

        then:
            resource?.class.simpleName == 'RancherAgent'
    }

    def "Server creation and configuration"() {
        when:
            def server = server {
                hostname = 'server1.example.com'
                coreNumber = '1'
                memoryAmount = '2048'
            }

        then:
            server?.class.simpleName == 'Server'
            server.hostname == 'server1.example.com'
            server.coreNumber == '1'
            server.memoryAmount == '2048'
    }

    def "Server creation without configuration"() {
        when:
            def resource = server()

        then:
            resource?.class.simpleName == 'Server'
    }
}
