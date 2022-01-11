import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.nodeJS
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.2"

project {

    vcsRoot(Playwright)
    vcsRoot(HttpsGithubComNodejsNodejsOrgRefsHeadsMain)
    vcsRoot(HttpsGithubComArtb1shPlaywrightTeamcityRefsHeadsMain)

    buildType(Nodejs)
}

object Nodejs : BuildType({
    name = "nodejs"

    artifactRules = """
        jest-html-reporters-attach => jest-html-reporters-attach
        report.html
        src/html-report => src/html-report
    """.trimIndent()
    publishArtifacts = PublishMode.ALWAYS

    vcs {
        root(HttpsGithubComArtb1shPlaywrightTeamcityRefsHeadsMain)
    }

    steps {
        nodeJS {
            shellScript = """
                npm install --force
                npm test
            """.trimIndent()
            dockerImage = "mcr.microsoft.com/playwright"
        }
        nodeJS {
            enabled = false
            shellScript = """
                npm install eslint-teamcity --no-save
                npm run test:lint:js -- --format ./node_modules/eslint-teamcity/index.js
            """.trimIndent()
        }
        nodeJS {
            enabled = false
            shellScript = "npm run test"
        }
    }
})

object HttpsGithubComArtb1shPlaywrightTeamcityRefsHeadsMain : GitVcsRoot({
    name = "https://github.com/artb1sh/playwright-teamcity#refs/heads/main"
    url = "https://github.com/artb1sh/playwright-teamcity"
    branch = "refs/heads/main"
    authMethod = password {
        userName = "artb1sh"
        password = "credentialsJSON:93d6c368-5feb-45bc-9b7e-04dc408d3520"
    }
})

object HttpsGithubComNodejsNodejsOrgRefsHeadsMain : GitVcsRoot({
    name = "https://github.com/nodejs/nodejs.org#refs/heads/main"
    url = "https://github.com/nodejs/nodejs.org"
    branch = "refs/heads/main"
})

object Playwright : GitVcsRoot({
    name = "playwright"
    url = "https://github.com/thenishant/playwright-teamcity.git"
    branch = "refs/heads/main"
})
