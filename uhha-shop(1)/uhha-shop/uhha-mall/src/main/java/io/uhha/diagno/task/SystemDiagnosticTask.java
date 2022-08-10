package io.uhha.diagno.task;

import io.uhha.diagno.service.SysDiagnosticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemDiagnosticTask {
    @Autowired
    private SysDiagnosticService sysDiagnosticService;

    public void run(){
//        sysDiagnosticService.diagnosticAll();
    }

}
