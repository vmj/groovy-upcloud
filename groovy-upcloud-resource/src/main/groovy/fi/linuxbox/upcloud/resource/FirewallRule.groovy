/*
 * Groovy UpCloud library - Resource Module
 * Copyright (C) 2018  <mikko@varri.fi>
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
 * Firewall rule of a {@link Server}.
 * <p>
 *     Each server has its own list of firewall rules, with a maximum of 1000 rules.  The firewall of a server can be
 *     disabled or enabled without stopping the server by toggling the {@link Server#firewall} property.
 * </p>
 * <p>
 *     The network traffic (each packet) is checked against the list of firewall rules.  The rules are checked in
 *     order from first rule ({@link #position} {@code 1}) onwards.  The {@link #action} property of the first rule
 *     that matches the packet determines what happens to the packet, and the rest of the rules are ignored.
 * </p>
 * <p>
 *     The last rule is a special case and corresponds to Default Rule set through Control Panel. It should contain
 *     only {@link #direction} and {@link #action} properties in addition of {@link #position}.
 * </p>
 *
 * <h4>Listing firewall rules</h4>
 * <p>
 *     A list of firewall rules for a server can be fetched using the
 *     {@link fi.linuxbox.upcloud.api.Server#firewallRules(def) Firewall Rules} API.
 * </p>
 *
 * <h4>Creating firewall rules</h4>
 * <p>
 *     Firewall rules must be created one by one.
 *     The description of the firewall rule can be created using the
 *     {@link fi.linuxbox.upcloud.resource.Builder#firewallRule(groovy.lang.Closure) Builder} API, and the
 *     description then passed to the
 *     {@link fi.linuxbox.upcloud.api.Server#createFirewallRule(fi.linuxbox.upcloud.core.Resource, def) Firewall creation}
 *     API.
 * </p>
 * <pre><code class="groovy">
 *     import static fi.linuxbox.upcloud.resource.Builder.firewallRule
 *
 *     def acceptHttp = firewallRule {
 *         action = 'accept'
 *         comment = 'Allow HTTP from anywhere'
 *         destinationPortEnd = '80'
 *         destinationPortStart = '80'
 *         direction = 'in'
 *         family = 'IPv4'
 *         protocol = 'tcp'
 *     }
 *
 *     serverApi.createFirewallRule acceptHttp, { resp, err ->
 *         assert resp?.firewallRule instanceof FirewallRule
 *     }
 * </code></pre>
 * <p>
 *     If the {@link #position} property is not specified, then the rule will be appended to the list of rules.  If
 *     the {@link #position} is specified, then the rule is inserted at that position, and any following rules will
 *     have their positions increaded by one.
 * </p>
 *
 * <h4>Modifying firewall rules</h4>
 * <p>
 *     Firewall rules can not be modified.  Modification must be emulated by deleting the rule that is to be updated,
 *     and then creating a new rule with the same {@link #position} as the old rule.
 * </p>
 *
 * <h4>Deleting firewall rules</h4>
 * <p>
 *     Firewall rules must be removed one by one using the
 *     {@link fi.linuxbox.upcloud.api.Server#deleteFirewallRule(def, def) Firewall rule deletion} API.
 * </p>
 * <pre><code class="groovy">
 *     serverApi.deleteFirewallRule position, { resp, err ->
 *         ...
 *     }
 * </code></pre>
 * <p>
 *     If the removed rule is not the last rule, the {@link #position} property of the following rules will be
 *     decreased by one.
 * </p>
 */
