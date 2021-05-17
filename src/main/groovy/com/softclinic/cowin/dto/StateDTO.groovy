package com.softclinic.cowin.dto

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

@Introspected
@CompileStatic
class StateDTO {
    Integer state_id
    String state_name
}
