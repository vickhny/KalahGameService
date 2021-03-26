package io.assignment.kalah.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KalahGameDTO {

    private String id;
    private String uri;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<Integer, String> status;

    public KalahGameDTO(final String id, final String uri) {
        this(id, uri, null);
    }

    public KalahGameDTO(final String id, final String uri, final Map<Integer, String> status) {
        this.id = id;
        this.uri = uri;
        this.status = status;
    }

}