@InheritConstructors
class FirewallRule extends Resource {
    /**
     * Action to take if a network packet matches this firewall rule: {@code accept}, or {@code drop}.
     * <p>
     *     This is available in all firewall rule responses, and must be set when creating firewall rules.
     * </p>
     * <p>
     *     Action {@code accept} lets the packet through.  Action {@code drop} blocks the packet without sending an
     *     error.
     * </p>
     */
    String action
    /**
     * Human readable explanation of this firewall rule.
     * <p>
     *     This is available in all firewall rule responses, and can optionally be set when creating a firewall rule.
     * </p>
     */
    String comment
    /**
     * The end of the destination IP address range this rule applies to.
     * <p>
     *     This is a string representation of the IP address:
     *     either in dotted decimal notation (for {@code IPv4} {@link #family}),
     *     or hexadecimal representation (for {@code IPv6} {@link #family}).
     * </p>
     * <p>
     *     This is available in all firewall rule responses, and can optionally be set when creating a firewall rule.
     *     If this is set, then {@link #destinationAddressStart} property must be set also.
     *     If {@link #destinationAddressStart} property is set, then this must be set also.
     * </p>
     */
    String destinationAddressEnd
    /**
     * The start of the destination IP address range this rule applies to.
     * <p>
     *     This is a string representation of the IP address:
     *     either in dotted decimal notation (for {@code IPv4} {@link #family}),
     *     or hexadecimal representation (for {@code IPv6} {@link #family}).
     * </p>
     * <p>
     *     This is available in all firewall rule responses, and can optionally be set when creating a firewall rule.
     *     If this is set, then {@link #destinationAddressEnd} property must be set also.
     *     If {@link #destinationAddressEnd} property is set, then this must be set also.
     * </p>
     */
    String destinationAddressStart
    /**
     * The end of the destination (TCP/UDP) port range this rule applies to: {@code 1}-{@code 65535}.
     * <p>
     *     This is available in all firewall rule responses, and can optionally be set when creating a firewall rule.
     *     If this is set, then {@link #destinationPortStart} property must be set also.
     *     If {@link #destinationPortStart} property is set, then this must be set also.
     * </p>
     */
    String destinationPortEnd
    /**
     * The start of the destination (TCP/UDP) port range this rule applies to: {@code 1}-{@code 65535}.
     * <p>
     *     This is available in all firewall rule responses, and can optionally be set when creating a firewall rule.
     *     If this is set, then {@link #destinationPortEnd} property must be set also.
     *     If {@link #destinationPortEnd} property is set, then this must be set also.
     * </p>
     */
    String destinationPortStart
    /**
     * Whether this rule applies to incoming or outgoing traffic: {@code in} or {@code out}.
     * <p>
     *     This is available in all firewall rule responses, and must be set when creating firewall rules.
     * </p>
     */
    String direction
    /**
     * The IP address family this rule applies to: {@code IPv4} or {@code IPv6}.
     * <p>
     *     This is available in all firewall rule responses, and must be set when creating firewall rules
     *     <strong>if</strong> {@link #protocol} is set.
     * </p>
     */
    String family
    /**
     * The ICMP type this rule applies to: {@code 1}-{@code 255}.
     * <p>
     *     This is available in all firewall rule responses, and can optionally be set when creating a firewall rule.
     * </p>
     */
    String icmpType
    /**
     * Position of this firewall rule in the server's firewall rule list.
     * <p>
     *     This is available in all firewall rule responses, and can optionally be set when creating a firewall rule.
     * </p>
     * <p>
     *     Position of the first rule is {@code 1} and the maximum number of rules per server is {@code 1000}.
     * </p>
     */
    String position
    /**
     * The network protocol this rule applies to: {@code tcp}, {@code udp}, or {@code icmp}.
     * <p>
     *     This is available in all firewall rule responses, and can optionally be set when creating a firewall rule.
     *     If this is set when creating a rule, the {@link #family} property must be set also.
     * </p>
     */
    String protocol
    /**
     * The end of the source IP address range this rule applies to.
     * <p>
     *     This is a string representation of the IP address:
     *     either in dotted decimal notation (for {@code IPv4} {@link #family}),
     *     or hexadecimal representation (for {@code IPv6} {@link #family}).
     * </p>
     * <p>
     *     This is available in all firewall rule responses, and can optionally be set when creating a firewall rule.
     *     If this is set, then {@link #sourceAddressStart} property must be set also.
     *     If {@link #sourceAddressStart} property is set, then this must be set also.
     * </p>
     */
    String sourceAddressEnd
    /**
     * The start of the source IP address range this rule applies to.
     * <p>
     *     This is a string representation of the IP address:
     *     either in dotted decimal notation (for {@code IPv4} {@link #family}),
     *     or hexadecimal representation (for {@code IPv6} {@link #family}).
     * </p>
     * <p>
     *     This is available in all firewall rule responses, and can optionally be set when creating a firewall rule.
     *     If this is set, then {@link #sourceAddressEnd} property must be set also.
     *     If {@link #sourceAddressEnd} property is set, then this must be set also.
     * </p>
     */
    String sourceAddressStart
    /**
     * The end of the source (TCP/UDP) port range this rule applies to: {@code 1}-{@code 65535}.
     * <p>
     *     This is available in all firewall rule responses, and can optionally be set when creating a firewall rule.
     *     If this is set, then {@link #sourcePortStart} property must be set also.
     *     If {@link #sourcePortStart} property is set, then this must be set also.
     * </p>
     */
    String sourcePortEnd
    /**
     * The start of the source (TCP/UDP) port range this rule applies to: {@code 1}-{@code 65535}.
     * <p>
     *     This is available in all firewall rule responses, and can optionally be set when creating a firewall rule.
     *     If this is set, then {@link #sourcePortEnd} property must be set also.
     *     If {@link #sourcePortEnd} property is set, then this must be set also.
     * </p>
     */
    String sourcePortStart
}
