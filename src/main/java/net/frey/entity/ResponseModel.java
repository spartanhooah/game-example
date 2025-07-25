package net.frey.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Schema(description = "Response model")
public class ResponseModel {
    @Schema(description = "Message", examples = "OK")
    private String message;

    @Schema(description = "HTTP response code", examples = "200")
    private int code;

    @Schema(description = "Timestamp of the operation", examples = "2024-09-09 13:04:01")
    private String timestamp;

    public ResponseModel(String message, int code) {
        this.message = message;
        this.code = code;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
