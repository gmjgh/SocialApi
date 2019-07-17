package com.social.api.domain.model

import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Digits


@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "ACTIVITIES")
data class Activity(
        @Convert(converter = PeriodTypeConverter::class)
        @Column(name = "period_type")
        var periodType: PeriodType?,
        @Column(name = "activity_start")
        val activityStart: Long?,
        @Column(name = "activity_end")
        val activityEnd: Long?,
        val intersectable: Boolean? = false,
        val occupied: Boolean? = false,
        val local: Boolean? = true,
        val chargable: Boolean? = false,
        @Digits(integer=5, fraction=2)
        val value: BigDecimal? = null,
        @Convert(converter = CurrencyConverter::class)
        val currency: Currency? = null,
        val barter: String? = null,
        @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
        var extras: List<ActivityExtra>? = null,
        @ManyToOne
        @JoinColumn(name = "schedule")
        var organization: Organization? = null,
        @Column(name = "user_id")
        var userId: Long? = organization?.user?.id,
        @Id
        @GeneratedValue
        val id: Long? = null)

@Converter
class CurrencyConverter : AttributeConverter<Currency, String> {

        override fun convertToDatabaseColumn(currency: Currency): String = currency.currencyCode

        override fun convertToEntityAttribute(currency: String): Currency = Currency.getInstance(currency)

}
