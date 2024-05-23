package com.howard.rentalmytrack.controller;

import com.howard.rentalmytrack.dto.AddTrack;
import com.howard.rentalmytrack.entity.RentalMyTrack;
import com.howard.rentalmytrack.service.impl.RentalMyTrackServiceImpl;
import com.roger.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/frontend/rentalmytrack")
public class FrontendRentalMyTrackController {

    @Autowired
    private RentalMyTrackServiceImpl rentalMyTrackService;

    @ModelAttribute("frontendTrackList")
    protected List<RentalMyTrack> getAllData() {
        return rentalMyTrackService.getAll();
    }

    String toLogin = "/frontend/member/loginMember";

    @GetMapping("/toRentalMyTrack")
    public String toRentalMyTrack(HttpSession session, ModelMap model) {
        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return "redirect:" + toLogin;
        }
        model.addAttribute("member", member);
        return "frontend/rental/rentalMyTrack";
    }

    @PostMapping("/addTrack")
    public ResponseEntity<?> addTrack(@RequestBody AddTrack addTrack, HttpSession session) {
        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.OK).body(toLogin);
        }
        // 若已經存在，一樣返回加入成功
        RentalMyTrack testTrack = rentalMyTrackService.findById(addTrack.getRentalNo(), member.getMemNo());
        if (testTrack != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(testTrack.getRental().getRentalName());
        }
        Timestamp expRentalDate = new Timestamp(System.currentTimeMillis());
        RentalMyTrack.CompositeTrack compositeTrack = new RentalMyTrack.CompositeTrack(addTrack.getRentalNo(), member.getMemNo());
        RentalMyTrack rentalMyTrack = new RentalMyTrack(compositeTrack, expRentalDate);
        rentalMyTrackService.insert(rentalMyTrack);
        //
        RentalMyTrack trackForReturn = null;
        String rentalName = null;
        int tries = 10;
        while (tries-- > 0) {
            trackForReturn = rentalMyTrackService.findById(addTrack.getRentalNo(), member.getMemNo());
            if (trackForReturn.getRental() != null && trackForReturn.getRental() != null) {
                rentalName = trackForReturn.getRental().getRentalName();
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(rentalName);

    }

    // 刪除
    @DeleteMapping("/deleteTrack")
    public ResponseEntity<?> deleteTrack(@RequestParam Integer rentalNo, HttpSession session) {

        Member member = (Member) session.getAttribute("loginsuccess");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.OK).body(toLogin);
        }
        RentalMyTrack rentalMyTrack = rentalMyTrackService.findById(rentalNo, member.getMemNo());
        if (rentalMyTrack != null) {
            rentalMyTrackService.delete(rentalMyTrack);
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }

    }



}
