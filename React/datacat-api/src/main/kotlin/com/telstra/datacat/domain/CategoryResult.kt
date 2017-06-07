package com.telstra.datacat.domain

import com.telstra.datacat.adapters.databases.repositories.DataCategories
import java.math.BigInteger

data class CategoryResult(val category : DataCategories, val amount : BigInteger)