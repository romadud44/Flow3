import kotlinx.coroutines.flow.*
import kotlin.random.Random

suspend fun main() {
    val persons: MutableList<Person> = mutableListOf()

    val finishFlow = allInOneFlows(names, getCardFlow(5), getPasswordFlow(5))
    finishFlow.collect { person -> persons.add(person) }
    println(persons)
}

data class Person(val name: String, val card: String, val pass: String) {
    override fun toString(): String {
        return "\nИмя: $name Номер карты: $card Пароль:$pass"
    }
}

data class NameCard(val name: String, val card: String)

val names = listOf("Ivan", "Roman", "Olga", "Victor", "Petr").asFlow()
fun getCardFlow(count: Int) = getListOfCard(count).asFlow()
fun getPasswordFlow(count: Int) = getListOfPassword(count).asFlow()

fun createPassword(): String {
    val chars = "1234567890".toCharArray()
    var password = ""
    for (i in 0..<4) {
        val randomIndex = Random.nextInt(chars.size)
        password = "$password${(chars[randomIndex])}"
    }
    return password
}

fun getListOfPassword(count: Int): List<String> {
    var position = 0
    val list: MutableList<String> = mutableListOf()
    while (true) {
        if (position < count) {
            val pass = createPassword()
            list.add(pass)
            position++
        } else break
    }
    return list.toList()
}

fun createCard(): String {
    val chars = "1234567890".toCharArray()
    var number = ""
    for (i in 0..<19) {
        val randomIndex = Random.nextInt(chars.size)
        number = if ((i == 4) || (i == 9) || (i == 14)) {
            "$number "
        } else {
            "$number${(chars[randomIndex])}"
        }
    }
    return number
}

fun getListOfCard(count: Int): List<String> {
    var position = 0
    val list: MutableList<String> = mutableListOf()
    while (true) {
        if (position < count) {
            val pass = createCard()
            list.add(pass)
            position++
        } else break
    }
    return list.toList()
}

fun allInOneFlows(first: Flow<String>, second: Flow<String>, third: Flow<String>): Flow<Person> {
    val personListFlow = first.zip(second) { name, card -> NameCard(name, card) }
        .zip(third) { nameCard, pass ->
            Person(nameCard.name, nameCard.card, pass)
        }
    return personListFlow
}
