package ssh_call.call.dto;


import lombok.Data;
import lombok.Getter;

@Data
public class ProcessDto {

    private Long seq;
    private String name;
    private String serverName;
    private String location;
    private String startShell;
    private String killShell;
    private String logShell;
    private String status;
}