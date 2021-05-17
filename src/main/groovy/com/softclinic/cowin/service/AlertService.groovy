package com.softclinic.cowin.service

import com.softclinic.cowin.domain.Alert
import com.softclinic.cowin.domain.AlertRepository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertService {

    @Inject AlertRepository alertRepository


    void saveAlert(Alert alert) {
        alertRepository.save(alert)
    }

    List<Alert> getAllAlerts() {
        return alertRepository.findAll().toList()
    }

    Alert findAlert(Long mobileNo){
        return alertRepository.find(mobileNo)
    }

    void removeAlert(Alert alert){
        alertRepository.delete(alert)
    }
}
