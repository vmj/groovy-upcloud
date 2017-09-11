package fi.linuxbox.upcloud.resource

import groovy.transform.InheritConstructors

import fi.linuxbox.upcloud.core.*

/**
 * Server details.
 * <p>
 *     A list of these, with the most relevant information, is typically fetched from
 *     {@link fi.linuxbox.upcloud.api.UpCloud#servers(def) Servers API}.  A more detailed
 *     information about a specific server can be fetched from
 *     {@link fi.linuxbox.upcloud.api.Server#load(def) Server API}.
 * </p>
 * <p>
 *     When creating a server, one would typically use
 *     {@link fi.linuxbox.upcloud.resource.Builder#server(groovy.lang.Closure)} to define an instance of server, and
 *     then pass it to
 *     {@link fi.linuxbox.upcloud.api.UpCloud#create(fi.linuxbox.upcloud.core.Resource, def)} to actually create the
 *     server.
 * </p>
 * <h4>Creating a server</h4>
 * <p>
 *     To create a server, the only required properties are {@link #hostname}, {@link #storageDevices},
 *     {@link #title}, and {@link #zone}.
 * </p>
 * <pre>
 *     import static fi.linuxbox.upcloud.resource.Builder.*
 *
 *     def web1 = server {
 *         hostname = 'web1.example.com'
 *         storageDevices = [
 *              // contents of this list depends... see below.
 *         ]
 *         title = 'frontend server 1'
 *         zone = 'fi-hel1'
 *     }
 * </pre>
 * <p>
 *     The contents of the {@link #storageDevices} list depends on whether the operating system disk of the server is
 *     going to be created
 *     by <a href="#clone_from_template">cloning from a template</a>,
 *     or by <a href="#clone_from_server">cloning the OS disk of another server</a>,
 *     or by <a href="#install_from_cdrom">creating a blank disk and installing from a CD-ROM</a>.
 * </p>
 * <p>
 *     Regardless, there are a lot of optional properties that can be set.
 * <pre>
 *     import static fi.linuxbox.upcloud.resource.Builder.*
 *
 *     def web1 = server {
 *         // skipping mandatory properties and showing only
 *         // optional properties with their default values:
 *         avoidHost = null
 *         bootOrder = 'disk'
 *         coreNumber = 1
 *         memoryAmount = 256
 *         plan = 'custom'
 *         firewall = 'on'
 *         ipAddresses = [
 *              ipAddress {
 *                  access = 'public'
 *                  family = 'IPv4'
 *              },
 *              ipAddress {
 *                  access = 'public'
 *                  family = 'IPv6'
 *              }
 *         ]
 *         nicModel = 'e1000'
 *         passwordDelivery = 'email'
 *         timezone = 'UTC'
 *         videoModel = 'vga'
 *         vnc = 'off'
 *         vncPassword = 'password' // actual default is a random string
 *     }
 * </pre>
 * <p>
 *     Note that there are a few properties that can not be set when creating a server: {@link #host},
 *     {@link #license}, {@link #state}, {@link #tags}, {@link #uuid}, {@link #vncHost}, and {@link #vncPort}.
 *     Most of these can not be manipulated from client side at all, but {@link #tags} must first be created via
 *     {@link fi.linuxbox.upcloud.api.UpCloud#create(fi.linuxbox.upcloud.core.Resource, def) Resource creation API}
 *     and then assigned to an existing server via
 *     {@link fi.linuxbox.upcloud.api.Server#addTags(def, def) Server tagging API}.
 * </p>
 * <a name="clone_from_template">&nbsp;</a>
 * <h5>Creating a server from template</h5>
 * <p>
 *     UpCloud provides templates for various operating systems.  To clone the operating system disk from such a
 *     template, the template UUID would be fetched from
 *     {@link fi.linuxbox.upcloud.api.UpCloud#storages(def) Storages API} by specifying a keyword argument
 *     {@code type: "template"}. Below, a Debian template is chosen.
 * </p>
 * <pre>
 *     import static fi.linuxbox.upcloud.resource.Builder.*
 *
 *     def web1 = server {
 *         // skipping all the other properties and showing only
 *         // storageDevices for cloning a template:
 *         storageDevices = [
 *             storageDevice {
 *                 action = "clone"
 *                 storage = "01000000-0000-4000-8000-000020030100"
 *                 title = "Debian from a template"
 *                 size = 50
 *                 tier = "maxiops"
 *             }
 *             // optionally 1-3 additional storageDevices can be
 *             // specified here, each with action create or attach
 *         ]
 *     }
 * </pre>
 * <p>
 *     Templates allows UpCloud to preconfigure the operating system at the time of server initialization.
 *     For example, UpCloud can apply the given hostname to the OS configuration, and set the administrative account
 *     password.
 * </p>
 * <p>
 *     Furthermore, if the template is a Linux template, UpCloud can create an additional user account on the server.
 * </p>
 * <pre>
 *     import static fi.linuxbox.upcloud.resource.Builder.*
 *
 *     def web1 = server {
 *         // skipping all the other properties and only showing
 *         // the optional loginUser property
 *         loginUser = loginUser {
 *              username = 'webmaster'
 *              createPassword = 'no'
 *              sshKeys = [
 *                  "ssh-rsa AAAAB3NzaC1yc2EAA[...]ptshi44x user@some.host",
 *                  "ssh-dss AAAAB3NzaC1kc3MAA[...]VHRzAA== someuser@some.other.host"
 *              ]
 *         }
 *     }
 * </pre>
 * <p>
 *     Another Linux template addition is the ability to execute an arbitrary provisioning script.
 * <p>
 * <pre>
 *     import static fi.linuxbox.upcloud.resource.Builder.*
 *
 *     def web1 = server {
 *         // skipping all the other properties and only showing
 *         // the optional userData property
 *         userData = """
 *         grep webmaster /etc/* > $HOME/install.log
 *         """
 *     }
 * </pre>
 * <p>
 *     Note that the {@link #userData} script will be executed with {@code root} privileges.
 * </p>
 * <a name="clone_from_server">&nbsp;</a>
 * <h5>Creating a server by cloning another server</h5>
 * <p>
 *     asd
 * </p>
 * <pre>
 *     import static fi.linuxbox.upcloud.resource.Builder.*
 *
 *     def web1 = server {
 *         // skipping all the other properties and showing only
 *         // storageDevices for cloning another servers' storages:
 *         storageDevices = [
 *             storageDevice {
 *                 action = "clone"
 *                 storage = "0169b4f8-051c-4a86-9484-f5b798249949"
 *             }
 *             // optionally 1-3 additional storageDevices can be
 *             // specified here, each with action create or attach
 *         ]
 *     }
 * </pre>
 * <p>
 *     This is different from cloning a template, since UpCloud has no way of knowing what's inside the storages, and
 *     no safe way to do any configuration.
 * </p>
 * <a name="install_from_cdrom">&nbsp;</a>
 * <h5>Creating a server by installing from a CD-ROM</h5>
 * <p>
 *     UpCloud provides installation CD-ROMs for various operating systems.  To install the operating system from
 *     such a CD-ROM, the CD-ROM UUID would be fetched from
 *     {@link fi.linuxbox.upcloud.api.UpCloud#storages(def) Storages API} by specifying a keyword argument
 *     {@code type: "cdrom"}. Below, a Debian CD-ROM is chosen.
 * </p>
 * <pre>
 *     import static fi.linuxbox.upcloud.resource.Builder.*
 *
 *     def web1 = server {
 *         // skipping all the other properties and showing only
 *         // storageDevices for installing from a CD-ROM:
 *         storageDevices = [
 *             storageDevice {
 *                 action = "create"
 *                 title = "Debian from scratch"
 *                 size = 20
 *                 tier = "maxiops"
 *             },
 *             storageDevice {
 *                 action = "attach"
 *                 storage = "01000000-0000-4000-8000-000020010301"
 *                 type = "cdrom"
 *             }
 *             // optionally 1-2 additional storageDevices can be
 *             // specified here, each with action create or attach
 *         ]
 *     }
 * </pre>
 * <p>
 *     One would typically use VNC to connect to the server for stepping through the installation.
 * </p>
 */
