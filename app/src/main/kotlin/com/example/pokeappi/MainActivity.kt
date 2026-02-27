package com.example.pokeappi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pokeappi.ui.navigation.AppNav
import com.example.pokeappi.ui.theme.PokeApiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita el modo edge-to-edge
        enableEdgeToEdge()

        // Usamos la función setContent correcta de ComponentActivity
        setContent {
            PokeApiTheme {
                AppNav()
            }
        }
    }
}