package ssh_call.call.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ssh_call.call.domain.Process;
import ssh_call.call.dto.ProcessDto;
import ssh_call.call.service.ProcessService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProcessController {

    private final ProcessService processService;

    @GetMapping("/process/new")
    public String createForm(Model model){
        model.addAttribute("processForm", new ProcessForm());
        return "process/createProcessForm";
    }

    @PostMapping("/process/new")
    public String create(@Valid ProcessForm ProcessForm, BindingResult result){

        if(result.hasErrors()){
            return "process/createProcessForm";
        }

        Process ps = new Process();
        ps.setName(ProcessForm.getName());
        ps.setServerName(ProcessForm.getServerName());
        ps.setLocation(ProcessForm.getLocation());
        processService.join(ps);
        //model.addAttribute("memberForm", new MemberForm());
        return "redirect:/";
    }

    @GetMapping("/process")
    public String list(Model model){
        List<ProcessDto> psList = processService.findProcessList();
        List<ProcessDto> psAliveList = processService.findAliveProcess(psList);
        model.addAttribute("process", psAliveList);
        return "process/processList";
    }

    @PostMapping("/process/task-start")
    @ResponseBody
    public Map taskStart(@RequestBody Map<String, Object> body){
        System.out.println("processSeq : " + body.get("processSeq"));
        Long processSeq = Long.parseLong(body.get("processSeq").toString());
        Process ps = processService.findProcessByProcessName(processSeq);
        Map map = new HashMap();
        if(ps.getServerName().equals("linux")){
            log.error("ps : " + ps.getServerName());
            map = processService.executeCmdByStringStartCommand(ps);
            log.error("map : " + map);
        }
        return map;
    }

    @PostMapping("/process/task-kill")
    @ResponseBody
    public Map taskKill(@RequestBody Map<String, Object> body){
        System.out.println("processSeq : " + body.get("processSeq"));
        Long processSeq = Long.parseLong(body.get("processSeq").toString());
        Process ps = processService.findProcessByProcessName(processSeq);
        Map map = new HashMap();
        if(ps.getServerName().equals("linux")){
            log.error("ps : " + ps.getServerName());
            map = processService.executeCmdByStringKillCommand(ps);
            log.error("map : " + map);
        }
        return map;
    }


    @PostMapping("/process/task-log")
    @ResponseBody
    public Map taskLog(@RequestBody Map<String, Object> body){
        System.out.println("processSeq : " + body.get("processSeq"));
        Long processSeq = Long.parseLong(body.get("processSeq").toString());
        Process ps = processService.findProcessByProcessName(processSeq);
        Map map = new HashMap();
        if(ps.getServerName().equals("linux")){
            log.error("ps : " + ps.getServerName());
            map = processService.executeCmdByStringLogCommand(ps);
            log.error("map : " + map);
        }
        return map;
    }
}
