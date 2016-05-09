package fi.linuxbox.upcloud.builder

import spock.lang.*

import fi.linuxbox.upcloud.core.*

import static fi.linuxbox.upcloud.builder.ModelBuilder.*

/**
 *
 */
@Stepwise
class ModelBuilderSpec extends Specification{

    def "Configuring an existing model"() {
        given:
            MODEL model = new MODEL()

        when:
            configure model, {
                hostname = 'server1.example.com'
                coreNumber = '1'
                memoryAmount = '2048'
            }

        then:
            model?.hostname == 'server1.example.com'
            model.coreNumber == '1'
            model.memoryAmount == '2048'
    }

    def "Configuration no-op"() {
        given:
            def someModel = new MODEL()

        when:
            def model = configure someModel

        then:
            model?.class.simpleName == 'MODEL'
    }

    def "Named model creation and configuration"() {
        when:
            def model = build 'ipAddress', {
                hostname = 'server1.example.com'
                coreNumber = '1'
                memoryAmount = '2048'
            }

        then:
            model?.class.simpleName == 'IpAddress'
            model.hostname == 'server1.example.com'
            model.coreNumber == '1'
            model.memoryAmount == '2048'
    }

    def "Named model creation without configuration"() {
        when:
            def server = build 'portRangeStart'

        then:
            server?.class.simpleName == 'PortRangeStart'
    }

    def "Custom model creation and configuration"() {
        given:
            def builder = new ModelBuilder()

        when:
            def model = builder.dockerImage {
                image = 'nginx:latest'
                state = 'running'
            }

        then:
            model?.class.simpleName == 'DockerImage'
            model.image == 'nginx:latest'
            model.state == 'running'
    }

    def "Custom model creation without configuration"() {
        given:
            def builder = new ModelBuilder()

        when:
            def model = builder.RancherAgent()

        then:
            model?.class.simpleName == 'RancherAgent'
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
            def model = server()

        then:
            model?.class.simpleName == 'Server'
    }
}
