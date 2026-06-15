package com.example.interactionservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CommentTreeResponse extends CommentResponse {
    @Default
    private List<CommentTreeResponse> children = new ArrayList<CommentTreeResponse>();
}
