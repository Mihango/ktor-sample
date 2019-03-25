package com.example

import com.example.models.Person
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*
import java.time.Duration

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CORS) {
        maxAge = Duration.ofDays(1)
    }
    install(ContentNegotiation) {
//        register(ContentType.Application.Json, GsonConverter())
        gson {
            setPrettyPrinting()
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }

        get("/person") {
            val person = Person("Rhyan", 2)
            call.respond(person)
        }

        post {
            val person = call.receive<Person>()
            person.apply {
                age += 20
            }
            call.respond(person)
        }

        route("/hello", HttpMethod.Get) {
            get {
                call.respond(mapOf("hello" to "Hello too"))
            }
        }

        method(HttpMethod.Get) {
            route("you") {
                route("guy") {
                    handle {
                        call.respond(mapOf("Getter" to "testing Handle"))
                    }
                }
            }
        }
    }
}

