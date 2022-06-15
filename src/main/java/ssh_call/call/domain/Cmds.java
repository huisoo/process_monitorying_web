package ssh_call.call.domain;


import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Cmds {

    @Id @GeneratedValue
    @Column(name="cmds_id")
    private Long id;
    private String name;
    private String source;


}
