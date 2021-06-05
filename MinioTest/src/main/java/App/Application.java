package App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        SpringApplication api = new SpringApplication(Application.class);

        api.setDefaultProperties(Collections
                .singletonMap("server.port", "8083"));

        api.run(args);

    }
}

@Controller
class SimpleApi {


    @RequestMapping(value = "/upload")
    @ResponseBody
    public String upload(@RequestParam String username) {
        try {
            System.out.println(username);
            return MinioTest.getPresignedPostUrl(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/download")
    @ResponseBody
    public String download(@RequestParam String username) {
        try {
            System.out.println(username);
            return MinioTest.getPresignedGetUrl(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
