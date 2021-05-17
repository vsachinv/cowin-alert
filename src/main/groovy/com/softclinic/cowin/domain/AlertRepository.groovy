package com.softclinic.cowin.domain

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface AlertRepository extends CrudRepository<Alert, Long> {
    Alert find(Long mobileNo);
}