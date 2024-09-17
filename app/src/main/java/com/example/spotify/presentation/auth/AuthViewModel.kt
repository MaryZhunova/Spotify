package com.example.spotify.presentation.auth

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.BuildConfig
import com.example.spotify.domain.SpotifyInfoRepository
import com.example.spotify.domain.auth.AuthRepository
import com.example.spotify.presentation.models.AuthError
import com.example.spotify.presentation.models.AuthState
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Вью модель, управляющая состоянием аутентификации
 *
 * @constructor
 * @param authRepository репозиторий для хранения токенов доступа
 * @param infoRepository репозитория для получения информации о треках и исполнителях из Spotify
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val infoRepository: SpotifyInfoRepository
) : ViewModel() {

    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)

    /**
     * Состояние процесса авторизации
     */
    val authState: State<AuthState> get() = _authState

    init {
        logout()
    }

    /**
     * Инициирует процесс аутентификации в Spotify
     *
     * Эта функция пытается запустить активити для входа в Spotify, используя переданный `authLauncher`.
     * Сначала она проверяет, установлено ли приложение Spotify на устройстве. Если оно установлено,
     * создается запрос авторизации, и начинается процесс аутентификации. Если приложение не установлено,
     * состояние аутентификации меняется на `Fail` с ошибкой, указывающей, что Spotify недоступен
     *
     * @param activity текущая активити, из которой будет запущен intent для входа в Spotify
     * @param authLauncher лаунчер, который обрабатывает запуск intent для входа и получение результата
     */
    fun startAuth(activity: Activity, authLauncher: ActivityResultLauncher<Intent>) {
        try {
            activity.packageManager.getPackageInfo(PACKAGE_NAME, 0)

            val request = AuthorizationRequest.Builder(
                BuildConfig.CLIENT_ID, AuthorizationResponse.Type.CODE, REDIRECT_URI
            ).setScopes(arrayOf(USER_READ_PRIVATE, USER_READ_EMAIL, USER_TOP_READ)).build()

            val authIntent = AuthorizationClient.createLoginActivityIntent(activity, request)
            authLauncher.launch(authIntent)

        } catch (e: PackageManager.NameNotFoundException) {
            _authState.value = AuthState.Fail(error = AuthError.NO_SPOTIFY)
        }
    }

    /**
     * Обрабатывает результат аутентификации в Spotify
     *
     * Эта функция обрабатывает результат активити для входа в Spotify. Если аутентификация
     * прошла успешно и был получен код авторизации, она запрашивает токен доступа
     * В случае успеха состояние аутентификации меняется на `Success`. В случае ошибки состояние
     * меняется на `Fail` с соответствующей ошибкой
     *
     * @param resultCode код результата, указывающий на успех или неудачу входа
     * @param data intent, возвращенный из активити, содержащий ответ авторизации
     */
    fun handleAuthResult(resultCode: Int, data: Intent?) = viewModelScope.launch {
        val response = AuthorizationClient.getResponse(resultCode, data)
        if (response.type == AuthorizationResponse.Type.CODE &&
            authRepository.obtainAccessToken(response.code, REDIRECT_URI) != null
        ) {
            _authState.value = AuthState.Success
        } else {
            _authState.value = AuthState.Fail(error = AuthError.AUTH_FAIL)
        }
    }

    /**
     * Выполняет выход пользователя, очищая данные аутентификации и информацию о пользователе
     */
    fun logout() = viewModelScope.launch {
        authRepository.clear()
        infoRepository.clear()
        _authState.value = AuthState.Idle
    }

    companion object {
        private const val REDIRECT_URI = "android-app://stats/auth"
        private const val PACKAGE_NAME = "com.spotify.music"
        private const val USER_READ_PRIVATE = "user-read-private"
        private const val USER_READ_EMAIL = "user-read-email"
        private const val USER_TOP_READ = "user-top-read"
    }
}