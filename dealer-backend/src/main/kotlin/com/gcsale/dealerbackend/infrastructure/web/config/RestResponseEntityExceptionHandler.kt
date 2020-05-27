package com.gcsale.dealerbackend.infrastructure.web.config

import com.gcsale.dealerbackend.application.exceptions.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

data class ValidationErrorDto(val field: String, val errorCode: String)

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: ValidationException, request: WebRequest):
            ResponseEntity<List<ValidationErrorDto>> {
        val dto = ex.errors.map {
            when (it) {
                is FieldError -> ValidationErrorDto(it.field, it.code.orEmpty())
                else -> ValidationErrorDto(it.toString(), it.code.orEmpty())
            }
        }.toList()
        return ResponseEntity(dto, HttpStatus.BAD_REQUEST)
    }
}