package ru.sokolovsoftware.life4.presentation

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import ru.sokolovsoftware.life4.domain.models.TypeUnicellular
import ru.sokolovsoftware.life4.domain.models.Unicellular

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(DrawView(this, initLife()))
  }

  private fun initLife() : MutableList<Unicellular> {
    val listUnicellular : MutableList<Unicellular> = ArrayList()
    for(i in 1..30) {
      listUnicellular.add(Unicellular("Vasya$i", type = TypeUnicellular.RED))
      listUnicellular.add(Unicellular("Piter$i", type = TypeUnicellular.BLUE))
      listUnicellular.add(Unicellular("Sasha$i", type = TypeUnicellular.GREEN))
    }
    for(i in 1..100) {
      listUnicellular.add(Unicellular("bacteria$i"))
    }
    return listUnicellular
  }
}
