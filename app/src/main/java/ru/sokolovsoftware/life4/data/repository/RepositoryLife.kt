package ru.sokolovsoftware.life4.data.repository


import android.content.res.Resources
import android.os.SystemClock
import ru.sokolovsoftware.life4.domain.models.TypeUnicellular
import ru.sokolovsoftware.life4.domain.models.Unicellular
import ru.sokolovsoftware.life4.domain.repository.DataLife
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin


class RepositoryLife : DataLife {
  private var unicellularList: MutableList<Unicellular> = ArrayList()
  private var copyUnicellularList: MutableList<Unicellular> = ArrayList()
  var lifeLoop = 0
  private val lifeCycle = 30
  private val bigSize = 50
  private var sleep = 100
  private var prevTime: Long = SystemClock.uptimeMillis()
  private var canvasHeight: Int = Resources.getSystem().displayMetrics.heightPixels
  private var canvasWidth: Int = Resources.getSystem().displayMetrics.widthPixels
  private val rightDownSector: IntRange = (0..90)
  private val leftDownSector: IntRange = (91..180)
  private val leftUpperSector: IntRange = (181..270)
  private val rightUpperSector: IntRange = (271..359)
  var globalCycle: Long = 0

  override fun calculateLife() {
    val stepsQuantity: IntRange = (50..150)
    val randomStep: IntRange = (-3..3)
    val currentTime: Long = SystemClock.uptimeMillis()
    val redFullSatiety = 2
    val blueFullSatiety = 3
    val greenFullSatiety = 20
    globalCycle++
    if (currentTime <= prevTime + sleep) return else prevTime = SystemClock.uptimeMillis()
    lifeLoop++
    for (unicellular in unicellularList) {
      unicellular.age++
      if (lifeLoop == lifeCycle) unicellular.size += 1
      if (unicellular.type == TypeUnicellular.LIGHT_GREEN) {
        unicellular.cx += randomStep.random()
        unicellular.cy += randomStep.random()
        continue
      }
      unicellular.cx =
        (unicellular.cx + (1..5).random() + 10 * cos(Math.toRadians(unicellular.directionPath.toDouble()))).toFloat()
      unicellular.cy =
        (unicellular.cy + (1..5).random() + 10 * sin(Math.toRadians(unicellular.directionPath.toDouble()))).toFloat()
      unicellular.steps++
      if (unicellular.steps > stepsQuantity.random()) {
        unicellular.steps = 0
        if (unicellular.directionPath < 180) {
          unicellular.directionPath += (45..90).random()
        } else {
          unicellular.directionPath -= (45..90).random()
        }
      }
      if (unicellular.cx + unicellular.size > canvasWidth) {
        unicellular.cx = canvasWidth - unicellular.size - 10F
        unicellular.directionPath = changePath(unicellular.directionPath)
        unicellular.steps = 0
      }
      if (unicellular.cy + unicellular.size > canvasHeight) {
        unicellular.cy = canvasHeight - unicellular.size - 10F
        unicellular.directionPath = changePath(unicellular.directionPath)
        unicellular.steps = 0
      }
      if (unicellular.cx <= 5 + unicellular.size) {
        unicellular.cx = unicellular.size + 10F
        unicellular.directionPath = changePath(unicellular.directionPath)
        unicellular.steps = 0
      }
      if (unicellular.cy <= 5 + unicellular.size) {
        unicellular.cy = unicellular.size + 10F
        unicellular.directionPath = changePath(unicellular.directionPath)
        unicellular.steps = 0
      }
      if (unicellular.type == TypeUnicellular.RED) {
        for (unicellular2 in unicellularList) {
          val hasContact = unicellularHasContact(unicellular, unicellular2)
          val sameTypeUnicellular = unicellular.type == unicellular2.type
          val notSelf = unicellular.genId != unicellular2.genId
          if (hasContact && unicellular2.live
            && unicellular2.type != TypeUnicellular.LIGHT_GREEN
            && !sameTypeUnicellular
            && unicellular.satiety <= redFullSatiety
          ) {
            unicellular2.live = false
            if (unicellular.size < 30) unicellular.size += 4
            unicellular.satiety++
          } else if (hasContact && unicellular2.live
            && sameTypeUnicellular && notSelf
          ) {
            unicellular2.directionPath = changePath(unicellular2.directionPath)
          } else if (hasContact && unicellular2.live
            && unicellular2.type == TypeUnicellular.LIGHT_GREEN && notSelf
          ) {
            if (unicellular.directionPath in rightDownSector) {
              unicellular2.cx += unicellular.size / 2
              unicellular2.cy += unicellular.size / 2
            } else if (unicellular.directionPath in leftDownSector) {
              unicellular2.cx -= unicellular.size / 2
              unicellular2.cy += unicellular.size / 2
            } else if (unicellular.directionPath in leftUpperSector) {
              unicellular2.cx -= unicellular.size / 2
              unicellular2.cy += unicellular.size / 2
            } else if (unicellular.directionPath in rightUpperSector) {
              unicellular2.cx += unicellular.size / 2
              unicellular2.cy -= unicellular.size / 2
            }
          }
        }
      }
      if (unicellular.type == TypeUnicellular.GREEN) {
        for (unicellular2 in unicellularList) {
          val hasContact = unicellularHasContact(unicellular, unicellular2)
          val sameTypeUnicellular = unicellular.type == unicellular2.type
          val notSelf = unicellular.genId != unicellular2.genId
          if (hasContact && unicellular2.live && unicellular.satiety <= greenFullSatiety
            && unicellular2.type == TypeUnicellular.LIGHT_GREEN
          ) {
            unicellular2.live = false
            if (unicellular.size < 30) unicellular.size += 1
            unicellular.satiety++
          } else if (hasContact && unicellular2.live && sameTypeUnicellular && notSelf) {
            unicellular2.directionPath = changePath(unicellular2.directionPath)
          } else if (hasContact && unicellular2.live
            && unicellular2.type == TypeUnicellular.LIGHT_GREEN && notSelf
            && unicellular.satiety > greenFullSatiety
          ) {
            if (unicellular.directionPath in rightDownSector) {
              unicellular2.cx += unicellular.size / 2
              unicellular2.cy += unicellular.size / 2
            } else if (unicellular.directionPath in leftDownSector) {
              unicellular2.cx -= unicellular.size / 2
              unicellular2.cy += unicellular.size / 2
            } else if (unicellular.directionPath in leftUpperSector) {
              unicellular2.cx -= unicellular.size / 2
              unicellular2.cy += unicellular.size / 2
            } else if (unicellular.directionPath in rightUpperSector) {
              unicellular2.cx += unicellular.size / 2
              unicellular2.cy -= unicellular.size / 2
            }
          }
        }
      }
      if (unicellular.type == TypeUnicellular.BLUE) {
        for (unicellular2 in unicellularList) {
          val hasContact = unicellularHasContact(unicellular, unicellular2)
          val sameTypeUnicellular = unicellular.type == unicellular2.type
          val notSelf = unicellular.genId != unicellular2.genId
          if (hasContact && unicellular2.live && unicellular.satiety <= blueFullSatiety &&
            (unicellular2.type == TypeUnicellular.GREEN
              || unicellular2.type == TypeUnicellular.RED && unicellular2.size < unicellular.size)
          ) {
            unicellular2.live = false
            if (unicellular.size < 30) unicellular.size += 2
            unicellular.satiety++
          } else if (hasContact && unicellular2.live && sameTypeUnicellular && notSelf) {
            unicellular2.directionPath = changePath(unicellular2.directionPath)
          } else if (hasContact && unicellular2.live
            && unicellular2.type == TypeUnicellular.LIGHT_GREEN && notSelf
          ) {
            if (unicellular.directionPath in rightDownSector) {
              unicellular2.cx += unicellular.size / 2
              unicellular2.cy += unicellular.size / 2
            } else if (unicellular.directionPath in leftDownSector) {
              unicellular2.cx -= unicellular.size / 2
              unicellular2.cy += unicellular.size / 2
            } else if (unicellular.directionPath in leftUpperSector) {
              unicellular2.cx -= unicellular.size / 2
              unicellular2.cy += unicellular.size / 2
            } else if (unicellular.directionPath in rightUpperSector) {
              unicellular2.cx += unicellular.size / 2
              unicellular2.cy -= unicellular.size / 2
            }
          }
        }
      }
    }
    copyUnicellularList.clear()
    for (unicellular in unicellularList) { // цикл смерти и рождения
      if (unicellular.age > 200 && unicellular.type == TypeUnicellular.LIGHT_GREEN) {
        unicellular.live = false
        var l: Int = 2
        if (unicellularList.size > 1500) l = 1
        if (unicellularList.size < 700) l = 3
        for (i in 1..l) copyUnicellularList.add(
          Unicellular(
            name = "NewNuc$unicellular.name$i",
            cx = (10..canvasWidth).random().toFloat(),
            cy = (10..canvasHeight).random().toFloat(),
            size = 3
          )
        )
      } else if (unicellular.age > 400 && unicellular.type != TypeUnicellular.LIGHT_GREEN && unicellular.type == TypeUnicellular.RED) {
        unicellular.live = false
      } else if (unicellular.age > 500 && unicellular.type != TypeUnicellular.LIGHT_GREEN && unicellular.type == TypeUnicellular.BLUE) {
        unicellular.live = false
      } else if (unicellular.age > 700 && unicellular.type != TypeUnicellular.LIGHT_GREEN && unicellular.type == TypeUnicellular.GREEN) {
        unicellular.live = false
      }
      if (unicellular.live && unicellular.type == TypeUnicellular.RED && unicellular.satiety >= redFullSatiety && unicellular.age > 150) {
        unicellular.live = false
        for (i in 1..2) {
          copyUnicellularList.add(
            Unicellular(
              name = "NewNuc$unicellular.name",
              cx = unicellular.cx,
              cy = unicellular.cy,
              type = TypeUnicellular.RED,
            )
          )
        }
      }
      if (unicellular.live && unicellular.type == TypeUnicellular.GREEN && unicellular.satiety >= greenFullSatiety && unicellular.age > 10) {
        unicellular.live = false
        for (i in 1..5) {
          copyUnicellularList.add(
            Unicellular(
              name = "NewNuc$unicellular.name$i",
              cx = unicellular.cx + i,
              cy = unicellular.cy + i,
              type = TypeUnicellular.GREEN,
            )
          )
        }
      }
      if (unicellular.live && unicellular.type == TypeUnicellular.BLUE && unicellular.satiety >= blueFullSatiety && unicellular.age > 100) {
        unicellular.live = false
        for (i in 1..2) {
          copyUnicellularList.add(
            Unicellular(
              name = "NewNuc$unicellular.name$i",
              cx = unicellular.cx + i,
              cy = unicellular.cy + i,
              type = TypeUnicellular.BLUE,
            )
          )
        }
      }
      if (unicellular.live) {
        copyUnicellularList.add(unicellular)
      }
    }
    unicellularList.clear()
    unicellularList.addAll(copyUnicellularList)
    if (lifeLoop == lifeCycle) lifeLoop = 0
  }

