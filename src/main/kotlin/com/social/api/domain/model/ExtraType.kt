package com.social.api.domain.model

import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class ExtraType {
    STREET,
    BUILDING,
    OFFICE,
    ADDRESS,
    CITY,
    COUNTRY,
    ZIP_CODE,
    REGION,
    GENERAL_INFO,
    CONDITION,
    ATTACHMENT,
    COORDINATES,
    EMAIL,
    PHONE,
    DESCRIPTION,
    NAME;
}

@Converter
class ExtraTypeConverter : AttributeConverter<PeriodType, String> {

    override fun convertToDatabaseColumn(type: PeriodType): String = type.toString()

    override fun convertToEntityAttribute(type: String): PeriodType = PeriodType.valueOf(type)

}