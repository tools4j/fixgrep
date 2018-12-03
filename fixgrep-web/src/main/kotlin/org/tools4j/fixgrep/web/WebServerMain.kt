package org.tools4j.fixgrep.web

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


/**
 * User: benjw
 * Date: 03/12/2018
 * Time: 17:08
 */
fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {
        routing {
            get("/") {
                call.respondText("My Example Blog", ContentType.Text.Html)
            }
        }
    }.start(wait = true)
}