package com.devopsbuddy.web.controllers;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.service.EmailService;
import com.devopsbuddy.backend.service.I18NService;
import com.devopsbuddy.backend.service.PasswordResetTokenService;
import com.devopsbuddy.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by tedonema on 12/04/2016.
 */
@Controller
public class ForgotMyPasswordController {

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(ForgotMyPasswordController.class);

    /** The name of the view that displays users the form where they can enter their email address. */
    public static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";

    /** The Path that will handle the user's request to change their password. */
    public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";

    private static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME = "forgotmypassword.email.text";

    @Autowired
    private I18NService i18NService;

    @Autowired
    private EmailService emailService;

    @Value("${webmaster.email")
    private String webMasterEmail;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @RequestMapping(value = "/forgotmypassword", method = RequestMethod.GET)
    public String forgotPasswordGet() {
        return EMAIL_ADDRESS_VIEW_NAME;
    }

    @RequestMapping(value = "/forgotmypassword", method = RequestMethod.POST)
    public String forgotPasswordPost(HttpServletRequest request, @RequestParam("email") String email, ModelMap model) {

        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(email);

        if (null == passwordResetToken) {
            LOG.warn("The application couldn't find a user for the given email {}. Nothing will be done", email);
        } else {

            User user = passwordResetToken.getUser();

            String resetPasswordUrl = UserUtils.createPasswordResetUrl(request, user.getId(), passwordResetToken.getToken());
            LOG.debug("Reset Password URL: {}", resetPasswordUrl);

            String emailText = i18NService.getMessage(EMAIL_MESSAGE_TEXT_PROPERTY_NAME, request.getLocale());
            SimpleMailMessage emailMessage = new SimpleMailMessage();
            emailMessage.setTo(user.getEmail());
            emailMessage.setSubject("[Devopsbuddy]: How to Reset Your Password");
            emailMessage.setText(emailText + "\r\n" + resetPasswordUrl);
            emailMessage.setFrom(webMasterEmail);

            emailService.sendGenericEmailMessage(emailMessage);
        }

        model.addAttribute("mailSent", true);

        return EMAIL_ADDRESS_VIEW_NAME;
    }
}
