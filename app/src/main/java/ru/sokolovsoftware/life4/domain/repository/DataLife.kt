package ru.sokolovsoftware.life4.domain.repository

import ru.sokolovsoftware.life4.domain.models.Unicellular

interface DataLife {

  fun calculateLife()

  fun createUnicellular(unicellular: Unicellular)

  fun createListOfUnicellular(unicellularList: MutableList<Unicellular>): MutableList<Unicellular>

  fun getListOfUnicellular(): MutableList<Unicellular>

}
