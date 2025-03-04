import mu.KLogging

class ClientService {

    fun saveClient(client: Client): Client = client
        .also { validateClient(client) }
        .let { saveToMyPhantomDB(client) }
        .also { logger.info { "Успешно сохранена новая версия $it" } }

    fun validateClient(client: Client) {
        val errorList = ArrayList<ErrorCode>()
        errorList.addAll(PhoneValidator().validate(client.phone))
        errorList.addAll(FirstNameValidator().validate(client.firstName))
        errorList.addAll(LastNameValidator().validate(client.lastName))
        errorList.addAll(EmailValidator().validate(client.email))
        errorList.addAll(SnilsValidator().validate(client.snils))
        if (errorList.isNotEmpty()) {
            throw ValidationException(*errorList.toTypedArray())
        }
    }

    private fun saveToMyPhantomDB(client: Client) = client
        .also { it.incrementVersion() }

    companion object : KLogging()
}

fun main() {
    val client = Client(
        phone = "1234567",
        firstName = null,
        lastName = null,
        email = null,
        snils = null,
        version = 1
    )

    val clientService = ClientService()
    clientService.validateClient(client)

}