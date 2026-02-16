package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.model.Trip;
import com.tripvault.TripVault.model.TripMember;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.service.TripMemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/trips")
public class TripMemberController {

    private final TripMemberService tripMemberService;
    public TripMemberController(TripMemberService tripMemberService){
        this.tripMemberService=tripMemberService;
    }


}