  private fun unicellularHasContact(u1: Unicellular, u2: Unicellular): Boolean {
    val distanceX = abs(u1.cx - u2.cx)
    val distanceY = abs(u1.cy - u2.cy)
    val distance = hypot(distanceX, distanceY)
    val r = (u1.size + u2.size)
    return (distance < r)
  }

  private fun changePath(directionPath: Int): Int {
    var result = directionPath
    if (directionPath in rightDownSector) {
      result = leftUpperSector.random()
    } else if (directionPath in leftDownSector) {
      result = rightUpperSector.random()
    } else if (directionPath in leftUpperSector) {
      result = rightDownSector.random()
    } else if (directionPath in rightUpperSector) {
      result = leftDownSector.random()
    }
    return result
  }

  override fun createUnicellular(unicellular: Unicellular) {
    if (unicellular.cx.equals(0F) || unicellular.cx < 0) {
      unicellular.cx += (10..canvasWidth).random().toFloat()
    } else if (unicellular.cx > canvasWidth) {
      unicellular.cx = canvasWidth - (50..100).random().toFloat()
    }
    if (unicellular.cy.equals(0F) || unicellular.cy < 0) {
      unicellular.cy += (10..canvasHeight).random().toFloat()
    } else if (unicellular.cy > canvasHeight) {
      unicellular.cy = canvasHeight - (50..100).random().toFloat()
    }
    this.unicellularList.add(unicellular)
  }

  override fun createListOfUnicellular(unicellularList: MutableList<Unicellular>): MutableList<Unicellular> {
    for (unicellular in unicellularList) {
      createUnicellular(unicellular)
    }
    return unicellularList
  }

  override fun getListOfUnicellular(): MutableList<Unicellular> {
    return unicellularList
  }

  fun setCanvasSize(width: Int, height: Int) {
    canvasWidth = width
    canvasHeight = height
  }
}
