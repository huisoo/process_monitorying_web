package ssh_call.call.service;

import ssh_call.call.domain.Cmds;
import ssh_call.call.repository.CmdsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssh_call.call.utils.SSHUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CmdsService {

    private final CmdsRepository cmdsRepository;

    @Autowired
    public CmdsService(CmdsRepository cmdsRepository) {
        this.cmdsRepository = cmdsRepository;
    }

    //command 등록
    @Transactional(readOnly = false)
    public Long join(Cmds cmd){
        validateDuplicateCmd(cmd);
        cmdsRepository.save(cmd);
        return cmd.getId();
    }

    private void validateDuplicateCmd(Cmds cmd) {
        //Exception
        List<Cmds> cmdList = cmdsRepository.findByName(cmd.getName());
        if(!cmdList.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 command 입니다.");
        }
    }

    //command 전체 조회
    public List<Cmds> findCmds(){
        return cmdsRepository.findAll();
    }

    public Cmds findOne(Long cmdId){
        return cmdsRepository.findOne(cmdId);
    }

    public String executeCmdByCommandId(Long cmdId){
        SSHUtils sshUtils = new SSHUtils();
        sshUtils.command(this.findOne(cmdId).getSource());

        return "";
    }

    public String executeCmdByStringCommand(String cmd){
        SSHUtils sshUtils = new SSHUtils();
        return sshUtils.getSSHResponse(cmd);
    }

}
