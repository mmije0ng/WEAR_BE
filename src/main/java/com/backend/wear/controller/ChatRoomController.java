package com.backend.wear.controller;

import com.backend.wear.dto.ChatRoomDto;
import com.backend.wear.dto.ChatRoomIdDto;
import com.backend.wear.dto.ChatRoomProfileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.backend.wear.entity.ChatRoom;
import com.backend.wear.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
//@CrossOrigin(origins={"http://43.201.189.171:8080", "http://localhost:5173", "http://wear-frontend.s3-website.ap-northeast-2.amazonaws.com"})
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatService chatService;
    private static Logger log = LoggerFactory.getLogger(ChatRoomController.class);

    @Autowired
    public ChatRoomController(ChatService chatService){
        this.chatService=chatService;
    }

    // 채팅방 생성
    // chat/room?productId={productId}&customerId={customerId}
    @PostMapping("/room/create") //상품 아이디, 클릭한 유저 아이디
    public ResponseEntity<?> createRoom(@RequestParam Long productId, @RequestParam Long customerId) {

        try {
            //채팅방 pk 반환
            ChatRoomIdDto chatRoomId =  chatService.createRoom(productId, customerId);

            if(chatRoomId.isCreated()){
                return ResponseEntity.ok().body(chatRoomId.getChatRoomId());
            }

            else{
                Long roomId=chatRoomId.getChatRoomId();
                // 생성된 채팅방의 ID를 사용하여 enterRoom 엔드포인트로 리다이렉션
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create("/api/chat/room/enter?roomId=" + roomId + "&productId=" + productId))
                        .build();
            }
        }

        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 채팅방 입장
    // chat/room/enter?roomId={roomId}&productId={productId}
    @GetMapping("/room/enter")
    public ResponseEntity<?> enterRoom(@RequestParam Long roomId,
                                       @RequestParam Long productId)
    {
        try{
            ChatRoomDto dto=chatService.chatRoom(roomId, productId);

            log.info("채팅방 입장 완료");

            return ResponseEntity.ok().body(dto);
        }

        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //사용자 모든 채팅방 조회
    @GetMapping("/rooms")
    // chat/room?userId={userId}
    public ResponseEntity<?> roomList(@RequestParam Long userId){

        List<ChatRoomProfileDto> chatRoomList=chatService.findAllRoom(userId);

        if(!chatRoomList.isEmpty()){
            log.info("모든 채팅방 조회 완료.");
            return ResponseEntity.ok().body(chatRoomList);
        }

        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("채팅 내역이 없습니다.");
    }
}