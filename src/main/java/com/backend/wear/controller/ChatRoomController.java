package com.backend.wear.controller;

import com.backend.wear.config.jwt.JwtUtil;
import com.backend.wear.dto.chat.ChatRoomResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.backend.wear.service.ChatRoomService;
import org.springframework.web.bind.annotation.*;
import java.security.SignatureException;


@Slf4j
@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService){
        this.chatRoomService = chatRoomService;
    }

    // 채팅방 생성
    // /api/chat/room/create?productId={productId}&userId={userId}
    @PostMapping("/room/create") //상품 아이디, 클릭한 유저 아이디
    public ResponseEntity<?> createRoom(@RequestHeader("Authorization") String authorizationHeader,
                                        @RequestParam(name="userId") Long userId,
                                        @RequestParam(name="productId") Long productId) throws Exception{
        try {
            ChatRoomResponseDto.CreatedDto createdDto =  chatRoomService.createdChatRoom(productId, userId);
            log.info("채팅방 생성 완료");

            return ResponseEntity.ok(createdDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 채팅방 입장
    // /api/chat/room/enter?chatRoomId={chatRoomId}&userId={userId}
    @GetMapping("/room/enter")
    public ResponseEntity<?> enterRoom( @RequestHeader("Authorization") String authorizationHeader,
                                        @RequestParam(name = "userId") Long userId,
                                       @RequestParam(name = "chatRoomId") Long chatRoomId) throws Exception
    {
        try{
            // 채팅방 정보
            ChatRoomResponseDto.DetailDto detailDto= chatRoomService.enterChatRoom(chatRoomId, userId);
            log.info("채팅방 입장 완료");

            return ResponseEntity.ok().body(detailDto);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //사용자 모든 채팅방 조회
    @GetMapping("/rooms")
    // /api/chat/rooms?userId={userId}&pageNumber={pageNumber}
    public ResponseEntity<?> getChatRoomList( @RequestHeader("Authorization") String authorizationHeader,
                                               @RequestParam(name = "userId") Long userId,
                                             @RequestParam(name = "pageNumber") Integer pageNumber) throws Exception {
        try{
            // 채팅방 정보
            Page<ChatRoomResponseDto.ScreenDto> chatRoomsPage= chatRoomService.findAllChatRooms(userId, pageNumber);
            log.info("모든 채팅방 조회 완료.");

            return ResponseEntity.ok().body(chatRoomsPage);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //채팅 상대 차단하기
    //api/chat/room/block?chatRoomId={chatRoomId}&userId={userId}
    @PostMapping("/room/block")
    public ResponseEntity<?> blockedChatUser(@RequestHeader("Authorization") String authorizationHeader,
                                             @RequestParam(name = "userId") Long userId,
                                             @RequestParam(name = "chatRoomId") Long chatRoomId) throws Exception {
        try{
            chatRoomService.blockedChatUser(chatRoomId,userId);
            return ResponseEntity.ok().body("채팅 상대방 차단 완료");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (SignatureException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    // 채팅 삭제하기
    // api/chat/room/delete?chatRoomId={chatRoomId}&userId={userId}
    @DeleteMapping("/room/delete")
    public ResponseEntity<?> deleteChatRoom( @RequestHeader("Authorization") String authorizationHeader,
                                             @RequestParam(name = "userId") Long userId,
                                             @RequestParam(name = "chatRoomId") Long chatRoomId) throws Exception{
        try{
            chatRoomService.deleteChatRoom(chatRoomId, userId);
            return ResponseEntity.ok().body("채팅방 삭제 완료");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}