package com.practice.oauth.auth.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.practice.oauth.web.dto.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TokenAccessDeniedHandler(
    private val objectMapper: ObjectMapper,
) : AccessDeniedHandler {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        response.sendError(request, accessDeniedException)
    }

    private fun HttpServletResponse.sendError(
        request: HttpServletRequest,
        exception: AccessDeniedException,
    ) {
        contentType = "application/json"
        status = HttpServletResponse.SC_FORBIDDEN
        writer.println(
            ErrorResponse(
                LocalDateTime.now(),
                status,
                exception.javaClass.simpleName,
                exception.localizedMessage,
                request.requestURI
            ).also { logger.info(it.toString()) }
                .let { objectMapper.writeValueAsString(it) }

        )
    }
}