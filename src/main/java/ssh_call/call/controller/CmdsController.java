package ssh_call.call.controller;

import org.springframework.web.bind.annotation.ResponseBody;
import ssh_call.call.domain.Cmds;
import ssh_call.call.service.CmdsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CmdsController {

    private final CmdsService cmdsService;

    @GetMapping("/cmds/new")
    public String createForm(Model model){
        model.addAttribute("cmdsForm", new CmdsForm());
        return "cmds/createCmdsForm";
    }

    @PostMapping("/cmds/new")
    public String create(@Valid CmdsForm cmdForm, BindingResult result){

        if(result.hasErrors()){
            return "cmds/createCmdsForm";
        }

        Cmds cmd = new Cmds();
        cmd.setName(cmdForm.getName());
        cmd.setSource(cmdForm.getSource());

        //cmd.setAddress(address);
        cmdsService.join(cmd);
        //model.addAttribute("memberForm", new MemberForm());
        return "redirect:/";
    }

    @GetMapping("/cmds")
    public String list(Model model){
        List<Cmds> cmds = cmdsService.findCmds();
        model.addAttribute("cmds", cmds);
        return "cmds/cmdList";
    }

    @GetMapping("/cmds/commandId")
    @ResponseBody
    public String executeScriptByCommandId(Long id){
        String response = cmdsService.executeCmdByCommandId(id);
        //model.addAttribute("cmds", cmds);
        return response;
    }

    @GetMapping("/cmds/commandString")
    @ResponseBody
    public String executeScriptByCommand(String command){
        return cmdsService.executeCmdByStringCommand(command);
    }
}
