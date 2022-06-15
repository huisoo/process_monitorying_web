package ssh_call.call.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class SSHUtils {
    private final String username = "dc1adm";
    private final String host = "192.168.0.101";
    private final int port = 22;
    private final String password = "Dhi@1004";

    private Session session;
    private ChannelExec channelExec;

    //Ssh 연결
    private void connectSSH() throws JSchException {
        log.info("try connect ssh");
        session = new JSch().getSession(username, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");       // 호스트 정보를 검사하지 않도록 설정
        session.connect();
    }

    //command 실행
    public void command(String command) {
        try {
            connectSSH();
            channelExec = (ChannelExec) session.openChannel("exec");	// 실행할 channel 생성

            channelExec.setCommand(command);	// 실행할 command 설정
            channelExec.connect();		// command 실행

        } catch (JSchException e) {
            log.error("JSchException");
        } finally {
            this.disConnectSSH();
        }
    }

    //command 결과 받기
    public String getSSHResponse(String command) {
        StringBuilder response = null;
        try {
            connectSSH();
            channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);

            InputStream inputStream = channelExec.getInputStream();
            channelExec.connect();

            byte[] buffer = new byte[8192];
            int decodedLength;
            response = new StringBuilder();
            while ((decodedLength = inputStream.read(buffer, 0, buffer.length)) > 0)
                response.append(new String(buffer, 0, decodedLength));

        } catch (Exception e) {
            log.error("JSchException");
            e.printStackTrace();
        } finally {
            this.disConnectSSH();
        }
        return response.toString();
    }

    //ssh 연결해제
    private void disConnectSSH() {
        if (session != null) session.disconnect();
        if (channelExec != null) channelExec.disconnect();
    }


}
