package fi.linuxbox.upcloud.resource

import fi.linuxbox.upcloud.core.Resource
import groovy.transform.InheritConstructors

/**
 * Backup rule of a {@link Storage}.
 */
@InheritConstructors
class BackupRule extends Resource {
    /**
     * The weekday when the backup is created: {@code daily}, {@code mon}, {@code tue}, {@code wed}, {@code thu},
     * {@code fri}, {@code sat}, or {@code sun}.
     * <p>
     *     If {@code daily} is selected, backups are made every day at the same time.
     * </p>
     */
    String interval
    /**
     * The time of day when the backup is created: {@code 0000} - {@code 2359}.
     */
    String time
    /**
     * The number of days before a backup is automatically deleted: {@code 1} - {@code 1095}.
     * <p>
     *     The maximum retention period is three years (1095 days).
     * </p>
     */
    String retention
}
