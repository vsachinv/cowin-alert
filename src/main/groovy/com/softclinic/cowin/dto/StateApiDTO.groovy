package com.softclinic.cowin.dto

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

@Introspected
@CompileStatic
class StateApiDTO {
    List<StateDTO> states
}
