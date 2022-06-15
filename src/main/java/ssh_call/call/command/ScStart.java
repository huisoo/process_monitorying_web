package ssh_call.call.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScStart {
    start_doocare_common_module_multiDB("source sc_start_doocare_common_module_multiDB.sh", "COMMON_MODULE_실행");

    private final String cmd;
    private final String description;
}
