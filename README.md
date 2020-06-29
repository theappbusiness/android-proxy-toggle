# Proxy Toggle

Small application to help Android and Test Engineers to quickly enable/disable global proxy settings without the faff of going through the annoying Network Settings path.

---

**It's everyone's responsibility to keep this document up to date as part of each release, or if you find something that needs updating.**


## Setup

No special setup required. Just clone the repository and it should build out of the box.
At the moment of writing this, the project is being created using Android Studio 4.2 Canary 2.

## About the app

### Supported devices

This app supports Android 5.0 and above.

### Architecture

The project is split in different feature modules that provide an encapsulated piece of work.
The app follows MVVM Clean Architecture.

### Dependency Injection

We currently use [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for Dependency Injection.  
Each Activity and Fragment that use DI must be annotated with `@AndroidEntryPoint`.  
Each ViewModel must be injected using `@ViewModelInject` in order to be provided using `by viewModels()`.

### Testing

TODO: Probably only Unit Test. Maybe setup CI?

## License

Proxy Toggle is available under the MIT license. See the [LICENSE](LICENSE.md) file for more info.
