
package ma.um6p.compusconnect;

/*
 * Enum representing the two possible types of posts
 * that students can publish on CompusConnect.
 *
 * Using an enum restricts the post type to predefined values,
 * instead of allowing arbitrary strings.
 */
public enum PostType {
    // A student is looking for help, a skill, or a service.
    NEED,
    // A student is offering help, a skill, or a service.
    HAVE
}