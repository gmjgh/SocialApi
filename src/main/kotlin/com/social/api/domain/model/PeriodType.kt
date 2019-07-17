package com.social.api.domain.model

import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class PeriodType {
    MILLISECOND, SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, YEAR, DECADE, CENTURY;
}

@Converter
class PeriodTypeConverter : AttributeConverter<PeriodType, String> {

    override fun convertToDatabaseColumn(type: PeriodType): String = type.toString()

    override fun convertToEntityAttribute(type: String): PeriodType = PeriodType.valueOf(type)

}