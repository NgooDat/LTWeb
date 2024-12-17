package webshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

import javax.mail.internet.MimeMessage;

@Controller
@RequestMapping("/mailer/")
public class MailerController {

    
    @RequestMapping("form")
    public String index() {
        return "mailer/form";
    }
    
    public static String getCode() {
    	Random random = new Random();
        int randomCode = 10000000 + random.nextInt(90000000);
        return String.valueOf(randomCode);
    }

    
    public String sendCode(JavaMailSender mailSender, String to, String subject, String text) {
        try {
            // Tạo mail
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true để gửi email dưới dạng HTML

            // Gửi email
            mailSender.send(mail);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
