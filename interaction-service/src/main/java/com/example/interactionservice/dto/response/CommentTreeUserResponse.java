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
public class CommentTreeUserResponse extends CommentUserResponse {
    @Default
    private List<CommentTreeUserResponse> children = new ArrayList<CommentTreeUserResponse>();
}
