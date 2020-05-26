package com.gcsale.dealerbackend.application.exceptions

import org.springframework.validation.ObjectError
import java.lang.RuntimeException

class ValidationException(val errors: MutableList<ObjectError>) :
        Throwable(errors.map { e -> e.toString() }.joinToString())