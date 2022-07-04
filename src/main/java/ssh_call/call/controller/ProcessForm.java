package ssh_call.call.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class ProcessForm {
    @NotEmpty(message="process명은 필수입니다.")
    private String name;
    private String serverName;
    private String location;
}
