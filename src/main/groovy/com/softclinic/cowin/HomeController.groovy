package com.softclinic.cowin


import com.softclinic.cowin.domain.Alert
import com.softclinic.cowin.dto.AlertCommand
import com.softclinic.cowin.dto.StateDTO
import com.softclinic.cowin.service.AlertService
import io.micronaut.core.util.CollectionUtils
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.views.View

import javax.validation.ConstraintViolationException
import javax.validation.Valid
import java.text.SimpleDateFormat

@Controller("/")
class HomeController {

    private final CowinApiClient cowinApiClient
    private final AlertService alertService

    HomeController(CowinApiClient cowinApiClient, AlertService alertService) {
        this.cowinApiClient = cowinApiClient
        this.alertService = alertService
    }

    @View("home")
    @Get("/")
    public HttpResponse index() {
        return HttpResponse.ok(CollectionUtils.mapOf("stateDTOS", states, "username", "sdelamo"))
    }

    @View("watcher")
    @Get("/watcher")
    public HttpResponse watcher() {
        return HttpResponse.ok(CollectionUtils.mapOf("stateDTOS", states, "username", "sdelamo"))
    }

    @Get(uri = "/state/{id}", produces = MediaType.APPLICATION_JSON)
    public List<Map> getDistricts(Integer id) {
        if (!id) {
            return []
        }
        cowinApiClient.fetchDistricts(id).districts.collect {
            [id: it.district_id, text: it.district_name]
        }
    }

    @View("thankyou")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/subscribe")
    public HttpResponse subscribe(@Body @Valid AlertCommand command) {
        alertService.saveAlert(new Alert(command))
        return HttpResponse.ok()
    }

    @Get(uri = "/subscribers", produces = MediaType.APPLICATION_JSON)
    public List<Alert> getSubscribers() {
        alertService.allAlerts
    }

    @Get(uri = "/unsubScribe/{mobileNo}", produces = MediaType.APPLICATION_JSON)
    public Map unsubScribe(Long mobileNo) {
        Alert alert = alertService.findAlert(mobileNo)
        if (alert) {
            alertService.removeAlert(alert)
            return [success: true]
        }
        return [failed: true]
    }

    @Get(uri = "/availableSlots/{id}/{vaccine}", produces = MediaType.APPLICATION_JSON)
    public List<Map> getCentersWithAvailableSlots(Integer id, String vaccine) {
        cowinApiClient.fetchCenters(id, new SimpleDateFormat("dd-MM-yyyy").format(new Date())).centers.findAll {
            it.sessions.any {
                it.vaccine == vaccine && it.available_capacity > 5 && it.min_age_limit == 18
            }
        }.collect {
            [center: it.name]
        }
    }


    @View("home")
    @Error(exception = ConstraintViolationException.class)
    public HttpResponse handleInvalidInput(HttpRequest<AlertCommand> request, ConstraintViolationException cv) {
        return index()
    }

    @View("home")
    @Error(exception = org.hibernate.exception.ConstraintViolationException.class)
    public HttpResponse handleAlreadySubscribed(HttpRequest<AlertCommand> request, org.hibernate.exception.ConstraintViolationException cv) {
        return index()
    }

    private List<StateDTO> getStates() {
        if (System.getProperty('local')) {
            return cowinApiClient.fetchStates().states
        } else {
            return Utils.cachedStates
        }

    }


}
