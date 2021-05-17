package com.softclinic.cowin.dto

import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.Validated

import javax.validation.constraints.Email
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
class AlertCommand {
    @Size(min = 10, max = 10)
    @NotNull
    Long mobileNo
    @NotNull
    Integer districtId
    @Email
    @NotNull
    String emailId
    @NotNull
    String vaccineType
}
