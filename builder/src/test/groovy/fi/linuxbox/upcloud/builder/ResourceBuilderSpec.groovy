package fi.linuxbox.upcloud.builder

import spock.lang.*

import fi.linuxbox.upcloud.core.*
import fi.linuxbox.upcloud.core.http.*

import static fi.linuxbox.upcloud.builder.ResourceBuilder.*

/**
 *
 */
class ResourceBuilderSpec extends Specification{

    API api = new API(Mock(HTTP), null, null, null)

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

    def "Named resource creation with kwargs and config"() {
        when:
            def resource = build 'Tag', API: api, {
                name = 'DEV'
            }

        then:
            resource?.API != null
            resource.name == 'DEV'
    }

    def "Named resource creation with kwargs and no config"() {
        when:
            def resource = build 'Tag', API: api

        then:
            resource?.API != null
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

    def "Custom resource creation with kwargs and config"() {
        given:
            def builder = new ResourceBuilder()

        when:
            def resource = builder.dockerImage API: api, {
                image = 'nginx:stable'
            }

        then:
            resource?.API != null
            resource.image == 'nginx:stable'
    }

    def "Custom resource creation with kwags and no config"() {
        given:
            def builder = new ResourceBuilder()

        when:
            def resource = builder.RancherAgent API: api

        then:
            resource?.API != null
    }
}
