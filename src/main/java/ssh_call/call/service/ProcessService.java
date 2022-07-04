package ssh_call.call.service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssh_call.call.domain.Cmds;
import ssh_call.call.domain.Process;
import ssh_call.call.repository.CmdsRepository;
import ssh_call.call.repository.ProcessRepository;
import ssh_call.call.utils.SSHUtils;
import ssh_call.call.utils.ShRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

    //command 전체 조회
    public List<Process> findProcessList(){
        return processRepository.findAll();
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
        String[] callCmd = {"tail","-n","100",cmd};
        Map map = shRunner.execCommand(callCmd);
        log.error("map : " + map);
        return map;
    }

}
