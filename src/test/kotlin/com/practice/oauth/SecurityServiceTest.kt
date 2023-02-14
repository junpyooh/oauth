package com.practice.oauth

import com.practice.oauth.config.properties.OAuth2Properties
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestTemplate

@SpringBootTest
class SecurityServiceTest(
    @Autowired private val restTemplate: RestTemplate,
    @Autowired private val oAuth2Properties: OAuth2Properties,
) {

    @Test
    fun requestAccessToken() {

        val url = "https://oauth2.googleapis.com/token"
        val params = hashMapOf<String, String>()
        params["code"] = "4%2F0AWtgzh4kOjUf__0D1nMztYKCgsdTULCyU161wQRAzFY10Szkvpgqio7gL3r88nkx7C8mkw"
        params["client_id"] = oAuth2Properties.clientId
        params["client_secret"] = oAuth2Properties.clientSecret
        params["redirect_uri"] = oAuth2Properties.redirectUri
        params["grant_type"] = "authorization_code"

        println("결과" + restTemplate.postForEntity(url, params, String::class.java))
    }

}