package com.softclinic.cowin.dto

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

@Introspected
@CompileStatic
class DistrictDTO {
    Integer district_id
    String district_name
}
