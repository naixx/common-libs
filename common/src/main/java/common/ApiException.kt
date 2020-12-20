package common

class ApiException(val code: Int, val apiMessage: String?): Exception("ApiException(code=$code, apiMessage=$apiMessage)")
