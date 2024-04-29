package com.backend.wear.controller;

import com.backend.wear.dto.chat.ChatRoomResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.backend.wear.service.ChatService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatService chatService;
    private static Logger log = LoggerFactory.getLogger(ChatRoomController.class);

    @Autowired
    public ChatRoomController(ChatService chatService){
        this.chatService=chatService;
    }

    // 채팅방 생성
    // /api/chat/room/create?productId={productId}&userId={userId}
    @PostMapping("/room/create") //상품 아이디, 클릭한 유저 아이디
    public ResponseEntity<?> createRoom(@RequestParam(name="productId") Long productId, @RequestParam(name="userId") Long userId) throws Exception{

        try {
            ChatRoomResponseDto.CreatedDto createdDto =  chatService.createdChatRoom(productId, userId);
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
    public ResponseEntity<?> enterRoom(@RequestParam(name = "chatRoomId") Long chatRoomId, @RequestParam(name = "userId") Long userId) throws Exception
    {
        try{
            // 채팅방 정보
            ChatRoomResponseDto.DetailDto detailDto=chatService.enterChatRoom(chatRoomId, userId);

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
    public ResponseEntity<?> roomList(@RequestParam(name = "userId") Long userId,
                                      @RequestParam(name = "pageNumber") Integer pageNumber ) throws Exception {

        try{
            // 채팅방 정보
            Page<ChatRoomResponseDto.ScreenDto> chatRoomsPage=chatService.findAllChatRooms(userId, pageNumber);
            log.info("모든 채팅방 조회 완료.");

            return ResponseEntity.ok().body(chatRoomsPage);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}