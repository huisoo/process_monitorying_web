package ssh_call.call.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScStop {
    stop_doocare_common_module_multiDB("source sc_stop_doocare_common_module_multiDB.sh", "COMMON_MODULE_실행");

    private final String cmd;
    private final String description;
}
