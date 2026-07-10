package com.example.request;


import com.example.ConnectionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConnectRequest {
    @NotNull(message = "status không được null")
    private ConnectionStatus status;
}
