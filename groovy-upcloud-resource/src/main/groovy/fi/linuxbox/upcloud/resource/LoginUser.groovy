package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * Administrative account to create for a new server (from a Linux template).
 * <p>
 *     An instance of this class can be set to {@link Server#loginUser} property when creating a new server from a
 *     Linux template.
 * </p>
 */
@InheritConstructors
class LoginUser extends Resource {
    /**
     * A valid, non-reserved username.
     * <p>
     *     If set to other than {@code root}, normal user with {@code sudo} privileges is created.
     * </p>
     */
    String username
    /**
     * Whether to set administrative account's password: {@code yes} or {@code no}.
     * <p>
     *     This can be set to {@code no} if the user can login only via SSH.
     * </p>
     */
    String createPassword
    /**
     * Public SSH keys for passwordless login.
     */
    List<String> sshKeys
}
