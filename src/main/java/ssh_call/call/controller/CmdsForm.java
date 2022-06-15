package ssh_call.call.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class CmdsForm {
    @NotEmpty(message="command명은 필수입니다.")
    private String name;
    private String source;
}
