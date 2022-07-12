package ssh_call.call.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssh_call.call.domain.Process;
import ssh_call.call.dto.ProcessDto;
import ssh_call.call.repository.ProcessRepository;
import ssh_call.call.utils.ShRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ProcessService {

    private final ProcessRepository processRepository;

    @Autowired
    public ProcessService(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    //command 등록
    @Transactional(readOnly = false)
    public Long join(Process ps){
        validateDuplicateProcess(ps);
        processRepository.save(ps);
        return ps.getSeq();
    }

    private void validateDuplicateProcess(Process ps) {
        //Exception
        List<Process> processList = processRepository.findByName(ps.getName());
        if(!processList.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 process 입니다.");
        }
    }

    public List<ProcessDto> findProcessList(){
        ModelMapper modelMapper = new ModelMapper();
        List<Process> psList = processRepository.findAll();
        List<ProcessDto> collect =
                psList.stream().map(p -> modelMapper.map(p, ProcessDto.class)).collect(Collectors.toList());

        return collect;
    }


    public List<ProcessDto> findAliveProcess(List<ProcessDto> processList){
        for (ProcessDto ps : processList){
            executeCmdByStringAliveCommand(ps);
        }

        return processList;
    }

    public Process findProcessByProcessName(Long processSeq){
        return processRepository.findOne(processSeq);
    }

    public Process findOne(Long psId){
        return processRepository.findOne(psId);
    }

    public Map executeCmdByStringStartCommand(Process ps){
        String cmd =  ps.getLocation()  + ps.getStartShell();
        log.error("cmd : "  + cmd);
        ShRunner shRunner = new ShRunner();
        String[] callCmd = {"/bin/sh", "-c", cmd};
        Map map = shRunner.execCommand(callCmd);
        return map;
    }

    public Map executeCmdByStringKillCommand(Process ps){
        String cmd =  ps.getLocation()  + ps.getKillShell();
        log.error("cmd : "  + cmd);

        ShRunner shRunner = new ShRunner();
        String[] callCmd = {"/bin/sh", "-c", cmd};
        Map map = shRunner.execCommand(callCmd);
        return map;
    }


    public Map executeCmdByStringLogCommand(Process ps){
        String cmd =   ps.getLocation()  + ps.getLogShell();
        log.error("cmd : "  + cmd);

        ShRunner shRunner = new ShRunner();
        String[] callCmd = {"tail","-n","50",cmd};
        Map map = shRunner.execCommand(callCmd);
        log.error("map : " + map);
        return map;
    }

    public void executeCmdByStringAliveCommand(ProcessDto ps){
        String cmd =   "ps -ef | grep " +ps.getName();

        ShRunner shRunner = new ShRunner();
        String[] callCmd = {"/bin/sh", "-c", cmd};
        Map map = shRunner.execCommand(callCmd);

        if(map.get("log").toString().contains("/app/build/")){
            ps.setStatus("OK");
        }else{
            ps.setStatus("NO");
            return;
        }

        String[] tailCallCmd = {"tail","-n","100",cmd};
        Map tailMap = shRunner.execCommand(tailCallCmd);
        if(tailMap.get("log").toString().contains("Exception")){
            ps.setStatus("NO");
        }

    }

}
