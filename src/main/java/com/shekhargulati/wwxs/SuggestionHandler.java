package com.shekhargulati.wwxs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
public class SuggestionHandler {

    private AutocompletionService autocompletionService;

    @Autowired
    public SuggestionHandler(AutocompletionService autocompletionService) {
        this.autocompletionService = autocompletionService;
    }

    public Mono<ServerResponse> suggest(ServerRequest request) {
        Optional<String> optionalInput = request.queryParam("input");
        if (!optionalInput.isPresent()) {
            return ServerResponse.ok().build();
        }
        String input = optionalInput.get();
        List<Pair> result = autocompletionService.autocomplete(input);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(result));

    }
}
