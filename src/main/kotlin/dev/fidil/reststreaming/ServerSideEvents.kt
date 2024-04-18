package dev.fidil.reststreaming

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.util.*

@RestController
class SSERestEndpoint(
    private val messageRepository: MessageRepository,
    private val sink: Sinks.Many<Message>,
) {

    @PostMapping("/messages", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun postMessage(@RequestBody createMessage: CreateMessage): Mono<Message> {
        return messageRepository.save(Message(UUID.randomUUID(), createMessage.value))
            .doOnNext(sink::tryEmitNext)
    }

    @GetMapping("/messages", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun messageSubscription(): Flux<ServerSentEvent<MessageView>> {
        return sink.asFlux().map {
            ServerSentEvent.builder(MessageView(it.id, it.message))
                .id(it.id.toString())
                .event("MessageCreated")
                .build()
        }
    }
}

data class CreateMessage(val value: String)

data class MessageView(val id: UUID, val message: String)

@Document
data class Message(
    @Id
    val id: UUID,
    val message: String,
)

interface MessageRepository : ReactiveMongoRepository<Message, UUID>

@Configuration
class UserStreamConfiguration {

    @Bean
    fun publisher(): Sinks.Many<Message> {
        return Sinks.many().replay().all()
    }
}