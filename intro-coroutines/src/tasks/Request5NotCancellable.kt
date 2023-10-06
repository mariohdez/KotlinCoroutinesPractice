package tasks

import contributors.*
import kotlinx.coroutines.*
import kotlinx.coroutines.repackaged.net.bytebuddy.agent.builder.AgentBuilder.CircularityLock.Global
import kotlin.coroutines.coroutineContext

suspend fun loadContributorsNotCancellable(service: GitHubService, req: RequestData): List<User> {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()

    val deferreds = repos.map { repo ->
        GlobalScope.async {
            log("Start loading for ${repo.name}")
            delay(3000)
            service.getRepoContributors(req.org, repo.name)
                .also { users -> logUsers(repo, users) }
                .bodyList()
        }
    }

    return deferreds.awaitAll().flatten().aggregate()
}
