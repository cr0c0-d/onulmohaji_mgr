package me.croco.onulmohaji.bookmark.service;

import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.bookmark.domain.Bookmark;
import me.croco.onulmohaji.bookmark.dto.BookmarkAddRequest;
import me.croco.onulmohaji.bookmark.repository.BookmarkRepository;
import me.croco.onulmohaji.member.domain.Member;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    // 처리결과가 즐겨찾기 상태(true)인지 해제상태(false)인지 반환
    public Boolean toggleBookmark(BookmarkAddRequest request, Member loginMember) {
        Optional<Bookmark> bookmark = bookmarkRepository.findByUserIdAndPlaceTypeAndPlaceId(loginMember.getId(), request.getPlaceType(), request.getPlaceId());

        if(bookmark.isEmpty()) {    // 신규 추가
            Bookmark newBookmark = bookmarkRepository.save(
                                        Bookmark.builder()
                                            .userId(loginMember.getId())
                                            .placeType(request.getPlaceType())
                                            .placeId(request.getPlaceId())
                                            .build()
                                    );
            return true;

        } else {    // 삭제
            bookmarkRepository.deleteById(bookmark.get().getId());
            return false;
        }
    }

    public List<Bookmark> getBookmarkList(Member loginMember) {
        return bookmarkRepository.findByUserId(loginMember.getId());
    }


}
