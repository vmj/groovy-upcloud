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

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

/**
 * Server representation.
 * <p>
 *     A list of servers, with the most relevant information, is typically fetched from
 *     {@link fi.linuxbox.upcloud.api.UpCloud#servers(def) Servers API}.  A more detailed
 *     information about a specific server can be fetched from
 *     {@link fi.linuxbox.upcloud.api.Server#load(def) Server details API}.
 * </p>
 * <p>
 *     When creating a server, one would typically use
 *     {@link fi.linuxbox.upcloud.resource.Builder#server(groovy.lang.Closure) Builder API} to define an instance of
 *     a server, and then pass it to
 *     {@link fi.linuxbox.upcloud.api.UpCloud#create(fi.linuxbox.upcloud.core.Resource, def) Resource creation API}
 *     to actually create the server.
 * </p>
 * <h4>Creating a server</h4>
 * <p>
 *     To create a server, the only required properties are {@link #hostname}, {@link #storageDevices},
 *     {@link #title}, and {@link #zone}.
 * </p>
 * <pre><code class="groovy">
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
 * </code></pre>
 * <p>
 *     The contents of the {@link #storageDevices} list depends on whether the operating system (OS) disk of the
 *     server is going to be created
 *     <ul>
 *       <li>by <a href="#clone_from_template">cloning from an OS template</a>, or</li>
 *       <li>by <a href="#clone_from_server">cloning the disk(s) of another server</a>, or</li>
 *       <li>by <a href="#install_from_cdrom">creating a blank disk and installing from a CD-ROM</a>.</li>
 *     </ul>
 * </p>
 * <p>
 *     Regardless, there are a lot of optional properties that can be set.
 * <pre><code class="groovy">
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
 * </code></pre>
 * <p>
 *     There are a few properties that can not be set when creating a server: {@link #host}, {@link #license},
 *     {@link #state}, {@link #tags}, {@link #uuid}, {@link #vncHost}, and {@link #vncPort}.  Most of these can not
 *     be manipulated from client side at all.
 * </p>
 * <p>
 *     While storages and IP addresses can be created with the server, they can not be modified with the server.
 *     See {@link Storage} and {@link IpAddress} class documentation for information on how to add and remove those
 *     resources from an existing server.
 * </p>
 * <p>
 *     Tags can not be created with the server.  See {@link Tag} class documentation for information on how to create
 *     and manage tags.
 * </p>
 *
 * <a name="clone_from_template">&nbsp;</a>
 * <h5>Creating a server OS disk by cloning from an OS template</h5>
 * <p>
 *     UpCloud provides templates for various operating systems.  To clone the operating system disk from such a
 *     template, the template UUID would be fetched from
 *     {@link fi.linuxbox.upcloud.api.UpCloud#storages(def) Storages API} by specifying a keyword argument
 *     {@code type: "template"}. Below, a storage UUID of a Debian template is chosen.
 * </p>
 * <pre><code class="groovy">
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
 * </code></pre>
 * <p>
 *     Templates allows UpCloud to preconfigure the operating system at the time of server initialization.
 *     For example, UpCloud can apply the given {@link #hostname} to the OS configuration, and set the administrative
 *     account password (which would then be delivered according to {@link #passwordDelivery} property).
 * </p>
 * <p>
 *     Furthermore, if the template is a Linux template, UpCloud can create an additional user account on the server
 *     if our representation provides the optional {@link #loginUser} property.
 * </p>
 * <pre><code class="groovy">
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
 * </code></pre>
 * <p>
 *     Another Linux template addition is the ability to execute an arbitrary provisioning script by means of the
 *     optional {@link #userData} property.
 * <p>
 * <pre><code class="groovy">
 *     import static fi.linuxbox.upcloud.resource.Builder.*
 *
 *     def web1 = server {
 *         // skipping all the other properties and only showing
 *         // the optional userData property
 *         userData = """
 *         grep webmaster /etc/* > $HOME/install.log
 *         """
 *     }
 * </code></pre>
 * <p>
 *     See the property descriptions for more details.
 * </p>
 *
 * <a name="clone_from_server">&nbsp;</a>
 * <h5>Creating server storage devices by cloning the disk(s) of another server</h5>
 * <p>
 *     This use case provides a simple way to create identical servers.  Below, storage UUID of another storage that
 *     belongs to the same account and resides on the same zone is used as the source of the cloning.
 * </p>
 * <pre><code class="groovy">
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
 * </code></pre>
 * <p>
 *     This is different from cloning a template, since UpCloud has no way of knowing what's inside the storages, and
 *     no safe way to do any configuration.
 * </p>
 *
 * <a name="install_from_cdrom">&nbsp;</a>
 * <h5>Creating a server OS disk by creating a blank disk and installing from a CD-ROM</h5>
 * <p>
 *     UpCloud provides installation CD-ROMs for various operating systems.  To install the operating system from
 *     such a CD-ROM, the CD-ROM UUID would be fetched from
 *     {@link fi.linuxbox.upcloud.api.UpCloud#storages(def) Storages API} by specifying a keyword argument
 *     {@code type: "cdrom"}. Below, a storage UUID of a Debian CD-ROM is chosen.
 * </p>
 * <pre><code class="groovy">
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
 * </code></pre>
 * <p>
 *     One would typically use VNC to connect to the server for stepping through the installation.
 * </p>
 */
