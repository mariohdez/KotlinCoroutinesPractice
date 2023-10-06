package tasks

import contributors.*
import kotlinx.coroutines.*
import retrofit2.Response

suspend fun loadContributorsConcurrent(service: GitHubService, req: RequestData): List<User> = coroutineScope {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()

    val deferreds = repos.map { repo ->
        async {
            log("Start loading for ${repo.name}")
            delay(3000)
            service.getRepoContributors(req.org, repo.name)
                .also { users -> logUsers(repo, users) }
                .bodyList()
        }
    }

    deferreds.awaitAll().flatten().aggregate()
}