@InheritConstructors
class Server extends Resource {
    /**
     * The storage device boot order: {@code disk}, {@code cdrom}, {@code disk,cdrom}, or {@code cdrom,disk}.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code disk}.
     * </p>
     */
    String bootOrder
    /**
     * Number of CPU cores on this server.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to a smallest possible value.  If set when creating a server, this has to be a valid
     *     combination with {@link #memoryAmount} (see {@link fi.linuxbox.upcloud.api.UpCloud#serverSizes(def)}).
     *     If {@link #plan} is also set, then this must match with the selected plan.
     * </p>
     */
    String coreNumber
    /**
     * Whether the firewall is enabled for this server: {@code on} or {@code off}.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code on}.
     * </p>
     */
    String firewall
    /**
     * Host ID that hosts this virtual machine.
     * <p>
     *     This is available in the server details response, and can not be set when creating a server.
     * </p>
     */
    Long host
    /**
     * Hostname of this server.
     * <p>
     *     This is available in the server details and list responses, and must be set when creating a server.
     * </p>
     */
    String hostname
    /**
     * IP addresses of this server.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to one {@code IPv4} and one {@code IPv6} address (both {@code public}).
     * </p>
     */
    List<IpAddress> ipAddresses
    /**
     * Amount of credits per hour per CPU required by this server license.
     * <p>
     *     This property is the sum of all the attached storages' license properties.
     * </p>
     * <p>
     *     This is available in the server details response, and can not be set when creating a server.
     * </p>
     */
    Integer license
    /**
     * Amount of main memory in megabytes on this server.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to a smallest possible value.  If set when creating a server, this has to be a valid
     *     combination with {@link #coreNumber} (see {@link fi.linuxbox.upcloud.api.UpCloud#serverSizes(def)}).
     *     If {@link #plan} is also set, then this must match with the selected plan.
     * </p>
     */
    String memoryAmount
    /**
     * Type of network interface card on this server: {@code e1000}, {@code virtio}, or {@code rtl8139}.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code e1000}.
     * </p>
     */
    String nicModel
    /**
     * Name of the preconfigured server plan, or {@code custom}, for this server.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code custom}.  If this is set to something else than {@code custom}, this must match
     *     a valid plan name from {@link fi.linuxbox.upcloud.api.UpCloud#plans(def)}.  If {@link #coreNumber} and/or
     *     {@link #memoryAmount} are also set, they must match the selected plan.
     * </p>
     */
    String plan
    /**
     * Server state.
     * <p>
     *     This is available in the server details response, and can not be set when creating a server.
     * </p>
     * @see <a href="https://www.upcloud.com/api/1.2.4/8-servers/#server-states" target="_top">UpCloud API docs for server states</a>
     */
    String state
    /**
     * Storage devices attached to this server.
     * <p>
     *     This is available in the server details response, and must be set to 1-4 storage devices when creating a
     *     server.
     * </p>
     */
    List<StorageDevice> storageDevices
    /**
     * Tags assigned to this server.
     * <p>
     *     This is available in the server details response, and can not be set when creating a server.
     * </p>
     */
    List<String> tags
    /**
     * The hardware clock timezone for this server.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code UTC}.  If set, must be a valid timezone identifier (see
     *     {@link fi.linuxbox.upcloud.api.UpCloud#timezones(def)}).
     * </p>
     */
    String timezone
    /**
     * Title of this server.
     * <p>
     *     This is available in the server details response, and must be set when creating a server.
     * </p>
     */
    String title
    /**
     * Unique identifier of this server.
     * <p>
     *     This is available in the server details response, and can not be set when creating a server.
     * </p>
     */
    String uuid
    /**
     * Type of video card attached to this server: {@code vga} or {@code cirrus}.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code vga}.
     * </p>
     */
    String videoModel
    /**
     * Whether VNC is enabled on this server: {@code on} or {@code off}.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code off}.
     * </p>
     */
    String vnc
    /**
     * Hostname where VNC is available for this server.
     * <p>
     *     This is available in the server details response, and can not be set when creating a server.
     * </p>
     */
    String vncHost
    /**
     * VNC password.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to a randomly generated string.  If set, must be 8-32 alphanumeric characters
     *     ({@code a-zA-Z0-9}).
     * </p>
     */
    String vncPassword
    /**
     * TCP port number on {@link #vncHost} where the VNC server is listening for this server.
     * <p>
     *     This is available in the server details response, and can not be set when creating a server.
     * </p>
     */
    String vncPort
    /**
     * Zone ID where this server is located.
     * <p>
     *     This is available in the server details response, and must be set when creating a server.
     *     See {@link fi.linuxbox.upcloud.api.UpCloud#zones(def)}.
     * </p>
     */
    String zone
    /**
     * Administrative account to create.
     * <p>
     *     This can be set when creating a server from template.  This is not available in server detail or list
     *     responses.
     * </p>
     */
    LoginUser loginUser
    /**
     * Delivery method for administrative accounts' password: {@code none}, {@code email}, or {@code sms}.
     * <p>
     *     This can optionally be set when creating a server, and defaults to {@code email}.
     *     This is not available in server detail or list responses.
     * </p>
     */
    String passwordDelivery
    /**
     * Host to avoid.
     * <p>
     *     When creating a new server, this can be set to a {@link #host} property of another server in order to create
     *     High Availability -environment.
     * </p>
     * <p>
     *     This is not available in the server detail or list response.
     * </p>
     */
    String avoidHost
    /**
     * A valid URL or the contents of a Bash script.
     * <p>
     *     When creating a server from a Linux template, this can be set and it will be executed with root privileges
     *     upon server initialization.  Pay special attention to verifying it.
     * </p>
     */
    String userData
}
