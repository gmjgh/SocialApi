package com.social.api.presentation.security.oauth2.user

class FacebookOAuth2UserInfo(attributes: Map<String, Any>) : OAuth2UserInfo {

    override val id: String = attributes["id"].toString()

    override val name: String = attributes["name"].toString()

    override val email: String = attributes["email"].toString()

    override val imageUrl: String? =
            if (attributes.containsKey("picture")) {
                val pictureObj = attributes["picture"] as Map<String, Any>
                if (pictureObj.containsKey("data")) {
                    val dataObj = pictureObj["data"] as Map<String, Any>
                    if (dataObj.containsKey("url")) {
                        dataObj["url"] as String
                    } else null
                } else null
            } else null
}
