
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val statusCode: Int? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(data: T? = null, message: String, statusCode: Int? = null) : Resource<T>(data, message, statusCode)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
