# coauthoredby

Is a web app that builds the `Co-authored-by` trailer for your Git commit message. It gives credit to the GitHub users who helped with the commit. Enter the GitHub usernames, and the app creates the trailer in the format that GitHub accepts. Try it at https://ghostbear.me/coauthoredby/.

## Build
 
Run `./gradlew build`.

The build writes the app files to `webApp/build/dist`.

## Develop

To run the app in development mode:

1. Run `./gradlew :webApp:jsDevelopmentRun`.
2. Open the URL that Gradle prints.

For the WebAssembly build, run `./gradlew :webApp:wasmJsDevelopmentRun` instead.