@CompileStatic
@InheritConstructors
class Server extends Resource {
    /**
     * The storage device boot order: {@code disk}, {@code cdrom}, {@code disk,cdrom}, or {@code cdrom,disk}.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code disk}.  This can also be modified without stopping the server.
     * </p>
     */
    String bootOrder
    /**
     * Number of CPU cores on this server.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to a smallest possible value.  If modified, the server must be stopped.
     * </p>
     * <p>
     *     If set when creating or modifying a server, this has to be a valid combination with {@link #memoryAmount}
     *     (see {@link fi.linuxbox.upcloud.api.UpCloud#serverSizes(def)}).  If {@link #plan} is also set, then this
     *     must match with the selected plan.
     * </p>
     */
    String coreNumber
    /**
     * Whether the firewall is enabled for this server: {@code on} or {@code off}.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code on}.  This can also be modified without stopping the server.
     * </p>
     */
    String firewall
    /**
     * Host ID that hosts this virtual machine.
     * <p>
     *     This is available in the server details response, and can not be set when creating or modifying a server.
     * </p>
     */
    Long host
    /**
     * Hostname of this server.
     * <p>
     *     This is available in the server details and list responses.
     * </p>
     * <p>
     *     This must be set when creating a server.  When creating the server OS disk by cloning from a template,
     *     this hostname will be reflected in the configuration files on the OS disk.  When creating the server in any
     *     other way, this hostname will <strong>not</strong> automatically be set in the OS configuration.
     * </p>
     * <p>
     *     This hostname can also be modified without stopping the server, but the modification will
     *     <strong>not</strong> automatically be changed to the configuration files on the OS disk.
     * </p>
     * <p>
     *     This has to look like a DNS hostname or label, but this is not automatically added to any DNS server.
     * </p>
     */
    String hostname
    /**
     * IP addresses of this server.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to one {@code IPv4} and one {@code IPv6} address (both {@code public}).
     *     See {@link IpAddress} class documentation for information about managing these after server creation.
     * </p>
     */
    List<IpAddress> ipAddresses
    /**
     * Amount of credits per hour per CPU required by this server license.
     * <p>
     *     This property is the sum of all the attached storages' license properties.
     * </p>
     * <p>
     *     This is available in the server details response, and can not be set when creating or modifying a server.
     * </p>
     */
    Integer license
    /**
     * Amount of main memory in megabytes on this server.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to a smallest possible value.  If modified, the server must be stopped.
     * </p>
     * <p>
     *     If set when creating or modifying a server, this has to be a valid combination with {@link #coreNumber}
     *     (see {@link fi.linuxbox.upcloud.api.UpCloud#serverSizes(def)}).  If {@link #plan} is also set, then this
     *     must match with the selected plan.
     * </p>
     */
    String memoryAmount
    /**
     * Type of network interface card on this server: {@code e1000}, {@code virtio}, or {@code rtl8139}.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code e1000}.  This can also be modified without stopping the server.
     * </p>
     */
    String nicModel
    /**
     * Name of the preconfigured server plan, or {@code custom}, for this server.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code custom}.  This can also be modified without stopping the server.
     * </p>
     * <p>
     *     If this is set to something else than {@code custom}, this must match a valid plan name from
     *     {@link fi.linuxbox.upcloud.api.UpCloud#plans(def)}.  If {@link #coreNumber} and/or {@link #memoryAmount}
     *     are also set, they must match the selected plan.
     * </p>
     */
    String plan
    /**
     * Number of bytes of IPv4 traffic included in the plan.
     * <p>
     *     This is available in the server listing and server details responses.  This can not be modified.
     * </p>
     */
    String planIpv4Bytes
    /**
     * Number of bytes of IPv6 traffic included in the plan.
     * <p>
     *     This is available in the server listing and server details responses.  This can not be modified.
     * </p>
     */
    String planIpv6Bytes
    /**
     * Server state.
     * <p>
     *     This is available in the server details response, and can not be set when creating or modifying a server.
     * </p>
     * @see <a href="https://www.upcloud.com/api/8-servers/#server-states" target="_top">UpCloud API docs for server states</a>
     */
    String state
    /**
     * Storage devices attached to this server.
     * <p>
     *     This is available in the server details response, and must be set to 1-4 storage devices when creating a
     *     server.  Modifying these after server creation needs to go through
     *     {@link fi.linuxbox.upcloud.api.UpCloud#create(fi.linuxbox.upcloud.core.Resource, def) Resource creation} and
     *     {@link fi.linuxbox.upcloud.api.Storage Storage management} APIs.
     * </p>
     */
    List<StorageDevice> storageDevices
    /**
     * Tags assigned to this server.
     * <p>
     *     This is available in the server details response, and can not be set when creating or modifying a server.
     * </p>
     */
    List<String> tags
    /**
     * The hardware clock timezone for this server.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code UTC}.  This can also be modified without stopping the server.
     * </p>
     * <p>
     *     If set, must be a valid timezone identifier (see {@link fi.linuxbox.upcloud.api.UpCloud#timezones(def)}).
     * </p>
     */
    String timezone
    /**
     * Title of this server.
     * <p>
     *     This is available in the server details response, and must be set when creating a server.
     *     This can also be modified without stopping the server.
     * </p>
     */
    String title
    /**
     * Unique identifier of this server.
     * <p>
     *     This is available in the server details response, and can not be set when creating or modifying a server.
     * </p>
     */
    String uuid
    /**
     * Type of video card attached to this server: {@code vga} or {@code cirrus}.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code vga}.  This can also be modified without stopping the server.
     * </p>
     */
    String videoModel
    /**
     * Whether VNC is enabled on this server: {@code on} or {@code off}.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to {@code off}.  This can also be modified without stopping the server.
     * </p>
     */
    String vnc
    /**
     * Hostname where VNC is available for this server.
     * <p>
     *     This is available in the server details response, and can not be set when creating or modifying a server.
     * </p>
     */
    String vncHost
    /**
     * VNC password.
     * <p>
     *     This is available in the server details response, and can optionally be set when creating a server where
     *     this defaults to a randomly generated string.  This can also be modified without stopping the server.
     * </p>
     * <p>
     *     If set, must be 8-32 alphanumeric characters ({@code a-zA-Z0-9}).
     * </p>
     */
    String vncPassword
    /**
     * TCP port number on {@link #vncHost} where the VNC server is listening for this server.
     * <p>
     *     This is available in the server details response, and can not be set when creating or modifying a server.
     * </p>
     */
    String vncPort
    /**
     * Zone ID where this server is located.
     * <p>
     *     This is available in the server details response, and must be set when creating a server.
     *     See {@link fi.linuxbox.upcloud.api.UpCloud#zones(def)}.  This can not be modified.
     * </p>
     */
    String zone
    /**
     * Administrative account to create.
     * <p>
     *     This can be set when creating a server from template.  This is not available in server detail or list
     *     responses, and can not be modified after server creation.
     * </p>
     */
    LoginUser loginUser
    /**
     * Delivery method for administrative accounts' password: {@code none}, {@code email}, or {@code sms}.
     * <p>
     *     This can optionally be set when creating a server, and defaults to {@code email}.
     *     This is not available in server detail or list responses, and can not be modified after server creation.
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
     *     This is not available in the server detail or list response, and can not be modified after server creation.
     * </p>
     */
    String avoidHost
    /**
     * A valid URL or the contents of a Bash script.
     * <p>
     *     When creating a server from a Linux template, this can be set and it will be executed with root privileges
     *     upon server initialization.  Pay special attention to verifying it.  This has no meaning after server
     *     creation.
     * </p>
     */
    String userData
}
