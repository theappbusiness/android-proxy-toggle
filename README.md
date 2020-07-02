![Build Check](https://img.shields.io/github/workflow/status/theappbusiness/android-proxy-toggle/PR%20Check/main)
![Codecov report](https://img.shields.io/codecov/c/github/theappbusiness/android-proxy-toggle/main)
[![Android API](https://img.shields.io/badge/API-21%2B-blue)](https://android-arsenal.com/api?level=21)
![License](https://img.shields.io/github/license/theappbusiness/android-proxy-toggle)

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

Due to the simplicity of the app, the project has a single `app` module and the different features can be found under the `feature` package.

The app follows MVVM Clean Architecture principles.

### Dependency Injection

We currently use [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for Dependency Injection.  
Each Activity and Fragment that use DI must be annotated with `@AndroidEntryPoint`.  
Each ViewModel must be injected using `@ViewModelInject` in order to be provided using `by viewModels()`.

### Testing

We use Github Actions to execute all Unit Tests in the project for every PR against the `main` branch.
JaCoCo is setup to run test coverage.

### Features

#### DeviceSettingsManager

This class will use [Settings.Global](https://developer.android.com/reference/android/provider/Settings.Global). Since this is a system setting, we are allowed to read these settings, but we're not allowed to write them.
This small inconvenience is bypassed by granting the app `WRITE_SECURE_SETTINGS` permissions.

> Note: this is a protected permission that only System apps should be granted. Be extra careful when you grant these permissions for unknown sources apps.

In order to gran these permissions, every time you install the application, connect the device to your computer and execute the following command:

```
adb shell pm grant com.kinandcarta.create.proxytoggle android.permission.WRITE_SECURE_SETTINGS
```

#### Home Screen Widget

The app features a Home Screen widget that the user can use to quickly toggle the proxy with the last used setup, without having to even open the app.
It will also allow the user to launch the app to configure the proxy settings, if needed.

#### Quick Setting Tile

Similarly to the widget, Android 7.0 and above users will have the option to add a Quick Settings Tile in their Notification Drawer.
This tile will allow the users to toggle the proxy even without having to leave the current application they are using.

## License

Proxy Toggle is available under the MIT license. See the [LICENSE](LICENSE.md) file for more info.
