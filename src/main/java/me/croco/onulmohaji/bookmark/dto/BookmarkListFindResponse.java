package me.croco.onulmohaji.bookmark.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.bookmark.domain.Bookmark;

@Setter
@Getter
public class BookmarkListFindResponse {

    private String placeType;
    private String placeId;

    public BookmarkListFindResponse(Bookmark bookmark) {
        this.placeType = bookmark.getPlaceType();
        this.placeId = bookmark.getPlaceId();
    }

}
