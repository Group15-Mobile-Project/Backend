package airbnb.com.backend1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import airbnb.com.backend1.Entity.Request.DiscountRequest;
import airbnb.com.backend1.Entity.Response.DiscountResponse;
import airbnb.com.backend1.Service.DiscountService;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/discount")
public class DiscountController {
    @Autowired
    DiscountService discountService;

    @PostMapping("/")
    public ResponseEntity<DiscountResponse> createDiscount(@RequestBody @Valid DiscountRequest request) {
        return new ResponseEntity<DiscountResponse>(discountService.save(request), HttpStatus.CREATED);
    }
    @PutMapping("/id/{id}")
    public ResponseEntity<DiscountResponse> updateDiscount(@RequestBody @Valid DiscountRequest request, @PathVariable Long id) {
        return new ResponseEntity<DiscountResponse>(discountService.update(id, request), HttpStatus.CREATED);
    }
}
