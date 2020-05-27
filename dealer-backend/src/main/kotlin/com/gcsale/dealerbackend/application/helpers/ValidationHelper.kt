package com.gcsale.dealerbackend.application.helpers

import com.gcsale.dealerbackend.application.exceptions.ValidationException
import org.springframework.validation.BindingResult
import org.springframework.validation.DataBinder
import org.springframework.validation.Validator

object ValidationHelper {

    fun validate(obj: Any, validator: Validator): BindingResult {
        val binder = DataBinder(obj)
        binder.addValidators(validator)
        binder.validate()
        return binder.bindingResult
    }

    fun checkValidation(results: BindingResult) {
        if (results.hasErrors()) throw ValidationException(results.allErrors)
    }

    fun performValidation(obj: Any, validator: Validator) {
        val results = validate(obj, validator)
        checkValidation(results)
    }
}