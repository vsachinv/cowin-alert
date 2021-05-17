package com.softclinic.cowin.domain

import com.softclinic.cowin.dto.AlertCommand

import javax.persistence.Entity
import javax.persistence.Id


@Entity(name = 'ALERT_SUBSCRIPTION')
class Alert {

    String emailId
    @Id
    Long mobileNo
    String vaccineType
    Integer districtId

    Alert(){
    }

    Alert(AlertCommand command){
        this.emailId = command.emailId
        this.mobileNo = command.mobileNo
        this.vaccineType = command.vaccineType
        this.districtId = command.districtId
    }
}
