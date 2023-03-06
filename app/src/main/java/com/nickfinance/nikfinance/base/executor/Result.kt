package com.nickfinance.nikfinance.base.executor

sealed class Result<Model> {
    class Error<Model>(val error: String) : Result<Model>()
    class Success<Model>(val data: Model) : Result<Model>()
}