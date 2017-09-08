package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * UpCloud API account information.
 * <p>
 *     An instance of this is typically fetched from {@link fi.linuxbox.upcloud.api.UpCloud#account(def) Account API}.
 * </p>
 */
@InheritConstructors
class Account extends Resource {
    /**
     * Number of credits still available on the UpCloud account.
     * <p>
     *     The credits correspond to the super-account of the UpCloud API account.
     *     The API users do not have their own credits.
     * </p>
     */
    String credits
    /**
     * The UpCloud API username.
     * <p>
     *     This is typically the sub-account of the UpCloud account.
     * </p>
     */
    String username
}
