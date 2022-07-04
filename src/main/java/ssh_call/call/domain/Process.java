package ssh_call.call.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Process {

    @Id @GeneratedValue
    @Column(name="process_seq")
    private Long seq;
    @Column(name="process_name")
    private String name;
    private String aliasProcessName;
    private String serverName;
    private String location;
    private String startShell;
    private String killShell;
    private String logShell;

}
