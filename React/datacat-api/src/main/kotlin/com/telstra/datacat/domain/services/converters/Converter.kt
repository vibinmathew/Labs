package com.telstra.datacat.domain.services.converters

import com.telstra.datacat.domain.CategoryResult
import com.telstra.datacat.domain.DataCategorisationResult

interface Converter {
    fun  convert(input: DataCategorisationResult, noOfCategories: Int): List<CategoryResult>
}