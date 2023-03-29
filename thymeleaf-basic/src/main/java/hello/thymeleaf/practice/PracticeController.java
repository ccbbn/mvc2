package hello.thymeleaf.practice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/practice")
public class PracticeController {
    @GetMapping("/field")
    public String field(Model model) {
      model.addAttribute("item", new Item("삼다수",100000));
      return "/practice/field";
    }
}
