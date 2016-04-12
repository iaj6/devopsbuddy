package com.devopsbuddy.web.controllers;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.service.EmailService;
import com.devopsbuddy.backend.service.I18NService;
import com.devopsbuddy.backend.service.PasswordResetTokenService;
import com.devopsbuddy.backend.service.UserService;
import com.devopsbuddy.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Created by tedonema on 12/04/2016.
 */
@Controller
public class ForgotMyPasswordController {

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(ForgotMyPasswordController.class);

    /** The name of the view that displays users the form where they can enter their email address. */
    public static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";

    /** The name of the view that displays users the form where they can reset their password. */
    public static final String CHANGE_PASSWORD_VIEW_NAME = "forgotmypassword/changePassword";

    /** The Path that will handle the user's request to change their password. */
    public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";

    private static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME = "forgotmypassword.email.text";

    private static final String PASSWORD_RESET_ATTRIBUTE_NAME = "passwordReset";

    private static final String MESSAGE_ATTRIBUTE_NAME = "message";

    @Autowired
    private I18NService i18NService;

    @Autowired
    private EmailService emailService;

    @Value("${webmaster.email")
    private String webMasterEmail;

    @Autowired
    private UserService userService;

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

    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.GET)
    public String changeUserPasswordGet(@RequestParam("id") long id,
                                        @RequestParam("token") String token,
                                        Locale locale,
                                        ModelMap model) {

        if (StringUtils.isEmpty(token) || id == 0) {
            LOG.error("Invalid user id {}  or token value {}", id, token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Invalid user id or token value");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);

        if (null == passwordResetToken) {
            LOG.warn("A token couldn't be found with value {}", token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Token not found");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = passwordResetToken.getUser();
        if (user.getId() != id) {
            LOG.error("The user id {} passed as parameter does not match the user id {} associated with the token {}",
                    id, user.getId(), token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.invalid", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        if (LocalDateTime.now(Clock.systemUTC()).isAfter(passwordResetToken.getExpiryDate())) {
            LOG.error("The token {} has expired", token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.expired", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        model.addAttribute("principalId", user.getId());

        // OK to proceed. We auto-authenticate the user so that in the POST request we can check if the user
        // is authenticated
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);


        return CHANGE_PASSWORD_VIEW_NAME;

    }

    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.POST)
    public String changeUserPasswordPost(@RequestParam("principal_id") long userId,
                                         @RequestParam("password") String password,
                                         ModelMap model,
                                         Principal requestor) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() != userId) {
            LOG.warn("Security breach! User {} is trying to make a password reset request on behalf of {}",
                    user.getId(), userId);
        }

        userService.updateUserPassword(userId, password);
        LOG.info("Password successfully updated for user {}", user.getUsername());

        model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "true");

        return CHANGE_PASSWORD_VIEW_NAME;

    }
}
