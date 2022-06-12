package ru.sokolovsoftware.life4.domain.models

import java.util.*

class Unicellular(
  val name: String = "",
  val genId: Long = (1..9223372036854775807).random(),
  val dateBorn: Date = Date(),
  var cx: Float = 0F,
  var cy: Float = 0F,
  var steps: Int = 0,
  var directionPath: Int = (1..360).random(),
  val type: TypeUnicellular = TypeUnicellular.LIGHT_GREEN,
  var age: Int = 10,
)


