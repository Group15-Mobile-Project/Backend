package airbnb.com.backend1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import airbnb.com.backend1.Entity.Request.MessageRequest;
import airbnb.com.backend1.Entity.Response.MessageResponse;
import airbnb.com.backend1.Service.MessageService;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    MessageService messageService;

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageResponse>> getAllByChat(@PathVariable Long chatId) {
        return new ResponseEntity<List<MessageResponse>>(messageService.getAllByChat(chatId), HttpStatus.OK);
    }
    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<MessageResponse>> getAllByAuthAndReceiver(@PathVariable Long receiverId) {
        return new ResponseEntity<List<MessageResponse>>(messageService.getAllByAuthserAndReceiver(receiverId), HttpStatus.OK);
    }
    @PostMapping("/message")
    public ResponseEntity<MessageResponse> addMessage(@RequestBody @Valid MessageRequest req) {
        return new ResponseEntity<MessageResponse>(messageService.add(req), HttpStatus.CREATED);
    }
    @DeleteMapping("/message/{id}")
    public ResponseEntity<HttpStatus> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return new ResponseEntity<HttpStatus>( HttpStatus.NO_CONTENT);
    }

}
