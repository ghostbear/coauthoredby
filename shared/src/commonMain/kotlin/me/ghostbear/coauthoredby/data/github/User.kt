package me.ghostbear.coauthoredby.data.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = UserSerializer::class)
sealed interface User {
    val login: String
    val id: Long
    val userViewType: String?
    val nodeId: String
    val avatarUrl: String
    val gravatarId: String?
    val url: String
    val htmlUrl: String
    val followersUrl: String
    val followingUrl: String
    val gistsUrl: String
    val starredUrl: String
    val subscriptionsUrl: String
    val organizationsUrl: String
    val reposUrl: String
    val eventsUrl: String
    val receivedEventsUrl: String
    val type: String
    val siteAdmin: Boolean
    val name: String?
    val company: String?
    val blog: String?
    val location: String?
    val email: String?
    val notificationEmail: String?
    val hireable: Boolean?
    val bio: String?
    val twitterUsername: String?
    val publicRepos: Int
    val publicGists: Int
    val followers: Int
    val following: Int
    val createdAt: String
    val updatedAt: String
    val plan: Plan?
    val privateGists: Int?
    val totalPrivateRepos: Int?
    val ownedPrivateRepos: Int?
    val diskUsage: Int?
    val collaborators: Int?
}

@Serializable
data class Plan(
    @SerialName("collaborators") val collaborators: Int,
    @SerialName("name") val name: String,
    @SerialName("space") val space: Int,
    @SerialName("private_repos") val privateRepos: Int
)

@Serializable
data class PrivateUser(
    @SerialName("login") override val login: String,
    @SerialName("id") override val id: Long,
    @SerialName("user_view_type") override val userViewType: String? = null,
    @SerialName("node_id") override val nodeId: String,
    @SerialName("avatar_url") override val avatarUrl: String,
    @SerialName("gravatar_id") override val gravatarId: String?,
    @SerialName("url") override val url: String,
    @SerialName("html_url") override val htmlUrl: String,
    @SerialName("followers_url") override val followersUrl: String,
    @SerialName("following_url") override val followingUrl: String,
    @SerialName("gists_url") override val gistsUrl: String,
    @SerialName("starred_url") override val starredUrl: String,
    @SerialName("subscriptions_url") override val subscriptionsUrl: String,
    @SerialName("organizations_url") override val organizationsUrl: String,
    @SerialName("repos_url") override val reposUrl: String,
    @SerialName("events_url") override val eventsUrl: String,
    @SerialName("received_events_url") override val receivedEventsUrl: String,
    @SerialName("type") override val type: String,
    @SerialName("site_admin") override val siteAdmin: Boolean,
    @SerialName("name") override val name: String?,
    @SerialName("company") override val company: String?,
    @SerialName("blog") override val blog: String?,
    @SerialName("location") override val location: String?,
    @SerialName("email") override val email: String?,
    @SerialName("notification_email") override val notificationEmail: String? = null,
    @SerialName("hireable") override val hireable: Boolean?,
    @SerialName("bio") override val bio: String?,
    @SerialName("twitter_username") override val twitterUsername: String? = null,
    @SerialName("public_repos") override val publicRepos: Int,
    @SerialName("public_gists") override val publicGists: Int,
    @SerialName("followers") override val followers: Int,
    @SerialName("following") override val following: Int,
    @SerialName("created_at") override val createdAt: String,
    @SerialName("updated_at") override val updatedAt: String,
    @SerialName("private_gists") override val privateGists: Int,
    @SerialName("total_private_repos") override val totalPrivateRepos: Int,
    @SerialName("owned_private_repos") override val ownedPrivateRepos: Int,
    @SerialName("disk_usage") override val diskUsage: Int,
    @SerialName("collaborators") override val collaborators: Int,
    @SerialName("two_factor_authentication") val twoFactorAuthentication: Boolean,
    @SerialName("plan") override val plan: Plan? = null,
    @SerialName("business_plus") val businessPlus: Boolean? = null,
    @SerialName("ldap_dn") val ldapDn: String? = null
) : User

@Serializable
data class PublicUser(
    @SerialName("login") override val login: String,
    @SerialName("id") override val id: Long,
    @SerialName("user_view_type") override val userViewType: String? = null,
    @SerialName("node_id") override val nodeId: String,
    @SerialName("avatar_url") override val avatarUrl: String,
    @SerialName("gravatar_id") override val gravatarId: String?,
    @SerialName("url") override val url: String,
    @SerialName("html_url") override val htmlUrl: String,
    @SerialName("followers_url") override val followersUrl: String,
    @SerialName("following_url") override val followingUrl: String,
    @SerialName("gists_url") override val gistsUrl: String,
    @SerialName("starred_url") override val starredUrl: String,
    @SerialName("subscriptions_url") override val subscriptionsUrl: String,
    @SerialName("organizations_url") override val organizationsUrl: String,
    @SerialName("repos_url") override val reposUrl: String,
    @SerialName("events_url") override val eventsUrl: String,
    @SerialName("received_events_url") override val receivedEventsUrl: String,
    @SerialName("type") override val type: String,
    @SerialName("site_admin") override val siteAdmin: Boolean,
    @SerialName("name") override val name: String?,
    @SerialName("company") override val company: String?,
    @SerialName("blog") override val blog: String?,
    @SerialName("location") override val location: String?,
    @SerialName("email") override val email: String?,
    @SerialName("notification_email") override val notificationEmail: String? = null,
    @SerialName("hireable") override val hireable: Boolean?,
    @SerialName("bio") override val bio: String?,
    @SerialName("twitter_username") override val twitterUsername: String? = null,
    @SerialName("public_repos") override val publicRepos: Int,
    @SerialName("public_gists") override val publicGists: Int,
    @SerialName("followers") override val followers: Int,
    @SerialName("following") override val following: Int,
    @SerialName("created_at") override val createdAt: String,
    @SerialName("updated_at") override val updatedAt: String,
    @SerialName("plan") override val plan: Plan? = null,
    @SerialName("private_gists") override val privateGists: Int? = null,
    @SerialName("total_private_repos") override val totalPrivateRepos: Int? = null,
    @SerialName("owned_private_repos") override val ownedPrivateRepos: Int? = null,
    @SerialName("disk_usage") override val diskUsage: Int? = null,
    @SerialName("collaborators") override val collaborators: Int? = null
) : User

object UserSerializer : JsonContentPolymorphicSerializer<User>(User::class) {
    override fun selectDeserializer(element: JsonElement) = when {
        "two_factor_authentication" in element.jsonObject -> PrivateUser.serializer()
        else -> PublicUser.serializer()
    }
}
