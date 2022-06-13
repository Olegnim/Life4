package ru.sokolovsoftware.life4.domain.models

import java.util.*

class Unicellular(
  val name: String = "",
  val genId: Long = (1..922337203685477580).random(),
  val dateBorn: Date = Date(),
  var cx: Float = 0F,
  var cy: Float = 0F,
  var steps: Int = 0,
  var directionPath: Int = (1..360).random(),
  var type: TypeUnicellular = TypeUnicellular.LIGHT_GREEN,
  var size: Int = 10,
  var age: Int = 1,
  var satiety : Int = 0,
  var live : Boolean = true
)


