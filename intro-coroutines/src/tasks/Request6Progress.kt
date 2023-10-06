package tasks

import contributors.*
import kotlinx.coroutines.awaitAll
import java.util.concurrent.CountDownLatch

suspend fun loadContributorsProgress(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
    val repos = service.getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()

    var allUsers = emptyList<User>()

    repos.forEachIndexed { index, repo ->
        val users = service.getRepoContributors(req.org, repo.name)
            .also { users -> logUsers(repo, users) }
            .bodyList()

        allUsers = (allUsers + users).aggregate()

        updateResults(allUsers.toList(), index == repos.lastIndex)
    }
}
