package com.nandini.guestbook.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nandini.guestbook.security.AuthRequest;
import com.nandini.guestbook.utils.GuestBookConstants;

@Controller
public class GuestBookWebController {
	
    @GetMapping("/")
    public String displayGuestBook(Model model) {
    	try {
        model.addAttribute("login", new AuthRequest());
    	}catch(Exception e) {
		}

        return GuestBookConstants.GUESTBOOK_TEMPLATE;
    }

}
