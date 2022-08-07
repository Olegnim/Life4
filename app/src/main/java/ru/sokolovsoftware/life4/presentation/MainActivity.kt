package ru.sokolovsoftware.life4.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.sokolovsoftware.life4.domain.models.TypeUnicellular
import ru.sokolovsoftware.life4.domain.models.Unicellular

class MainActivity : AppCompatActivity() {

  private val startLife: Init = Init()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(DrawView(this, startLife.initLife()))
  }
}
