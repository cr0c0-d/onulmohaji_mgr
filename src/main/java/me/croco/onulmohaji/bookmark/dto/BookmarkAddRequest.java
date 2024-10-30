package me.croco.onulmohaji.bookmark.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookmarkAddRequest {
    private String placeType;
    private String placeId;
}
