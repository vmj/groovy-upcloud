package fi.linuxbox.upcloud.resource

import groovy.transform.InheritConstructors

import fi.linuxbox.upcloud.core.*

/**
 * Tag representation.
 *
 * <h4>Listing tags</h4>
 * <p>
 *     All the tags associated with the UpCloud account are available via the
 *     {@link fi.linuxbox.upcloud.api.UpCloud#tags(def) Tags} API.  Tags associated with an individual server are
 *     seen in the {@link Server#tags} property.
 * </p>
 *
 * <h4>Creating tags</h4>
 * <p>
 *     Tags can be created by first using the
 *     {@link fi.linuxbox.upcloud.resource.Builder#tag(groovy.lang.Closure) Builder} API, and then passing the tag
 *     to the
 *     {@link fi.linuxbox.upcloud.api.UpCloud#create(fi.linuxbox.upcloud.core.Resource, def) Resource creation} API.
 * </p>
 * <p>
 *     The only required property is the {@link #name} property.  Existing servers can be tagged at the same time.
 * </p>
 * <pre><code class="groovy">
 *     import static fi.linuxbox.upcloud.resource.Builder.tag
 *
 *     def dev = tag {
 *         name = 'DEV'
 *         description = 'Development servers'
 *         servers = [
 *              existingServer.uuid
 *         ]
 *     }
 *
 *     upcloud.create dev, { resp, err ->
 *         ...
 *     }
 * </code></pre>
 *
 * <h4>Modifying tags</h4>
 * <p>
 *     Tags can be modified by first using the
 *     {@link fi.linuxbox.upcloud.resource.Builder#tag(groovy.lang.Closure) Builder} API, and then passing the tag
 *     to the
 *     {@link fi.linuxbox.upcloud.api.Tag#update(fi.linuxbox.upcloud.core.Resource, def) Tag update} API.
 * </p>
 * <p>
 *     The only required property is the {@link #name} property.  Existing servers can be tagged at the same time.
 * </p>
 * <pre><code class="groovy">
 *     import fi.linuxbox.upcloud.api.Tag
 *     import static fi.linuxbox.upcloud.resource.Builder.tag
 *
 *     def prod = tag {
 *         name = 'PROD'
 *         description = 'Production servers'
 *     }
 *
 *     def tagApi = dev.withTraits(Tag)
 *     tagApi.update prod, { resp, err ->
 *         ...
 *     }
 * </code></pre>
 *
 * <h4>Deleting tags</h4>
 * <p>
 *     Tags must be deleted one by one using the
 *     {@link fi.linuxbox.upcloud.api.Tag#delete(def) Tag delete} API.  Deleting a tag will automatically remove that
 *     tag from any servers.
 * </p>
 * <pre><code class="groovy">
 *     import fi.linuxbox.upcloud.api.Tag
 *     import static fi.linuxbox.upcloud.resource.Builder.tag
 *
 *     def prod = tag SESSION: session, {
 *         name = 'PROD'
 *     }
 *
 *     def tagApi = prod.withTraits(Tag)
 *
 *     tagApi.delete { resp, err ->
 *         ...
 *     }
 * </code></pre>
 * <p>
 *     Deleting all the tags requires first listing the tags and then deleting them one by one (but concurrently).
 * </p>
 * <pre><code class="groovy">
 *     import java.util.concurrent.CompletableFuture
 *     import fi.linuxbox.upcloud.api.Tag
 *
 *     def tagsDeleted = new CompletableFuture()
 *
 *     upcloud.tags { resp ->
 *         def tagDeletions = resp.tags.collect { tag ->
 *             tag.withTraits(Tag).delete {}
 *         }
 *
 *         CompletableFuture.allOf(*tagDeletions).thenRun {
 *             tagsDeleted.complete(null)
 *         }
 *     }
 *
 *     tagsDeleted.thenRun {
 *         close()
 *     }
 * </code></pre>
 *
 * <h4>Assinging and removing tags from a server</h4>
 * <p>
 *     A special case of tag modification is adding one or more tags to one server.  This can be done
 *     like shown above, adding the server to one tag at a time.  But more efficient way is to use the
 *     {@link fi.linuxbox.upcloud.api.Server#addTags(def, def) Server tagging} API.  Similarly, removing tags from
 *     one server can be done using the {@link fi.linuxbox.upcloud.api.Server#deleteTags(def, def) Server untagging}
 *     API.
 * </p>

 */
@InheritConstructors
class Tag extends Resource {
    /**
     * Description of the tag: max 255 characters.
     * <p>
     *     This is available in the tag listing, and can optionally be specified when creating or modifying a tag.
     * </p>
     */
    String description
    /**
     * Unique name of the tag: 1-32 characters {@code A-Z}, {@code a-z}, {@code 0-9}, {@code _}.
     * <p>
     *     This is available in the tag listing, and must be specified when creating a tag.  When modifying a tag,
     *     this is optional and allows renaming.
     * </p>
     */
    String name
    /**
     * Unique identifiers of the servers tagged with this tag.
     * <p>
     *     This is available in the tag listing, and can optionally be specified when creating or modifying a tag.
     * </p>
     */
    List<String> servers
}
