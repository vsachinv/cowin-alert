package com.softclinic.cowin

import com.softclinic.cowin.dto.DistrictApiDTO
import com.softclinic.cowin.dto.DistrictDTO
import com.softclinic.cowin.dto.StateApiDTO
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client
import io.reactivex.Flowable

import static io.micronaut.http.HttpHeaders.ACCEPT
import static io.micronaut.http.HttpHeaders.USER_AGENT

@Client(CowinApiConfiguration.COWIN_API_URL)
@Header(name = USER_AGENT, value = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
@Header(name = ACCEPT, value = "application/json")
interface CowinApiClient {
    @Get('/admin/location/states')
    StateApiDTO fetchStates()

    @Get('/admin/location/districts/{id}')
    DistrictApiDTO fetchDistricts(Integer id)

    @Get('/appointment/sessions/public/calendarByDistrict?district_id={id}&date={date}')
    Map fetchCenters(Integer id,String date)
}
