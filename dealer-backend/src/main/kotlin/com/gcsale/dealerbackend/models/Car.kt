package com.gcsale.dealerbackend.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "cars")
class Car(
        var tempCarName: String,
        @Column(name = "external_uuid") var externalUUID: UUID,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)
