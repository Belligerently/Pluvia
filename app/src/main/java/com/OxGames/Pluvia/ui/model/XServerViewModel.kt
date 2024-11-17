package com.OxGames.Pluvia.ui.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.OxGames.Pluvia.ui.data.XServerState
import com.winlator.container.Container
import com.winlator.container.Shortcut
import com.winlator.core.EnvVars
import com.winlator.core.KeyValueSet
import com.winlator.core.WineInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class XServerViewModel : ViewModel() {
    private val _xServerState = MutableStateFlow(XServerState())
    val xServerState: StateFlow<XServerState> = _xServerState.asStateFlow()

    // fun setEnvVars(envVars: EnvVars) {
    //     _xServerState.update { currentState ->
    //         currentState.copy(envVars = envVars)
    //     }
    // }
    fun setDxwrapper(dxwrapper: String) {
        _xServerState.update { currentState ->
            currentState.copy(dxwrapper = dxwrapper)
        }
    }
    fun setDxwrapperConfig(dxwrapperConfig: KeyValueSet?) {
        _xServerState.update { currentState ->
            Log.d("XServerViewModel", "Setting dxwrapperConfig to $dxwrapperConfig")
            currentState.copy(dxwrapperConfig = dxwrapperConfig)
        }
    }
    // fun setShortcut(shortcut: Shortcut?) {
    //     _xServerState.update { currentState ->
    //         currentState.copy(shortcut = shortcut)
    //     }
    // }
    fun setScreenSize(screenSize: String) {
        _xServerState.update { currentState ->
            currentState.copy(screenSize = screenSize)
        }
    }
    fun setWineInfo(wineInfo: WineInfo) {
        _xServerState.update { currentState ->
            currentState.copy(wineInfo = wineInfo)
        }
    }
    fun setGraphicsDriver(graphicsDriver: String) {
        _xServerState.update { currentState ->
            currentState.copy(graphicsDriver = graphicsDriver)
        }
    }
    fun setAudioDriver(audioDriver: String) {
        _xServerState.update { currentState ->
            currentState.copy(audioDriver = audioDriver)
        }
    }
}