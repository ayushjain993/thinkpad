package io.uhha.diagno.service;

import io.uhha.common.exception.BCException;
import io.uhha.diagno.vo.DiagnoResponse;

import java.util.List;

public interface SysDiagnosticService {

    public List<DiagnoResponse> diagnosticAll(Long uid, String email, String phone) throws BCException;
    public String diagnosticProgress();
    public String diagnosticCrypto();
    public DiagnoResponse diagnosticETHNode();
    public Boolean diagnosticSms(Long uid, String phone) throws BCException;
    public Boolean diagnosticEmail(String email);
    public Boolean diagnosticPayment();
}
