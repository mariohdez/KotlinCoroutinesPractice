package tasks

import contributors.User

fun List<User>.aggregate(): List<User> {
    return this.groupBy { it.login }
        .map { kv -> User(kv.key, kv.value.sumOf { it.contributions }) }
        .sortedByDescending { it.contributions }
}
