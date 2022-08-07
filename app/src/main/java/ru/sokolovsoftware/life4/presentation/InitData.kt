package ru.sokolovsoftware.life4.presentation

import ru.sokolovsoftware.life4.domain.models.Unicellular

interface InitData {

  fun initLife():MutableList<Unicellular>
}
