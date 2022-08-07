package ru.sokolovsoftware.life4.presentation

import ru.sokolovsoftware.life4.domain.models.TypeUnicellular
import ru.sokolovsoftware.life4.domain.models.Unicellular

class Init : InitData{

  override fun initLife(): MutableList<Unicellular> {
    val listUnicellular: MutableList<Unicellular> = ArrayList()
    for (i in 1..30) {
      listUnicellular.add(Unicellular("Sasha$i", type = TypeUnicellular.GREEN))
    }
    for (i in 1..10) {
      listUnicellular.add(Unicellular("Vasya$i", type = TypeUnicellular.RED))
      listUnicellular.add(Unicellular("Piter$i", type = TypeUnicellular.BLUE))
    }
    var y = 1
    for (i in 1..1000) {
      if (y < 190) y ++ else y = 0
      listUnicellular.add(Unicellular("bacteria$i", size = 2, age = y))
    }
    return listUnicellular
  }
}
